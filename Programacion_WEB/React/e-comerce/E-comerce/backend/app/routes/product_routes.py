from flask import Blueprint, request, jsonify
from app.models.product import Product
from app.db import db

product_bp = Blueprint('products', __name__)

@product_bp.route('/products', methods=['GET'])
def get_products():
    products = Product.query.all()
    return jsonify([
        {
            'id': p.id,
            'nombre': p.nombre,
            'descripcion': p.descripcion,
            'precio': str(p.precio),
            'imagen_url': p.imagen_url,
            'stock': p.stock
        } for p in products
    ])

@product_bp.route('/products/<int:product_id>', methods=['GET'])
def get_product(product_id):
    p = Product.query.get_or_404(product_id)
    return jsonify({
        'id': p.id,
        'nombre': p.nombre,
        'descripcion': p.descripcion,
        'precio': str(p.precio),
        'imagen_url': p.imagen_url,
        'stock': p.stock
    })

@product_bp.route('/products', methods=['POST'])
def create_product():
    data = request.get_json()
    new_product = Product(
        nombre=data['nombre'],
        descripcion=data.get('descripcion'),
        precio=data['precio'],
        imagen_url=data.get('imagen_url'),
        stock=data.get('stock', 0)
    )
    db.session.add(new_product)
    db.session.commit()
    return jsonify({'message': 'Producto creado exitosamente'}), 201
