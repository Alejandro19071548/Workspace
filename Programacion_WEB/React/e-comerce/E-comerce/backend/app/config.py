import os

class Config:
    SECRET_KEY = os.environ.get("SECRET_KEY", "clave_secreta_segura")
    SQLALCHEMY_DATABASE_URI = os.environ.get("DATABASE_URL", "mysql+pymysql://root:tu_clave@localhost/ecommerce")
    SQLALCHEMY_TRACK_MODIFICATIONS = False
    JWT_SECRET_KEY = os.environ.get("JWT_SECRET_KEY", "super_jwt_secreto")
