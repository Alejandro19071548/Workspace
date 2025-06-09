# routes/auth.py
from flask import Blueprint, request, jsonify
from db import mysql
from models.user import User # Importa el modelo de usuario

# Como app.py YA ESTÁ INICIALIZANDO JWTManager, necesitamos importar create_access_token aquí.
from flask_jwt_extended import create_access_token # <--- DESCOMENTA/MANTÉN ESTA LÍNEA

auth_bp = Blueprint("auth", __name__)


@auth_bp.route('/register', methods=['POST'])
def register():
    data = request.json
    username = data.get('username')
    # nombre = data.get('name') # <--- ELIMINA ESTAS LÍNEAS
    # apellido = data.get('apellidos') # <--- ELIMINA ESTAS LÍNEAS
    correo = data.get('email') # En el frontend, este campo debería llamarse 'email'
    contraseña = data.get('password')

    # Ajusta la validación para que no espere nombre ni apellido
    if not all([username, correo, contraseña]):
        return jsonify({"error": "Todos los campos (username, email, password) son obligatorios"}), 400

    if User.find_by_username(username):
        return jsonify({"message": "El nombre de usuario ya existe"}), 409
    # Asegúrate de que User.find_by_email esté corregido en models/user.py
    if User.find_by_email(correo): 
        return jsonify({"message": "El correo electrónico ya está registrado"}), 409

    # Llama a User.create con los parámetros CORRECTOS (sin nombre/apellido)
    new_user = User.create(username, correo, contraseña, is_admin=False)

    if new_user:
        return jsonify({"message": "Usuario registrado exitosamente"}), 201
    else:
        return jsonify({"message": "Error al registrar usuario"}), 500


@auth_bp.route("/login", methods=["POST"])
def login():
    data = request.json
    # CAMBIADO: Ahora espera 'username' (como lo envía el frontend Login.jsx)
    username = data.get("username") # <--- CAMBIADO
    password = data.get("password")

    if not all([username, password]):
        return jsonify({"message": "Se requieren usuario y contraseña"}), 400

    # CAMBIADO: Busca por nombre de usuario en lugar de email
    user = User.find_by_username(username) # <--- CAMBIADO: Usa find_by_username

    # Verificar si el usuario existe, está activo y la contraseña es correcta
    if user and user.activo and user.check_password(password):
        # Como JWTManager está activo en app.py, debemos crear el token aquí
        access_token = create_access_token(identity=user.id) # <--- MANTÉN ESTA LÍNEA

        return jsonify({
            "message": "Inicio de sesión exitoso",
            "access_token": access_token, # <--- MANTÉN ESTA LÍNEA
            "user_data": user.to_dict() # Envía los datos del usuario, incluyendo is_admin
        })
    else:
        return jsonify({"message": "Credenciales inválidas o cuenta inactiva"}), 401