# backend/db.py
from flask_mysqldb import MySQL
# No necesitamos Flask aquí, ya que solo exportaremos la instancia MySQL.
# from flask import Flask 
# No necesitamos importar config si la configuración se hace en app.py
# from config import MYSQL_CONFIG

mysql = MySQL()

# Elimina la función init_app de aquí. La inicialización y configuración se hará en app.py
# def init_app(app):
#     app.config['MYSQL_HOST'] = MYSQL_CONFIG['host']
#     app.config['MYSQL_USER'] = MYSQL_CONFIG['user']
#     app.config['MYSQL_PASSWORD'] = MYSQL_CONFIG['password']
#     app.config['MYSQL_DB'] = MYSQL_CONFIG['database']
#     mysql.init_app(app)