from flask import Blueprint, request, jsonify
from db import mysql

auth_bp = Blueprint("auth", __name__)
account_bp = Blueprint("account", __name__)

@auth_bp.route('/register', methods=['POST'])
def register():
    data = request.json
    username = data.get('username')
    nombre = data.get('name')
    apellido = data.get('apellidos')
    correo = data.get('email')
    contraseña = data.get('password')

    if not all([username, nombre, apellido, correo, contraseña]):
        return jsonify({"error": "Todos los campos son obligatorios"}), 400

    cur = mysql.connection.cursor()
    cur.execute("""
        INSERT INTO users (username, nombre, apellido, correo, contraseña, activo)
        VALUES (%s, %s, %s, %s, %s, 1)
    """, (username, nombre, apellido, correo, contraseña))
    mysql.connection.commit()
    cur.close()

    return jsonify({"message": "Usuario registrado exitosamente"}), 201


@auth_bp.route("/login", methods=["POST"])
def login():
    data = request.json
    email = data.get("email")
    password = data.get("password")

    cur = mysql.connection.cursor()
    cur.execute("SELECT * FROM users WHERE correo=%s AND contraseña=%s AND activo=TRUE", (email, password))
    user = cur.fetchone()
    cur.close()

    if user:
        return jsonify({
            "message": "Inicio de sesión exitoso",
            "user": {
                "id": user[0],         # ID
                "username": user[1],   # username
                "correo": user[4]      # correo
            }
        })
    else:
        return jsonify({"message": "Credenciales inválidas"}), 401


@account_bp.route("/<int:id>", methods=["GET"])
def get_user(id):
    cur = mysql.connection.cursor()
    cur.execute("SELECT username, nombre, apellido, correo FROM users WHERE id = %s AND activo = TRUE", (id,))
    user = cur.fetchone()
    cur.close()

    if user:
        return jsonify({
            "username": user[0],
            "nombre": user[1],
            "apellido": user[2],
            "correo": user[3]
        })
    else:
        return jsonify({"error": "Usuario no encontrado"}), 404


@account_bp.route("/update", methods=["PUT"])
def update_user():
    data = request.json
    user_id = data.get("id")

    fields = ["username", "nombre", "apellido", "correo", "contraseña"]
    updates = [f"{field} = %s" for field in fields if data.get(field) is not None]
    values = [data[field] for field in fields if data.get(field) is not None]

    if not updates:
        return jsonify({"message": "No hay cambios para actualizar."}), 400

    values.append(user_id)
    query = f"UPDATE users SET {', '.join(updates)} WHERE id = %s AND activo = TRUE"

    cur = mysql.connection.cursor()
    cur.execute(query, values)
    mysql.connection.commit()
    cur.close()

    return jsonify({"message": "Datos actualizados correctamente"})


@account_bp.route("/delete", methods=["PUT"])
def deactivate_user():
    data = request.json
    user_id = data.get("id")

    cur = mysql.connection.cursor()
    cur.execute("UPDATE users SET activo = FALSE WHERE id = %s", (user_id,))
    mysql.connection.commit()
    cur.close()

    return jsonify({"message": "Cuenta desactivada correctamente"})
