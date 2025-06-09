# Plataforma E-learning de Cursos Digitales

Este repositorio contiene el código fuente de una plataforma E-learning diseñada para ofrecer y gestionar **cursos digitales**. Los usuarios pueden explorar un catálogo de cursos, inscribirse en ellos (implícitamente a través de la compra), y al finalizar, tienen la opción de adquirir un **certificado de finalización** mediante un pago seguro a través de PayPal. La plataforma también incluye un sistema robusto de autenticación de usuarios y la gestión de órdenes de compra.

## Características Principales

* **Autenticación de Usuarios:** Registro, inicio de sesión y gestión de sesiones mediante JWT (JSON Web Tokens).
* **Catálogo de Cursos:** Visualización detallada de los cursos disponibles, incluyendo descripciones y precios.
* **Gestión de Inscripciones/Compras:** Funcionalidad para que los usuarios adquieran cursos y los añadan a su historial de aprendizaje.
* **Procesamiento de Pagos:** Integración con PayPal para transacciones seguras de cursos y certificados (sandbox para desarrollo).
* **Gestión de Órdenes:** Creación y seguimiento de órdenes de compra en la base de datos para los cursos adquiridos.
* **Seguimiento de Progreso (Implícito):** Registro de la finalización de cursos para habilitar la compra de certificados.
* **Generación y Compra de Certificados:** Opción para que los usuarios compren y obtengan un certificado PDF de finalización de un curso.
* **Subida de Imágenes:** Gestión de imágenes asociadas a los cursos en el backend.

## Tecnologías Utilizadas

### Frontend
* **React:** Biblioteca para construir interfaces de usuario interactivas.
* **React Router DOM:** Para la gestión de rutas y navegación.
* **Context API:** Para la gestión de estado global (carrito, usuario).
* **Axios:** Cliente HTTP para realizar peticiones a la API.
* **PayPal JavaScript SDK:** Integración de botones de pago de PayPal.
* **jsPDF:** Para la generación de certificados PDF.
* **Vite:** Herramienta de construcción para un entorno de desarrollo rápido.

### Backend
* **Flask:** Microframework de Python para la API RESTful.
* **Flask-MySQLdb:** Para la interacción con la base de datos MySQL.
* **Flask-JWT-Extended:** Para la autenticación basada en JWT.
* **Flask-CORS:** Para manejar las políticas de Cross-Origin Resource Sharing.
* **python-dotenv:** Para la gestión de variables de entorno.
* **Requests:** Biblioteca HTTP para realizar peticiones a la API de PayPal.

### Base de Datos
* **MySQL:** Sistema de gestión de bases de datos relacionales.

## Cómo Inicializar el Proyecto

Sigue estos pasos para configurar y ejecutar el proyecto en tu máquina local.

### 1. Prerrequisitos

Asegúrate de tener instalado lo siguiente:

* **Python 3.x**
* **pip** (Administrador de paquetes de Python)
* **Node.js** (versión 14 o superior)
* **npm** (Administrador de paquetes de Node.js, viene con Node.js)
* **MySQL Server** (y un cliente como MySQL Workbench o phpMyAdmin para gestionar la DB)

### 2. Configuración de la Base de Datos (MySQL)

1.  **Crea la base de datos:**
    Abre tu cliente MySQL y ejecuta el siguiente comando para crear la base de datos si no existe:
    ```sql
    CREATE DATABASE IF NOT EXISTS ecommerce_db;
    USE ecommerce_db;
    ```

2.  **Crea las tablas:**
    Ejecuta los siguientes scripts SQL para crear las tablas necesarias.

    ```sql
    -- Tabla de Usuarios
    CREATE TABLE IF NOT EXISTS users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(80) UNIQUE NOT NULL,
        email VARCHAR(120) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        is_admin BOOLEAN DEFAULT FALSE
    );

    -- Tabla de Cursos (antes 'products')
    CREATE TABLE IF NOT EXISTS products ( -- Renombra a 'courses' si prefieres mayor claridad
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        description TEXT,
        price DECIMAL(10, 2) NOT NULL, -- Precio de inscripción al curso
        image_url VARCHAR(255),
        # Si los cursos tuvieran cupo limitado, podrías añadir:
        # capacity INT NOT NULL DEFAULT 0
    );

    -- Tabla de Órdenes (para inscripciones a cursos)
    CREATE TABLE IF NOT EXISTS orders (
        id INT AUTO_INCREMENT PRIMARY KEY,
        user_id INT NOT NULL,
        total_amount DECIMAL(10, 2) NOT NULL,
        order_status VARCHAR(50) DEFAULT 'pending', -- e.g., 'pending', 'completed', 'cancelled'
        paypal_order_id VARCHAR(255) UNIQUE, -- ID de la orden de PayPal
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

    -- Tabla de Ítems de Orden (los cursos comprados dentro de una orden)
    CREATE TABLE IF NOT EXISTS order_items (
        id INT AUTO_INCREMENT PRIMARY KEY,
        order_id INT NOT NULL,
        product_id INT NOT NULL, -- Aquí se referirá al ID del curso (de la tabla 'products')
        quantity INT NOT NULL, -- Normalmente 1 para cursos, pero puede variar
        price DECIMAL(10, 2) NOT NULL, -- Precio individual del curso en el momento de la compra
        FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
    );

    -- Tabla para registrar cursos completados por usuarios (para habilitar el certificado)
    CREATE TABLE IF NOT EXISTS user_completed_courses (
        user_id INT NOT NULL,
        course_id INT NOT NULL,
        completion_date DATETIME DEFAULT CURRENT_TIMESTAMP,
        -- Si el certificado tiene un costo adicional y se compra por separado, puedes añadir el ID de la transacción:
        certificate_paypal_order_id VARCHAR(255) UNIQUE NULL, -- ID de la transacción del certificado, puede ser NULL inicialmente
        PRIMARY KEY (user_id, course_id),
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
        FOREIGN KEY (course_id) REFERENCES products(id) ON DELETE CASCADE
    );
    ```

