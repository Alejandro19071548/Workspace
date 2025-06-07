from app.db import db

class ShippingInfo(db.Model):
    __tablename__ = 'shipping_info'

    id = db.Column(db.Integer, primary_key=True)
    order_id = db.Column(db.Integer, db.ForeignKey('orders.id'), nullable=False)
    direccion = db.Column(db.Text, nullable=False)
    ciudad = db.Column(db.String(100))
    estado = db.Column(db.String(100))
    codigo_postal = db.Column(db.String(20))
    pais = db.Column(db.String(100))
