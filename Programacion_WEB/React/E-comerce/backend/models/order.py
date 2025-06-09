# models/order.py
from db import mysql
from datetime import datetime

class OrderItem:
    def __init__(self, id, order_id, product_id, quantity, price):
        self.id = id
        self.order_id = order_id
        self.product_id = product_id
        self.quantity = quantity
        self.price = price

    @staticmethod
    def create(order_id, product_id, quantity, price):
        cur = mysql.connection.cursor()
        try:
            cur.execute(
                "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (%s, %s, %s, %s)",
                (order_id, product_id, quantity, price)
            )
            mysql.connection.commit()
            return cur.lastrowid # Retorna el ID del item de orden recién creado
        except Exception as e:
            mysql.connection.rollback()
            print(f"Error creating order item: {e}")
            return None
        finally:
            cur.close()

    @staticmethod
    def find_by_order_id(order_id):
        cur = mysql.connection.cursor()
        cur.execute("SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.price, p.name "
                    "FROM order_items oi JOIN products p ON oi.product_id = p.id "
                    "WHERE oi.order_id = %s", (order_id,))
        items_data = cur.fetchall()
        cur.close()
        return [
            {"id": item[0], "order_id": item[1], "product_id": item[2], "quantity": item[3], "price": float(item[4]), "product_name": item[5]}
            for item in items_data
        ]


class Order:
    def __init__(self, id, user_id, order_date, total_amount, status):
        self.id = id
        self.user_id = user_id
        self.order_date = order_date
        self.total_amount = total_amount
        self.status = status

    @staticmethod
    def create(user_id, total_amount, status='pending'):
        cur = mysql.connection.cursor()
        try:
            cur.execute(
                "INSERT INTO orders (user_id, total_amount, status) VALUES (%s, %s, %s)",
                (user_id, total_amount, status)
            )
            mysql.connection.commit()
            return cur.lastrowid # Retorna el ID de la orden recién creada
        except Exception as e:
            mysql.connection.rollback()
            print(f"Error creating order: {e}")
            return None
        finally:
            cur.close()

    @staticmethod
    def find_by_id(order_id):
        cur = mysql.connection.cursor()
        cur.execute("SELECT id, user_id, order_date, total_amount, status FROM orders WHERE id = %s", (order_id,))
        order_data = cur.fetchone()
        cur.close()
        if order_data:
            return Order(*order_data)
        return None

    @staticmethod
    def find_by_user_id(user_id):
        cur = mysql.connection.cursor()
        cur.execute("SELECT id, user_id, order_date, total_amount, status FROM orders WHERE user_id = %s", (user_id,))
        orders_data = cur.fetchall()
        cur.close()
        return [Order(*data) for data in orders_data]

    @staticmethod
    def get_all(): # Solo para administradores
        cur = mysql.connection.cursor()
        cur.execute("SELECT id, user_id, order_date, total_amount, status FROM orders")
        orders_data = cur.fetchall()
        cur.close()
        return [Order(*data) for data in orders_data]

    def to_dict(self):
        items = OrderItem.find_by_order_id(self.id)
        return {
            "id": self.id,
            "user_id": self.user_id,
            "order_date": self.order_date.isoformat(),
            "total_amount": float(self.total_amount),
            "status": self.status,
            "items": items
        }