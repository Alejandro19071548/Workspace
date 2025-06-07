from app.db import db

class User(db.Model):
    __tablename__ = 'users'
    
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(50), unique=True, nullable=False)
    password = db.Column(db.String(255), nullable=False)
    nombre = db.Column(db.String(100))
    apellido = db.Column(db.String(100))
    email = db.Column(db.String(100), unique=True)
    is_active = db.Column(db.Boolean, default=True)
