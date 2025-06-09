from flask import Flask, render_template, request
from flask_mysqldb import MySQL

# Crear app Flask
app = Flask(__name__)

# Configuración de conexión a MySQL
app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'Edelangel20'  # Si tienes contraseña, colócala aquí
app.config['MYSQL_DB'] = 'flask7'

mysql = MySQL(app)

#@app.route('/')
#def index():
#    return '¡Flask está funcionando!'

# Ruta para mostrar el formulario
@app.route('/form')
def form():
    return render_template('form.html')

# Ruta para insertar datos en la base
@app.route('/login', methods=['POST', 'GET'])
def login():
    if request.method == 'GET':
        return "Ingresa a través del formulario."
    
    if request.method == 'POST':
        name = request.form['name']
        age = request.form['age']
        cursor = mysql.connection.cursor()
        cursor.execute('''INSERT INTO info_table (name, age) VALUES (%s, %s)''', (name, age))
        mysql.connection.commit()
        cursor.close()
        return f"Registro guardado correctamente: {name}, {age}"

# Ruta para mostrar todos los registros
@app.route('/data')
def data():
    cursor = mysql.connection.cursor()
    cursor.execute("SELECT name, age FROM info_table")
    records = cursor.fetchall()
    cursor.close()
    return render_template('data.html', records=records)

# Ejecutar la app
if __name__ == '__main__':
    app.run(host='localhost', port=5000)
