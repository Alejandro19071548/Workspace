from app.db import db

class Product(db.Model):
    __tablename__ = 'products'

    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(100), nullable=False)
    descripcion = db.Column(db.Text)
    precio = db.Column(db.Numeric(10, 2), nullable=False)
    imagen_url = db.Column(db.String(255))
    stock = db.Column(db.Integer, default=0)
    creado_en = db.Column(db.DateTime, server_default=db.func.now())
