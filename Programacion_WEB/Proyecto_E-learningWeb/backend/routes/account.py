from flask import Blueprint, request, jsonify
from db import mysql

account_bp = Blueprint("account", __name__)

@account_bp.route("/<int:user_id>", methods=["GET"])
def get_account(user_id):
    cur = mysql.connection.cursor()
    cur.execute("SELECT id, username, nombre, apellido, correo FROM users WHERE id = %s AND activo = TRUE", (user_id,))
    user = cur.fetchone()
    cur.close()

    if user:
        keys = ["id", "username", "nombre", "apellido", "correo"]
        return jsonify(dict(zip(keys, user)))
    else:
        return jsonify({"message": "Usuario no encontrado o inactivo"}), 404

@account_bp.route("/<int:user_id>", methods=["PUT"])
def update_account(user_id):
    data = request.json
    username = data.get("username")
    nombre = data.get("nombre")
    apellido = data.get("apellido")
    correo = data.get("correo")
    nueva_contraseña = data.get("contraseña")  # puede ser None

    cur = mysql.connection.cursor()

    # Obtener contraseña actual
    cur.execute("SELECT contraseña FROM users WHERE id = %s AND activo = TRUE", (user_id,))
    resultado = cur.fetchone()
    if not resultado:
        cur.close()
        return jsonify({"message": "Usuario no encontrado o inactivo"}), 404

    contraseña_actual = resultado[0]

    # Validar nueva contraseña
    if nueva_contraseña:
        if nueva_contraseña == contraseña_actual:
            cur.close()
            return jsonify({"message": "La nueva contraseña debe ser diferente a la actual"}), 400
        if len(nueva_contraseña) < 6:
            cur.close()
            return jsonify({"message": "La contraseña debe tener al menos 6 caracteres"}), 400
    else:
        nueva_contraseña = contraseña_actual  # se mantiene la actual

    # Actualizar datos
    cur.execute("""
        UPDATE users 
        SET username = %s, nombre = %s, apellido = %s, correo = %s, contraseña = %s 
        WHERE id = %s AND activo = TRUE
    """, (username, nombre, apellido, correo, nueva_contraseña, user_id))
    mysql.connection.commit()
    cur.close()

    return jsonify({"message": "Datos actualizados correctamente"})


@account_bp.route("/<int:user_id>", methods=["DELETE"])
def delete_account(user_id):
    cur = mysql.connection.cursor()
    cur.execute("UPDATE users SET activo = FALSE WHERE id = %s", (user_id,))
    mysql.connection.commit()
    cur.close()

    return jsonify({"message": "Cuenta desactivada correctamente"})
