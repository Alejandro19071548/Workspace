# backend/routes/orders.py
from flask import Blueprint, request, jsonify
from models.order import Order, OrderItem
from models.product import Product # Necesario para OrderItem
from models.user import User # Necesario para la verificación de administrador
from db import mysql
from flask_jwt_extended import jwt_required, get_jwt_identity

orders_bp = Blueprint('orders', __name__)

# Helper para verificar si el usuario es administrador (reutilizar si ya lo tienes)
def admin_required(fn):
    def wrapper(*args, **kwargs):
        try:
            current_user_id = get_jwt_identity()
            user = User.find_by_id(current_user_id)
            if not user or not user.is_admin:
                return jsonify({"msg": "Acceso denegado: Se requiere rol de administrador"}), 403
            return fn(*args, **kwargs)
        except Exception as e:
            # En caso de que get_jwt_identity() falle o el token sea inválido
            return jsonify({"msg": "Error de autenticación: Token inválido o expirado"}), 401
    wrapper.__name__ = fn.__name__ # Necesario para Flask al usar decoradores
    return wrapper

# NOTA IMPORTANTE:
# La ruta para CREAR una orden (POST /orders) ha sido ELIMINADA de este archivo.
# La creación de órdenes, la actualización de stock y el registro de OrderItems
# ahora se manejan exclusivamente en el endpoint POST /api/checkout/capture-paypal-order
# dentro de 'backend/routes/checkout.py', asegurando que la orden solo se registra
# después de una confirmación de pago exitosa de PayPal.

@orders_bp.route('/orders/<int:order_id>', methods=['GET'])
@jwt_required()
def get_order_details(order_id):
    current_user_id = get_jwt_identity()
    order = Order.find_by_id(order_id)
    if not order or order.user_id != current_user_id:
        return jsonify({"msg": "Orden no encontrada o no tienes permiso"}), 404
    
    # También puedes querer obtener los ítems de la orden aquí
    order_details = order.to_dict()
    order_items = OrderItem.find_by_order_id(order_id) # Asume que este método existe
    order_details['items'] = order_items # Añadir los ítems a la respuesta

    return jsonify(order_details)

@orders_bp.route('/orders', methods=['GET'])
@jwt_required()
def get_user_orders():
    current_user_id = get_jwt_identity()
    orders = Order.find_by_user_id(current_user_id) # Asume que este método existe
    
    # Opcional: Cargar los ítems para cada orden
    orders_with_items = []
    for order in orders:
        order_dict = order.to_dict()
        order_dict['items'] = OrderItem.find_by_order_id(order.id)
        orders_with_items.append(order_dict)

    return jsonify([o.to_dict() for o in orders]) # Devolver solo la orden si no necesitas ítems, o orders_with_items si sí

@orders_bp.route('/admin/orders', methods=['GET'])
@jwt_required()
@admin_required
def get_all_orders_admin():
    orders = Order.get_all() # Asume que este método existe y trae todas las órdenes
    
    # Opcional: Cargar los ítems para cada orden para la vista de administrador
    orders_with_items = []
    for order in orders:
        order_dict = order.to_dict()
        order_dict['items'] = OrderItem.find_by_order_id(order.id)
        orders_with_items.append(order_dict)

    return jsonify([o.to_dict() for o in orders]) # Devolver solo la orden, o orders_with_items