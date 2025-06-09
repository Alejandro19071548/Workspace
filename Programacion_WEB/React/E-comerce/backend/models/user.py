# backend/models/user.py
from db import mysql
# Ya NO necesitamos importar werkzeug.security si no estás hasheando contraseñas
# from werkzeug.security import generate_password_hash, check_password_hash

class User:
    # CONSTRUCTOR: Asegúrate de que tenga todos los campos de la tabla 'users'
    def __init__(self, id, username, email, password_hash, is_admin, created_at, activo):
        self.id = id
        self.username = username
        self.email = email
        self.password_hash = password_hash # Esta variable almacenará la contraseña en texto plano (NO RECOMENDADO PARA PRODUCCIÓN)
        self.is_admin = is_admin
        self.created_at = created_at
        self.activo = activo

    @staticmethod
    def create(username, email, password, is_admin=False):
        plain_password = password # Guarda la contraseña en texto plano
        
        cur = mysql.connection.cursor()
        try:
            # La columna `created_at` debería tener un DEFAULT CURRENT_TIMESTAMP en tu DB
            cur.execute(
                "INSERT INTO users (username, email, password_hash, is_admin, activo) VALUES (%s, %s, %s, %s, TRUE)",
                (username, email, plain_password, is_admin)
            )
            mysql.connection.commit()
            
            # Recupera el usuario recién creado con todos sus datos, incluyendo created_at
            cur.execute("SELECT id, username, email, password_hash, is_admin, created_at, activo FROM users WHERE username = %s", (username,))
            user_data = cur.fetchone()
            if user_data:
                return User(*user_data)
            return None
        except Exception as e:
            mysql.connection.rollback()
            print(f"Error creating user: {e}")
            return None
        finally:
            cur.close()

    @staticmethod
    def find_by_username(username):
        cur = mysql.connection.cursor()
        # Asegúrate de que las columnas en SELECT coincidan con el constructor de User
        cur.execute("SELECT id, username, email, password_hash, is_admin, created_at, activo FROM users WHERE username = %s AND activo = TRUE", (username,))
        user_data = cur.fetchone()
        cur.close()
        if user_data:
            return User(*user_data)
        return None

    @staticmethod
    def find_by_email(email):
        cur = mysql.connection.cursor()
        # Asegúrate de que las columnas en SELECT coincidan con el constructor de User
        cur.execute("SELECT id, username, email, password_hash, is_admin, created_at, activo FROM users WHERE email = %s AND activo = TRUE", (email,))
        user_data = cur.fetchone()
        cur.close()
        if user_data:
            return User(*user_data)
        return None

    @staticmethod
    def find_by_id(user_id):
        cur = mysql.connection.cursor()
        # Asegúrate de que las columnas en SELECT coincidan con el constructor de User
        cur.execute("SELECT id, username, email, password_hash, is_admin, created_at, activo FROM users WHERE id = %s", (user_id,))
        user_data = cur.fetchone()
        cur.close()
        if user_data:
            return User(*user_data)
        return None

    def check_password(self, password):
        # Esta comparación es para contraseñas en texto plano.
        # En producción, se recomienda usar hashing como bcrypt.
        return self.password_hash == password

    def update_password(self, new_password):
        plain_new_password = new_password # Contraseña en texto plano
        
        cur = mysql.connection.cursor()
        try:
            cur.execute("UPDATE users SET password_hash = %s WHERE id = %s", (plain_new_password, self.id))
            mysql.connection.commit()
            return True
        except Exception as e:
            mysql.connection.rollback()
            print(f"Error updating password: {e}")
            return False
        finally:
            cur.close()

    def to_dict(self):
        # Asegura que 'created_at' sea un string para serialización JSON
        created_at_str = str(self.created_at) if self.created_at else None 
        
        return {
            "id": self.id,
            "username": self.username,
            "email": self.email,
            "is_admin": bool(self.is_admin), # <-- CORREGIDO: Asegura que sea True/False en el frontend
            "activo": bool(self.activo), # Asegura que sea True/False en el frontend
            "created_at": created_at_str 
        }