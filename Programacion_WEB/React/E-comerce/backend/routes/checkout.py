# backend/routes/checkout.py
from flask import Blueprint, request, jsonify, current_app
from flask_jwt_extended import jwt_required, get_jwt_identity
from models.user import User
from models.product import Product # Para verificar stock
from models.order import Order, OrderItem # Para guardar la orden en tu DB
from db import mysql # Para transacciones

import requests # Para hacer peticiones HTTP a la API de PayPal
import os

checkout_bp = Blueprint('checkout', __name__)

# --- HELPERS ---
# Helper para obtener un token de acceso de PayPal
def get_paypal_access_token():
    client_id = os.getenv('PAYPAL_CLIENT_ID')
    client_secret = os.getenv('PAYPAL_SECRET')
    paypal_api_base_url = os.getenv('PAYPAL_API_BASE_URL')

    if not client_id or not client_secret or not paypal_api_base_url:
        current_app.logger.error("Credenciales de PayPal no configuradas en variables de entorno.")
        return None

    auth_url = f"{paypal_api_base_url}/v1/oauth2/token"
    headers = {
        "Accept": "application/json",
        "Accept-Language": "en_US"
    }
    data = {
        "grant_type": "client_credentials"
    }
    
    try:
        response = requests.post(auth_url, headers=headers, data=data, auth=(client_id, client_secret))
        response.raise_for_status() # Lanza excepción para códigos de estado HTTP erróneos
        return response.json()['access_token']
    except requests.exceptions.RequestException as e:
        current_app.logger.error(f"Error al obtener token de acceso de PayPal: {e}")
        return None

# --- RUTAS DE CHECKOUT ---

# RUTA 1: Crear una orden de PayPal
@checkout_bp.route('/checkout/create-paypal-order', methods=['POST'])
@jwt_required()
def create_paypal_order():
    current_user_id = get_jwt_identity()
    user = User.find_by_id(current_user_id)
    if not user:
        return jsonify({"msg": "Usuario no encontrado"}), 404

    data = request.get_json()
    items = data.get('items')
    frontend_total_price = float(data.get('total_price', 0))

    if not items:
        return jsonify({"msg": "No hay productos en el carrito."}), 400

    # Validar productos y stock en el backend
    # Calcula el total en el backend para evitar manipulaciones del cliente
    calculated_total = 0
    paypal_items = []
    
    for item in items:
        product_id = item.get('product_id')
        quantity = item.get('quantity')
        price = float(item.get('price')) # Precio del frontend
        name = item.get('name') # Nombre del frontend

        product = Product.find_by_id(product_id)
        if not product or not product.activo:
            return jsonify({"msg": f"El producto '{name}' ya no está disponible o está inactivo."}), 400
        if product.stock < quantity:
            return jsonify({"msg": f"Stock insuficiente para '{name}'. Disponible: {product.stock}"}), 400
        
        # Usar el precio del backend para calcular el total
        calculated_total += product.price * quantity 
        
        paypal_items.append({
            "name": product.name,
            "quantity": str(quantity),
            "unit_amount": {
                "currency_code": "USD",
                "value": str(product.price) # Usar el precio del backend
            }
        })
    
    # Tolerancia por posibles diferencias de coma flotante
    if abs(calculated_total - frontend_total_price) > 0.01:
        current_app.logger.warning(f"Discrepancia de precio: Frontend Total {frontend_total_price}, Backend Total {calculated_total}")
        # return jsonify({"msg": "Discrepancia en el precio total. Por favor, recarga el carrito."}), 400
        # Puedes decidir si esto debe ser un error o solo un warning.
        # Para simplificar por ahora, permitiremos, pero es buena práctica ser estricto.

    access_token = get_paypal_access_token()
    if not access_token:
        return jsonify({"msg": "No se pudo obtener el token de acceso de PayPal."}), 500

    paypal_api_base_url = os.getenv('PAYPAL_API_BASE_URL')
    create_order_url = f"{paypal_api_base_url}/v2/checkout/orders"
    
    order_payload = {
        "intent": "CAPTURE",
        "purchase_units": [
            {
                "items": paypal_items,
                "amount": {
                    "currency_code": "USD",
                    "value": f"{calculated_total:.2f}", # Formatear a 2 decimales
                    "breakdown": {
                        "item_total": {
                            "currency_code": "USD",
                            "value": f"{calculated_total:.2f}"
                        }
                    }
                }
            }
        ],
        "application_context": {
            "return_url": "http://localhost:3000/paypal-success", # URL de éxito de tu frontend
            "cancel_url": "http://localhost:3000/paypal-cancel"   # URL de cancelación de tu frontend
        }
    }

    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {access_token}"
    }

    try:
        response = requests.post(create_order_url, headers=headers, json=order_payload)
        response.raise_for_status()
        paypal_order_data = response.json()
        current_app.logger.info(f"Orden de PayPal creada: {paypal_order_data.get('id')}")
        return jsonify(paypal_order_data)
    except requests.exceptions.RequestException as e:
        current_app.logger.error(f"Error al crear la orden de PayPal: {e.response.text if e.response else e}")
        return jsonify({"msg": "Error al contactar con la API de PayPal para crear la orden."}), 500

