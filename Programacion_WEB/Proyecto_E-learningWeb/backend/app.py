from flask import Flask
from flask_cors import CORS
from flask_mysqldb import MySQL
from routes.auth import auth_bp
from routes.account import account_bp
#from routes.courses import courses_bp
from routes.courses import courses

app = Flask(__name__)
CORS(app)

# Configuración de MySQL
app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'Stprm123'
app.config['MYSQL_DB'] = 'elearning'

from db import mysql
mysql.init_app(app)


# Registro de Blueprints
app.register_blueprint(courses)
#app.register_blueprint(courses_bp, url_prefix="/api/courses")
app.register_blueprint(auth_bp, url_prefix="/api/auth")
app.register_blueprint(account_bp, url_prefix="/api/account")

# Ruta de prueba para verificar que el backend está funcionando
@app.route("/")
def index():
    return "Backend Flask funcionando correctamente 🚀"
if __name__ == "__main__":
    app.run(debug=True)

