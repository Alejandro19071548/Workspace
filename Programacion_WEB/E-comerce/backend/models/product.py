# backend/models/product.py
from db import mysql
from datetime import datetime 

class Product:
    # CONSTRUCTOR: Asegúrate de que tenga todos los campos de la tabla, incluyendo los TIMESTAMP
    def __init__(self, id, name, description, price, stock, image_url, activo, created_at=None, updated_at=None):
        self.id = id
        self.name = name
        self.description = description
        self.price = price
        self.stock = stock
        self.image_url = image_url
        self.activo = activo
        self.created_at = created_at
        self.updated_at = updated_at

    @staticmethod
    def create(name, description, price, stock, image_url):
        cur = mysql.connection.cursor()
        try:
            # created_at y updated_at se gestionan por la base de datos con DEFAULT CURRENT_TIMESTAMP
            # Asumiendo que tu columna 'activo' tiene un valor por defecto de TRUE o lo manejas así.
            cur.execute(
                "INSERT INTO products (name, description, price, stock, image_url, activo) VALUES (%s, %s, %s, %s, %s, TRUE)",
                (name, description, price, stock, image_url)
            )
            mysql.connection.commit()
            product_id = cur.lastrowid # Obtener el ID del producto recién insertado
            return product_id
        except Exception as e:
            mysql.connection.rollback()
            print(f"Error creating product: {e}")
            return None
        finally:
            cur.close()

    @staticmethod
    def get_all():
        cur = mysql.connection.cursor()
        # Solo productos activos para la vista de usuario final
        cur.execute("SELECT id, name, description, price, stock, image_url, activo, created_at, updated_at FROM products WHERE activo = TRUE")
        products_data = cur.fetchall()
        cur.close()
        return [Product(*data) for data in products_data]

    @staticmethod
    def get_all_for_admin():
        cur = mysql.connection.cursor()
        # Todos los productos para el admin
        cur.execute("SELECT id, name, description, price, stock, image_url, activo, created_at, updated_at FROM products") 
        products_data = cur.fetchall()
        cur.close()
        return [Product(*data) for data in products_data]

    @staticmethod
    def find_by_id(product_id):
        cur = mysql.connection.cursor()
        cur.execute("SELECT id, name, description, price, stock, image_url, activo, created_at, updated_at FROM products WHERE id = %s", (product_id,))
        product_data = cur.fetchone()
        cur.close()
        if product_data:
            return Product(*product_data)
        return None

    @staticmethod
    def find_by_name(name):
        cur = mysql.connection.cursor()
        cur.execute("SELECT id, name, description, price, stock, image_url, activo, created_at, updated_at FROM products WHERE name = %s", (name,))
        product_data = cur.fetchone()
        cur.close()
        if product_data:
            return Product(*product_data)
        return None
    
    def update(self, name, description, price, stock, image_url, activo):
        cur = mysql.connection.cursor()
        try:
            # updated_at se gestiona por la base de datos con ON UPDATE CURRENT_TIMESTAMP
            cur.execute(
                "UPDATE products SET name = %s, description = %s, price = %s, stock = %s, image_url = %s, activo = %s WHERE id = %s",
                (name, description, price, stock, image_url, activo, self.id)
            )
            mysql.connection.commit()
            return True
        except Exception as e:
            mysql.connection.rollback()
            print(f"Error updating product {self.id}: {e}")
            return False
        finally:
            cur.close()

    def soft_delete(self):
        cur = mysql.connection.cursor()
        try:
            cur.execute("UPDATE products SET activo = FALSE WHERE id = %s", (self.id,))
            mysql.connection.commit()
            return True
        except Exception as e:
            mysql.connection.rollback()
            print(f"Error deactivating product {self.id}: {e}")
            return False
        finally:
            cur.close()

    def to_dict(self):
        # Serializa las fechas a string en formato ISO para que el frontend las maneje fácilmente
        created_at_str = self.created_at.isoformat() if self.created_at else None
        updated_at_str = self.updated_at.isoformat() if self.updated_at else None

        return {
            "id": self.id,
            "name": self.name,
            "description": self.description,
            "price": float(self.price), # Asegura que el precio sea flotante
            "stock": int(self.stock),   # Asegura que el stock sea entero
            "image_url": self.image_url,
            "activo": bool(self.activo), # Asegura que sea True/False en el frontend
            "created_at": created_at_str,
            "updated_at": updated_at_str
        }