### 3. Configuración del Backend

1.  **Clona el repositorio:**
    ```bash
    git clone [URL_DE_TU_REPOSITORIO]
    cd [nombre_de_tu_repositorio]
    ```

2.  **Navega a la carpeta del backend:**
    ```bash
    cd backend
    ```

3.  **Crea un entorno virtual (recomendado):**
    ```bash
    python -m venv venv
    ```

4.  **Activa el entorno virtual:**
    * **Windows:**
        ```bash
        .\venv\Scripts\activate
        ```
    * **macOS/Linux:**
        ```bash
        source venv/bin/activate
        ```

5.  **Instala las dependencias de Python:**
    Si no tienes un `requirements.txt`, crea uno con estas librerías o instálalas manualmente:
    ```bash
    pip install Flask Flask-MySQLdb Flask-CORS Flask-JWT-Extended python-dotenv requests
    ```
    (Si tienes `requirements.txt`, usa: `pip install -r requirements.txt`)

6.  **Crea el archivo `.env`:**
    En la carpeta `backend/`, crea un archivo llamado `.env` y añade tus credenciales y secretos:
    ```dotenv
    MYSQL_HOST=localhost
    MYSQL_USER=[TU_USUARIO_MYSQL]
    MYSQL_PASSWORD=[TU_PASSWORD_MYSQL]
    MYSQL_DB=ecommerce_db

    # Clave secreta para JWT (genera una cadena aleatoria y compleja)
    JWT_SECRET_KEY=super-secreto-para-jwt-no-compartir

    # Credenciales de PayPal Sandbox (puedes obtenerlas en tu cuenta de desarrollador PayPal)
    PAYPAL_CLIENT_ID=[TU_CLIENT_ID_PAYPAL_SANDBOX]
    PAYPAL_SECRET=[TU_SECRET_PAYPAL_SANDBOX]
    PAYPAL_API_BASE_URL=[https://api-m.sandbox.paypal.com](https://api-m.sandbox.paypal.com) # O [https://api-m.paypal.com](https://api-m.paypal.com) para producción

    # Directorio donde se guardarán las imágenes de los cursos
    UPLOAD_FOLDER=uploads
    ALLOWED_EXTENSIONS=png,jpg,jpeg,gif
    ```
    **Importante:** Asegúrate de que `PAYPAL_API_BASE_URL` apunte a `sandbox` para desarrollo.

7.  **Ejecuta el Backend:**
    ```bash
    python app.py
    ```
    El backend se ejecutará en `http://localhost:5000`.

### 4. Configuración del Frontend

1.  **Navega a la carpeta del frontend:**
    (Asegúrate de estar en la raíz de tu proyecto o en la carpeta `frontend/` si la tienes así)
    ```bash
    cd .. # Si estás en la carpeta backend, regresa a la raíz
    cd frontend # Si tu frontend está en una subcarpeta 'frontend'
    ```

2.  **Instala las dependencias de Node.js:**
    ```bash
    npm install
    # o si usas yarn:
    # yarn install
    ```

3.  **Actualiza el Client ID de PayPal en el Frontend:**
    Abre `src/components/Cart.jsx` y `src/components/CourseDetailPage.jsx` (o el componente donde manejes la compra del certificado) y asegúrate de que el `client-id` en `PayPalScriptProvider` sea el mismo que tu `PAYPAL_CLIENT_ID` de PayPal Sandbox.

    ```jsx
    // Ejemplo en Cart.jsx y CourseDetailPage.jsx
    const paypalScriptOptions = {
      "client-id": "ARB2RAItoNVcyg3Rk2GmyFGi1D1-F0gRWszi9fm6lhRKOtF2e-Wfsm9gxdRrK-ZjYIIv0ufhveOx16Va", // <--- ¡Asegúrate de que este sea tu Client ID de PayPal Sandbox!
      currency: "USD"
    };
    ```

4.  **Ejecuta el Frontend:**
    ```bash
    npm run dev
    # o si usas yarn:
    # yarn dev
    ```
    El frontend se ejecutará en `http://localhost:5173` (o un puerto similar).

¡Listo! Con ambos (frontend y backend) ejecutándose, tu plataforma E-learning de cursos digitales debería estar en funcionamiento.

---

**Notas Adicionales:**

* **Modo Debug:** El backend se ejecuta en modo `debug=True`, lo que es útil para el desarrollo pero no se recomienda para producción.
* **Tokens JWT:** Los tokens JWT se guardan en `localStorage` del navegador. Para pruebas, puedes inspeccionar `Application -> Local Storage` en las herramientas de desarrollador del navegador.
* **Errores:** Si encuentras errores, revisa las consolas de tu navegador (frontend) y de tu terminal (backend) para mensajes de error.
* **CORS:** Las configuraciones de CORS están establecidas para permitir la comunicación entre `localhost:5173` (frontend) y `localhost:5000` (backend).
