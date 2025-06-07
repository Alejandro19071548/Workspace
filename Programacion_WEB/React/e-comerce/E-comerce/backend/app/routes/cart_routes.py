from flask import Blueprint, request, jsonify
from app.models.cart import CartItem
from app.models.product import Product
from app.db import db

cart_bp = Blueprint('cart', __name__)

# Obtener carrito de un usuario
@cart_bp.route('/cart/<int:user_id>', methods=['GET'])
def get_cart(user_id):
    items = CartItem.query.filter_by(user_id=user_id).all()
    result = []
    for item in items:
        product = Product.query.get(item.product_id)
        result.append({
            'cart_item_id': item.id,
            'product_id': product.id,
            'nombre': product.nombre,
            'precio_unitario': str(product.precio),
            'cantidad': item.cantidad,
            'subtotal': str(product.precio * item.cantidad)
        })
    return jsonify(result)

# Agregar producto al carrito
@cart_bp.route('/cart/add', methods=['POST'])
def add_to_cart():
    data = request.get_json()
    user_id = data['user_id']
    product_id = data['product_id']
    cantidad = data.get('cantidad', 1)

    existing = CartItem.query.filter_by(user_id=user_id, product_id=product_id).first()
    if existing:
        existing.cantidad += cantidad
    else:
        item = CartItem(user_id=user_id, product_id=product_id, cantidad=cantidad)
        db.session.add(item)

    db.session.commit()
    return jsonify({'message': 'Producto agregado al carrito'})

# Eliminar producto del carrito
@cart_bp.route('/cart/remove', methods=['POST'])
def remove_from_cart():
    data = request.get_json()
    item = CartItem.query.get(data['cart_item_id'])

    if not item:
        return jsonify({'error': 'Elemento no encontrado'}), 404

    db.session.delete(item)
    db.session.commit()
    return jsonify({'message': 'Producto eliminado del carrito'})
