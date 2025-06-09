from flask import Blueprint, request, jsonify, current_app, send_from_directory
from models.product import Product
from models.user import User
from db import mysql 
from flask_jwt_extended import jwt_required, get_jwt_identity
import os 
from werkzeug.utils import secure_filename 
import uuid 

products_bp = Blueprint('products', __name__)

# Helper para verificar si el usuario es administrador
def admin_required(fn):
    def wrapper(*args, **kwargs):
        try:
            current_user_id = get_jwt_identity()
            user = User.find_by_id(current_user_id)
            if not user or not user.is_admin:
                return jsonify({"msg": "Acceso denegado: Se requiere rol de administrador"}), 403
            return fn(*args, **kwargs)
        except Exception as e:
            current_app.logger.error(f"Error in admin_required: {e}")
            return jsonify({"msg": "Error de autenticación/autorización"}), 401
    wrapper.__name__ = fn.__name__ 
    return wrapper

# Función helper para verificar extensiones de archivo
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in current_app.config['ALLOWED_EXTENSIONS']

# RUTA PARA OBTENER TODOS LOS PRODUCTOS (PARA USUARIOS FINALES - SOLO ACTIVOS)
@products_bp.route('/products', methods=['GET'])
def get_all_products():
    products = Product.get_all() # Llama a get_all que solo trae activos
    return jsonify([p.to_dict() for p in products])

# RUTA PARA OBTENER TODOS LOS PRODUCTOS (PARA EL ADMIN DASHBOARD - TODOS)
@products_bp.route('/admin/products', methods=['GET'])
@jwt_required()
@admin_required
def get_all_admin_products():
    products = Product.get_all_for_admin()
    return jsonify([p.to_dict() for p in products])

@products_bp.route('/products/<int:product_id>', methods=['GET'])
def get_product_by_id(product_id):
    product = Product.find_by_id(product_id)
    if not product:
        return jsonify({"msg": "Producto no encontrado"}), 404

    is_admin_request = False
    try:
        current_user_id = get_jwt_identity() 
        if current_user_id:
            user = User.find_by_id(current_user_id)
            if user and user.is_admin:
                is_admin_request = True
    except Exception:
        is_admin_request = False

    if not product.activo and not is_admin_request:
        return jsonify({"msg": "Producto no disponible"}), 404
    return jsonify(product.to_dict())

# backend/routes/products.py
# ... (imports y otras funciones) ...

@products_bp.route('/products', methods=['POST'])
@jwt_required()
@admin_required
def create_product():
    # DEBUGGING: Imprime el contenido de la request para verificar lo que llega
    current_app.logger.debug(f"Request Form: {request.form}")
    current_app.logger.debug(f"Request Files: {request.files}")

    name = request.form.get('name')
    description = request.form.get('description')
    price_str = request.form.get('price') 
    stock_str = request.form.get('stock') 
    
    activo_str = request.form.get('activo', 'true')
    activo = activo_str.lower() == 'true'

    image_url = None 

    file = request.files.get('image') # Obtener el objeto de archivo

    # CAMBIO IMPORTANTE AQUÍ: Verifica si el archivo existe Y tiene un nombre de archivo no vacío
    if file and file.filename != '' and allowed_file(file.filename): # <--- LÍNEA MODIFICADA
        filename = secure_filename(file.filename)
        unique_filename = str(uuid.uuid4()) + os.path.splitext(filename)[1]
        file_path = os.path.join(current_app.config['UPLOAD_FOLDER'], unique_filename)
        try:
            file.save(file_path)
            image_url = f"/uploads/{unique_filename}"
        except Exception as e:
            current_app.logger.error(f"Error saving image file: {e}")
            return jsonify({"msg": f"Error al guardar la imagen: {e}"}), 500
    elif file and file.filename != '' and not allowed_file(file.filename): # Solo si se proporcionó un archivo pero no está permitido
        return jsonify({"msg": "Tipo de archivo de imagen no permitido"}), 400
    # SINO: Si no se proporcionó ningún archivo o file.filename está vacío, image_url permanece None. Esto es lo deseado.

    if not all([name, price_str, stock_str]): 
        return jsonify({"msg": "Faltan datos requeridos: name, price, stock"}), 400
    
    try:
        price = float(price_str)
        stock = int(stock_str)
    except (ValueError, TypeError):
        return jsonify({"msg": "Precio y Stock deben ser números válidos"}), 400
    
    product_id = Product.create(name, description, price, stock, image_url, activo) # Asegúrate de pasar 'activo' aquí
    if product_id:
        new_product = Product.find_by_id(product_id)
        if new_product:
            return jsonify(new_product.to_dict()), 201
        else:
            return jsonify({"msg": "Producto creado pero no se pudo recuperar para retorno"}), 500
    return jsonify({"msg": "Error al crear el producto"}), 500

