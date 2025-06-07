from flask import Blueprint, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from app.models.order import Order, OrderItem
from app.models.product import Product

order_history_bp = Blueprint('order_history', __name__)

@order_history_bp.route('/my-orders', methods=['GET'])
@jwt_required()
def get_my_orders():
    user_id = get_jwt_identity()
    orders = Order.query.filter_by(user_id=user_id).order_by(Order.fecha.desc()).all()
    result = []

    for order in orders:
        items = OrderItem.query.filter_by(order_id=order.id).all()
        item_list = []
        for item in items:
            product = Product.query.get(item.product_id)
            item_list.append({
                'nombre': product.nombre,
                'cantidad': item.cantidad,
                'precio_unitario': str(item.precio_unitario),
                'subtotal': str(item.cantidad * item.precio_unitario)
            })
        result.append({
            'order_id': order.id,
            'fecha': order.fecha.strftime('%Y-%m-%d %H:%M'),
            'total': str(order.total),
            'estado': order.estado,
            'productos': item_list
        })

    return jsonify(result)
