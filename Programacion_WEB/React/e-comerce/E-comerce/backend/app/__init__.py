from flask import Flask
from flask_cors import CORS
from .db import db
from .routes import register_routes
from flask_jwt_extended import JWTManager
from .config import Config

def create_app():
    app = Flask(__name__)
    app.config.from_object(Config)

    db.init_app(app)
    jwt = JWTManager(app)

    with app.app_context():
        db.create_all()
        register_routes(app)

    return app
