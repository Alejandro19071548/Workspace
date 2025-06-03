# Importación de librerías necesarias
from flask import Flask, render_template, request, redirect, url_for  # Funciones básicas de Flask
from flask_mysqldb import MySQL  # Extensión para conectar Flask con MySQL

# Inicialización de la aplicación Flask
app = Flask(__name__)

# Configuración de la base de datos MySQL
app.config['MYSQL_HOST'] = 'localhost'           # Dirección del servidor MySQL
app.config['MYSQL_USER'] = 'root'                # Usuario de la base de datos
app.config['MYSQL_PASSWORD'] = 'Edelangel20'     # Contraseña del usuario
app.config['MYSQL_DB'] = 'flask_crud'            # Nombre de la base de datos

# Inicialización de la extensión MySQL
mysql = MySQL(app)

# RUTA PARA VER TODOS LOS USUARIOS
@app.route('/')
def index():
    cur = mysql.connection.cursor()                  # Crear cursor para interactuar con la BD
    cur.execute("SELECT * FROM users")               # Ejecutar consulta para obtener todos los usuarios
    data = cur.fetchall()                            # Obtener todos los resultados
    return render_template('index.html', users=data) # Renderizar plantilla con los datos

# RUTA PARA AGREGAR USUARIO
@app.route('/add_user', methods=['POST'])
def add_user():
    if request.method == 'POST':                          # Verificar que la solicitud sea POST
        name = request.form['name']                       # Obtener el nombre del formulario
        email = request.form['email']                     # Obtener el email del formulario
        cur = mysql.connection.cursor()
        cur.execute("INSERT INTO users (name, email) VALUES (%s, %s)", (name, email))  # Insertar nuevo usuario
        mysql.connection.commit()                         # Confirmar cambios en la BD
        return redirect(url_for('index'))                 # Redirigir a la página principal

# RUTA PARA EDITAR USUARIO (mostrar datos en formulario)
@app.route('/edit/<id>')
def edit_user(id):
    cur = mysql.connection.cursor()
    cur.execute("SELECT * FROM users WHERE id = %s", [id])  # Obtener usuario por ID
    data = cur.fetchone()                                   # Obtener un solo resultado
    return render_template('form.html', user=data)          # Renderizar formulario con datos del usuario

# RUTA PARA ACTUALIZAR USUARIO (una vez editado el formulario)
@app.route('/update/<id>', methods=['POST'])
def update_user(id):
    if request.method == 'POST':
        name = request.form['name']                         # Obtener nuevo nombre
        email = request.form['email']                       # Obtener nuevo email
        cur = mysql.connection.cursor()
        cur.execute("""
            UPDATE users 
            SET name = %s, email = %s 
            WHERE id = %s
        """, (name, email, id))                             # Actualizar usuario con nuevos datos
        mysql.connection.commit()                           # Confirmar cambios
        return redirect(url_for('index'))                   # Volver a la página principal

# RUTA PARA ELIMINAR USUARIO
@app.route('/delete/<id>')
def delete_user(id):
    cur = mysql.connection.cursor()
    cur.execute("DELETE FROM users WHERE id = %s", [id])    # Eliminar usuario por ID
    mysql.connection.commit()                               # Confirmar eliminación
    return redirect(url_for('index'))                       # Redirigir a página principal

# Ejecutar la app si este archivo se ejecuta directamente
if __name__ == '__main__':
    app.run(debug=True)  # Ejecutar en modo depuración (útil durante el desarrollo)