# ... (resto de tu archivo products.py)
@products_bp.route('/products/<int:product_id>', methods=['PUT'])
@jwt_required()
@admin_required
def update_product(product_id):
    # DEBUGGING: Imprime el contenido de la request para verificar lo que llega
    current_app.logger.debug(f"Request Form for Update: {request.form}")
    current_app.logger.debug(f"Request Files for Update: {request.files}")

    product = Product.find_by_id(product_id)
    if not product:
        return jsonify({"msg": "Producto no encontrado"}), 404

    # Usa los valores existentes como fallback si no se proporcionan en la request
    name = request.form.get('name', product.name)
    description = request.form.get('description', product.description)
    price_str = request.form.get('price', str(product.price))
    stock_str = request.form.get('stock', str(product.stock))
    
    activo_str = request.form.get('activo')
    activo = product.activo # Valor por defecto si no se proporciona
    if activo_str is not None:
        activo = activo_str.lower() == 'true'

    image_url = product.image_url # Mantener la imagen actual por defecto

    file = request.files.get('image') # Obtener el archivo de la request
    if file and allowed_file(file.filename):
        # Eliminar la imagen anterior si existe y no es la predeterminada/genérica
        if product.image_url and product.image_url.startswith('/uploads/'):
            old_image_path = os.path.join(current_app.config['UPLOAD_FOLDER'], os.path.basename(product.image_url))
            if os.path.exists(old_image_path):
                try:
                    os.remove(old_image_path)
                except OSError as e:
                    current_app.logger.warning(f"Could not delete old image file {old_image_path}: {e}")

        filename = secure_filename(file.filename)
        unique_filename = str(uuid.uuid4()) + os.path.splitext(filename)[1]
        file_path = os.path.join(current_app.config['UPLOAD_FOLDER'], unique_filename)
        try:
            file.save(file_path)
            image_url = f"/uploads/{unique_filename}"
        except Exception as e:
            current_app.logger.error(f"Error saving new image file during update: {e}")
            return jsonify({"msg": f"Error al guardar la nueva imagen: {e}"}), 500
    elif file and not allowed_file(file.filename): # Si el archivo no es permitido
        return jsonify({"msg": "Tipo de archivo de imagen no permitido"}), 400
    # Si no se sube un nuevo archivo y image_cleared se envía como 'true', borrar la imagen existente
    elif request.form.get('image_cleared') == 'true':
        if product.image_url and product.image_url.startswith('/uploads/'):
            old_image_path = os.path.join(current_app.config['UPLOAD_FOLDER'], os.path.basename(product.image_url))
            if os.path.exists(old_image_path):
                try:
                    os.remove(old_image_path)
                except OSError as e:
                    current_app.logger.warning(f"Could not delete old image file {old_image_path}: {e}")
        image_url = None # Establecer la URL de la imagen a NULL en la DB


    # Asegurarse de que price y stock sean del tipo correcto
    try:
        price = float(price_str)
        stock = int(stock_str)
    except (ValueError, TypeError):
        return jsonify({"msg": "Precio y Stock deben ser números válidos"}), 400

    if product.update(name, description, price, stock, image_url, activo): 
        updated_product = Product.find_by_id(product_id)
        return jsonify(updated_product.to_dict())
    return jsonify({"msg": "Error al actualizar el producto"}), 500

# Ruta para desactivar un producto (soft delete)
@products_bp.route('/products/<int:product_id>/deactivate', methods=['PUT'])
@jwt_required()
@admin_required
def deactivate_product(product_id):
    product = Product.find_by_id(product_id)
    if not product:
        return jsonify({"msg": "Producto no encontrado"}), 404

    # Aquí se podría llamar directamente a update con activo=False
    if product.update(product.name, product.description, product.price, product.stock, product.image_url, False):
        return jsonify({"msg": "Producto desactivado con éxito", "id": product_id}), 200
    return jsonify({"msg": "Error al desactivar el producto"}), 500

# Opcional: Ruta para reactivar un producto
@products_bp.route('/products/<int:product_id>/activate', methods=['PUT'])
@jwt_required()
@admin_required
def activate_product(product_id):
    product = Product.find_by_id(product_id)
    if not product:
        return jsonify({"msg": "Producto no encontrado"}), 404

    if product.update(product.name, product.description, product.price, product.stock, product.image_url, True):
        return jsonify({"msg": "Producto activado con éxito", "id": product_id}), 200
    return jsonify({"msg": "Error al activar el producto"}), 500

# Sirve archivos estáticos desde la carpeta 'uploads'
@products_bp.route('/uploads/<filename>')
def uploaded_file(filename):
    # Asegúrate de que UPLOAD_FOLDER esté configurado en app.py y que la ruta sea correcta.
    return send_from_directory(os.path.join(current_app.root_path, current_app.config['UPLOAD_FOLDER']), filename)

    @staticmethod
    def update_stock(product_id, quantity_change, cursor=None):
        # quantity_change puede ser negativo para reducir stock (e.g., -5)
        # o positivo para añadir stock (e.g., 10)
        if cursor is None:
            cur = mysql.connection.cursor()
            external_cursor = True
        else:
            cur = cursor
            external_cursor = False
            
        try:
            cur.execute(
                "UPDATE products SET stock = stock + %s WHERE id = %s",
                (quantity_change, product_id)
            )
            if not external_cursor:
                mysql.connection.commit()
            return True
        except Exception as e:
            if not external_cursor:
                mysql.connection.rollback()
            print(f"Error updating stock for product {product_id}: {e}")
            return False
        finally:
            if not external_cursor:
                cur.close()

    def to_dict(self):
        return {
            'id': self.id,
            'name': self.name,
            'description': self.description,
            'price': float(self.price), # Asegura que sea un float
            'stock': self.stock,
            'image_url': self.image_url,
            'activo': bool(self.activo), # Asegura que sea un booleano
            'created_at': self.created_at.isoformat() if self.created_at else None,
            'updated_at': self.updated_at.isoformat() if self.updated_at else None
        }