from .product_routes import product_bp
from .cart_routes import cart_bp
from .order_routes import order_bp
from .auth_routes import auth_bp
from .order_history_routes import order_history_bp

def register_routes(app):
    app.register_blueprint(product_bp)
    app.register_blueprint(cart_bp)
    app.register_blueprint(order_bp)
    app.register_blueprint(auth_bp)
    app.register_blueprint(order_history_bp)