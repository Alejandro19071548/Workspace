# backend/app.py (solo las partes relevantes que necesitas añadir/modificar)
import os
from flask import Flask, jsonify
from flask_jwt_extended import JWTManager
from flask_cors import CORS
from db import mysql
from routes.auth import auth_bp
from routes.products import products_bp
from routes.account import account_bp

app = Flask(__name__)
CORS(app) # Habilita CORS

# Configuración de JWT
app.config["JWT_SECRET_KEY"] = "super-secret-key-replace-with-a-real-one" # ¡Cambia esto en producción!
app.config["JWT_ACCESS_TOKEN_EXPIRES"] = 3600 # Token expira en 1 hora (opcional)
jwt = JWTManager(app)

# Configuración de la base de datos
app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'Stprm123'
app.config['MYSQL_DB'] = 'ecommerce_db'
mysql.init_app(app)

# --- CONFIGURACIÓN PARA SUBIDA DE IMÁGENES ---
app.config['UPLOAD_FOLDER'] = 'uploads' # Esta carpeta debe existir al mismo nivel que app.py
app.config['ALLOWED_EXTENSIONS'] = {'png', 'jpg', 'jpeg', 'gif'}

if not os.path.exists(app.config['UPLOAD_FOLDER']):
    os.makedirs(app.config['UPLOAD_FOLDER'])
# ...
# Función para verificar la extensión del archiv

# --- FIN CONFIGURACIÓN PARA SUBIDA DE IMÁGENES ---

# Registro de Blueprints
app.register_blueprint(auth_bp, url_prefix='/api/auth')
app.register_blueprint(products_bp, url_prefix='/api')
app.register_blueprint(account_bp, url_prefix='/api/account')

# ... (otras rutas y configuraciones)

if __name__ == '__main__':
    app.run(debug=True)