# RUTA 2: Capturar una orden de PayPal
@checkout_bp.route('/checkout/capture-paypal-order', methods=['POST'])
@jwt_required()
def capture_paypal_order():
    current_user_id = get_jwt_identity()
    user = User.find_by_id(current_user_id)
    if not user:
        return jsonify({"msg": "Usuario no encontrado"}), 404

    data = request.get_json()
    order_id = data.get('orderID') # ID de la orden de PayPal
    items = data.get('items') # Items del carrito desde el frontend
    frontend_total_price = float(data.get('total_price', 0)) # Total del carrito desde el frontend

    if not order_id:
        return jsonify({"msg": "ID de orden de PayPal no proporcionado."}), 400

    access_token = get_paypal_access_token()
    if not access_token:
        return jsonify({"msg": "No se pudo obtener el token de acceso de PayPal."}), 500

    paypal_api_base_url = os.getenv('PAYPAL_API_BASE_URL')
    capture_order_url = f"{paypal_api_base_url}/v2/checkout/orders/{order_id}/capture"

    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {access_token}"
    }

    try:
        response = requests.post(capture_order_url, headers=headers)
        response.raise_for_status()
        capture_data = response.json()

        if capture_data['status'] == 'COMPLETED':
            # --- Lógica de procesamiento de la orden en tu base de datos ---
            current_app.logger.info(f"PayPal Order {order_id} CAPTURADA. Procesando en DB...")
            
            # 1. Validar productos y stock FINALMENTE antes de procesar
            # Esta validación es CRÍTICA porque el stock puede haber cambiado entre create y capture.
            calculated_total = 0
            product_quantities = {} # Para facilitar la actualización de stock
            
            for item in items:
                product_id = item.get('product_id')
                quantity = item.get('quantity')
                price_from_frontend = float(item.get('price')) # Solo para referencia, usar product.price de DB
                
                product = Product.find_by_id(product_id)
                if not product or not product.activo:
                    current_app.logger.error(f"Producto {product_id} no encontrado o inactivo durante la captura de PayPal.")
                    # Deberías tener una lógica de reversión o manejo de errores aquí
                    return jsonify({"msg": "Error: uno o más productos ya no están disponibles."}), 500
                if product.stock < quantity:
                    current_app.logger.error(f"Stock insuficiente para producto {product_id} durante la captura de PayPal. Disponible: {product.stock}, Solicitado: {quantity}")
                    return jsonify({"msg": "Error: stock insuficiente para uno o más productos."}), 500
                
                calculated_total += product.price * quantity
                product_quantities[product.id] = quantity # Guardar cantidad para actualizar stock

            # Opcional: Re-verificar total capturado con el calculado
            paypal_captured_amount = float(capture_data['purchase_units'][0]['payments']['captures'][0]['amount']['value'])
            if abs(calculated_total - paypal_captured_amount) > 0.01:
                current_app.logger.error(f"Discrepancia de precio entre backend calculado ({calculated_total}) y PayPal capturado ({paypal_captured_amount}) para la orden {order_id}.")
                # Posiblemente revertir la transacción o marcarla para revisión
                return jsonify({"msg": "Discrepancia en el monto de pago. Contacta soporte."}), 500

            # 2. Iniciar una transacción MySQL
            cursor = mysql.connection.cursor()
            try:
                # 3. Crear la orden principal en tu DB (orders table)
                order_id_db = Order.create(user.id, calculated_total, 'completed', order_id, cursor)
                if not order_id_db:
                    raise Exception("Fallo al crear la orden principal en la base de datos.")

                # 4. Crear los items de la orden y actualizar el stock
                for item in items:
                    product_id = item.get('product_id')
                    quantity = item.get('quantity')
                    product = Product.find_by_id(product_id) # Volver a obtener el producto para su precio actual
                    
                    order_item_id = OrderItem.create(order_id_db, product_id, quantity, product.price, cursor)
                    if not order_item_id:
                        raise Exception(f"Fallo al crear item de orden para producto {product_id}.")
                    
                    # Actualizar stock del producto
                    # Asume que Product.update_stock(product_id, -quantity, cursor) existe
                    Product.update_stock(product_id, -quantity, cursor) 
                    current_app.logger.info(f"Stock de producto {product_id} reducido en {quantity}.")

                # 5. Confirmar la transacción
                mysql.connection.commit()
                current_app.logger.info(f"Orden de PayPal {order_id} y orden de DB {order_id_db} procesadas con éxito.")
                return jsonify({"msg": "Pago completado y orden procesada.", "paypal_data": capture_data, "order_id": order_id_db})

            except Exception as e:
                mysql.connection.rollback() # Revertir si algo falla
                current_app.logger.error(f"Error en la transacción de DB para la orden {order_id}: {e}")
                return jsonify({"msg": "Pago completado con PayPal, pero hubo un error al registrar la orden. Contacta soporte."}), 500
        else:
            current_app.logger.warning(f"PayPal Order {order_id} no CAPTURADA. Estado: {capture_data['status']}")
            return jsonify({"msg": f"El pago no se pudo completar. Estado: {capture_data['status']}"}), 400

    except requests.exceptions.RequestException as e:
        current_app.logger.error(f"Error al capturar la orden de PayPal: {e.response.text if e.response else e}")
        return jsonify({"msg": "Error al contactar con la API de PayPal para capturar la orden."}), 500