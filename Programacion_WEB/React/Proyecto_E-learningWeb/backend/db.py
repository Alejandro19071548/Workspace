from flask_mysqldb import MySQL
from flask import Flask
from config import MYSQL_CONFIG

mysql = MySQL()

def init_app(app):
    app.config['MYSQL_HOST'] = MYSQL_CONFIG['host']
    app.config['MYSQL_USER'] = MYSQL_CONFIG['user']
    app.config['MYSQL_PASSWORD'] = MYSQL_CONFIG['password']
    app.config['MYSQL_DB'] = MYSQL_CONFIG['database']
    mysql.init_app(app)
