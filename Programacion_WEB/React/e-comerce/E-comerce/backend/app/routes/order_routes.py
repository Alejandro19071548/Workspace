from flask import Blueprint, request, jsonify
from app.models.cart import CartItem
from app.models.product import Product
from app.models.order import Order, OrderItem
from app.models.shipping import ShippingInfo
from app.db import db

order_bp = Blueprint('orders', __name__)

@order_bp.route('/checkout', methods=['POST'])
def checkout():
    data = request.get_json()
    user_id = data['user_id']
    shipping = data['shipping']

    # Obtener carrito
    items = CartItem.query.filter_by(user_id=user_id).all()
    if not items:
        return jsonify({'error': 'El carrito está vacío'}), 400

    # Calcular total
    total = 0
    for item in items:
        product = Product.query.get(item.product_id)
        total += float(product.precio) * item.cantidad
        if item.cantidad > product.stock:
            return jsonify({'error': f'Stock insuficiente para {product.nombre}'}), 400

    # Crear orden
    order = Order(user_id=user_id, total=total)
    db.session.add(order)
    db.session.flush()  # para obtener order.id antes del commit

    # Crear ítems de la orden
    for item in items:
        product = Product.query.get(item.product_id)
        order_item = OrderItem(
            order_id=order.id,
            product_id=product.id,
            cantidad=item.cantidad,
            precio_unitario=product.precio
        )
        db.session.add(order_item)
        product.stock -= item.cantidad  # reducir stock

    # Guardar info de envío
    shipping_info = ShippingInfo(
        order_id=order.id,
        direccion=shipping['direccion'],
        ciudad=shipping.get('ciudad'),
        estado=shipping.get('estado'),
        codigo_postal=shipping.get('codigo_postal'),
        pais=shipping.get('pais')
    )
    db.session.add(shipping_info)

    # Limpiar carrito
    for item in items:
        db.session.delete(item)

    db.session.commit()
    return jsonify({'message': 'Orden creada exitosamente', 'order_id': order.id})
