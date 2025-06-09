# backend/utils/helpers.py

# Extensiones de archivo permitidas para las imágenes
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif'}

# Función para verificar si la extensión de un archivo es permitida
def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

# Puedes añadir otras funciones de utilidad aquí si las necesitas en el futuro.
# Por ejemplo, funciones para formatear datos, validar entradas genéricas, etc.