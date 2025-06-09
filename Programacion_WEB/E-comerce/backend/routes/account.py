# routes/account.py
from flask import Blueprint, request, jsonify
from db import mysql
from models.user import User # Importa el modelo de usuario
from flask_jwt_extended import jwt_required, get_jwt_identity # Importa para proteger rutas

account_bp = Blueprint("account", __name__)

@account_bp.route("/profile", methods=["GET"]) # Ruta para obtener el perfil del usuario logueado
@jwt_required() # Protege la ruta con JWT
def get_user_profile():
    current_user_id = get_jwt_identity() # Obtiene la identidad (ID del usuario) del token
    user = User.find_by_id(current_user_id)

    if user and user.activo:
        return jsonify(user.to_dict()) # Retorna todos los datos del usuario, incluyendo is_admin
    else:
        return jsonify({"message": "Usuario no encontrado o inactivo"}), 404


@account_bp.route("/update", methods=["PUT"]) # Ruta para actualizar el perfil del usuario logueado
@jwt_required()
def update_user_profile():
    current_user_id = get_jwt_identity()
    data = request.json

    user = User.find_by_id(current_user_id)
    if not user or not user.activo:
        return jsonify({"message": "Usuario no encontrado o inactivo"}), 404

    # Campos que pueden ser actualizados
    username = data.get("username", user.username)
    nombre = data.get("name", user.nombre) # Usa 'name' como en tu frontend de registro
    apellido = data.get("apellidos", user.apellido) # Usa 'apellidos'
    correo = data.get("email", user.email) # Usa 'email'
    nueva_contraseña = data.get("password") # Usa 'password'

    # Preparar la consulta UPDATE dinámicamente
    update_parts = []
    values = []

    if username != user.username:
        update_parts.append("username = %s")
        values.append(username)
    if nombre != user.nombre:
        update_parts.append("nombre = %s")
        values.append(nombre)
    if apellido != user.apellido:
        update_parts.append("apellido = %s")
        values.append(apellido)
    if correo != user.email:
        # Aquí podrías añadir una validación para que el nuevo correo no exista ya
        update_parts.append("correo = %s")
        values.append(correo)

    # Manejo de la contraseña
    if nueva_contraseña and not user.check_password(nueva_contraseña): # Solo actualizar si es diferente a la actual
        if len(nueva_contraseña) < 6:
            return jsonify({"message": "La nueva contraseña debe tener al menos 6 caracteres"}), 400
        user.update_password(nueva_contraseña) # Usa el método del modelo para hashear y actualizar
    elif nueva_contraseña and user.check_password(nueva_contraseña):
        return jsonify({"message": "La nueva contraseña debe ser diferente a la actual"}), 400

    if not update_parts and not nueva_contraseña: # Si no hay cambios en los campos y no se cambió la contraseña
        return jsonify({"message": "No hay cambios para actualizar."}), 400

    if update_parts: # Solo ejecuta el UPDATE si hay campos para actualizar además de la contraseña
        query = f"UPDATE users SET {', '.join(update_parts)} WHERE id = %s"
        values.append(current_user_id)

        cur = mysql.connection.cursor()
        try:
            cur.execute(query, tuple(values))
            mysql.connection.commit()
        except Exception as e:
            mysql.connection.rollback()
            print(f"Error al actualizar datos del usuario: {e}")
            return jsonify({"message": "Error al actualizar datos."}), 500
        finally:
            cur.close()

    # Obtener los datos actualizados del usuario para enviarlos de vuelta
    updated_user = User.find_by_id(current_user_id)
    return jsonify({
        "message": "Datos actualizados correctamente",
        "user_data": updated_user.to_dict()
    })


@account_bp.route("/deactivate", methods=["PUT"]) # Ruta para desactivar la cuenta del usuario logueado
@jwt_required()
def deactivate_account():
    current_user_id = get_jwt_identity()

    cur = mysql.connection.cursor()
    try:
        cur.execute("UPDATE users SET activo = FALSE WHERE id = %s", (current_user_id,))
        mysql.connection.commit()
    except Exception as e:
        mysql.connection.rollback()
        print(f"Error al desactivar cuenta: {e}")
        return jsonify({"message": "Error al desactivar cuenta."}), 500
    finally:
        cur.close()

    return jsonify({"message": "Cuenta desactivada correctamente"})

# La ruta DELETE /<int:user_id> ya no es necesaria con el esquema de rutas protegidas
# La ruta GET /<int:user_id> se reemplaza por /profile