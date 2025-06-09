# Plataforma E-commerce de Productos Electrónicos

Este repositorio contiene el código fuente de una plataforma de e-commerce diseñada para la venta de **productos electrónicos**. Los usuarios pueden explorar el catálogo de productos, añadirlos al carrito de compras y realizar pagos seguros a través de PayPal para adquirirlos. La plataforma también incluye un sistema de autenticación de usuarios y la gestión de órdenes de compra.

## Características Principales

* **Autenticación de Usuarios:** Registro, inicio de sesión y gestión de sesiones mediante JWT (JSON Web Tokens).
* **Catálogo de Productos:** Visualización detallada de los productos electrónicos disponibles para su compra.
* **Carrito de Compras:** Funcionalidad para añadir y eliminar productos del carrito, y gestionar cantidades.
* **Procesamiento de Pagos:** Integración con PayPal para transacciones seguras (sandbox para desarrollo).
* **Gestión de Órdenes:** Creación y seguimiento de órdenes de compra en la base de datos para los productos adquiridos.
* **Subida de Imágenes:** Gestión de imágenes asociadas a los productos en el backend.

## Tecnologías Utilizadas

### Frontend
* **React:** Biblioteca para construir interfaces de usuario interactivas.
* **React Router DOM:** Para la gestión de rutas y navegación.
* **Context API:** Para la gestión de estado global (carrito, usuario).
* **Axios:** Cliente HTTP para realizar peticiones a la API.
* **PayPal JavaScript SDK:** Integración de botones de pago de PayPal.
* **Vite:** Herramienta de construcción para un entorno de desarrollo rápido.
* **jsPDF:** (Mantenido por si es para facturas o recibos que no sean específicamente certificados de cursos, si no es necesario, puedes eliminarlo).

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

    -- Tabla de Productos (genérico, sirve para electrónicos)
    CREATE TABLE IF NOT EXISTS products (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        description TEXT,
        price DECIMAL(10, 2) NOT NULL,
        stock INT NOT NULL DEFAULT 0, -- Stock para productos físicos
        image_url VARCHAR(255)
    );

    -- Tabla de Órdenes
    CREATE TABLE IF NOT EXISTS orders (
        id INT AUTO_INCREMENT PRIMARY KEY,
        user_id INT NOT NULL,
        total_amount DECIMAL(10, 2) NOT NULL,
        order_status VARCHAR(50) DEFAULT 'pending', -- e.g., 'pending', 'completed', 'cancelled'
        paypal_order_id VARCHAR(255) UNIQUE, -- ID de la orden de PayPal
        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );

    -- Tabla de Ítems de Orden
    CREATE TABLE IF NOT EXISTS order_items (
        id INT AUTO_INCREMENT PRIMARY KEY,
        order_id INT NOT NULL,
        product_id INT NOT NULL,
        quantity INT NOT NULL,
        price DECIMAL(10, 2) NOT NULL,
        FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
    );

    -- Tabla para el historial de compras de productos (si necesitas un registro específico post-compra)
    -- Si 'orders' e 'order_items' ya son suficientes, esta tabla podría no ser estrictamente necesaria.
    -- La tabla user_completed_courses fue eliminada al ya no ser 'cursos' el foco principal.
    -- Si jsPDF se usará para facturas o recibos, no se necesita una tabla específica.
    -- Si tenías alguna otra tabla específica de "cursos", considera renombrarla o fusionarla con 'products' si aplica.
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
    Abre `src/components/Cart.jsx` y cualquier otro componente donde uses `PayPalScriptProvider` (anteriormente `CourseDetailPage.jsx`, ahora podría ser un `ProductDetailPage.jsx` genérico) y asegúrate de que el `client-id` sea el mismo que tu `PAYPAL_CLIENT_ID` de PayPal Sandbox.

    ```jsx
    // Ejemplo en Cart.jsx y ProductDetailPage.jsx (si lo tienes)
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

¡Listo! Con ambos (frontend y backend) ejecutándose, tu plataforma de e-commerce de productos electrónicos debería estar en funcionamiento.

---

**Notas Adicionales:**

* **Modo Debug:** El backend se ejecuta en modo `debug=True`, lo que es útil para el desarrollo pero no se recomienda para producción.
* **Tokens JWT:** Los tokens JWT se guardan en `localStorage` del navegador. Para pruebas, puedes inspeccionar `Application -> Local Storage` en las herramientas de desarrollador del navegador.
* **Errores:** Si encuentras errores, revisa las consolas de tu navegador (frontend) y de tu terminal (backend) para mensajes de error.
* **CORS:** Las configuraciones de CORS están establecidas para permitir la comunicación entre `localhost:5173` (frontend) y `localhost:5000` (backend).

**Notas Adicionales:**

* **Modo Debug:** El backend se ejecuta en modo `debug=True`, lo que es útil para el desarrollo pero no se recomienda para producción.
* **Tokens JWT:** Los tokens JWT se guardan en `localStorage` del navegador. Para pruebas, puedes inspeccionar `Application -> Local Storage` en las herramientas de desarrollador del navegador.
* **Errores:** Si encuentras errores, revisa las consolas de tu navegador (frontend) y de tu terminal (backend) para mensajes de error.
* **CORS:** Las configuraciones de CORS están establecidas para permitir la comunicación entre `localhost:5173` (frontend) y `localhost:5000` (backend).
