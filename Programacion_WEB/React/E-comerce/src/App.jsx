import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Header from './components/Header'; // Asegúrate de que Header pueda acceder al UserContext si muestra datos de usuario
import HomePage from './pages/HomePage'; // Esta sería tu página principal de productos ahora
import Login from './pages/Login';
import Register from './pages/Register';
import Account from './pages/Account';
import CourseDetailPage from './pages/CourseDetailPage'; // Si aún lo necesitas
import PrivateRoute from './components/PrivateRoute';
// Importar los nuevos contextos y componentes
import { UserProvider } from './context/UserContext';
import { CartProvider } from './context/CartContext';
import AdminDashboard from './pages/AdminDashboard'; // Ahora HomePage es la que muestra los productos
import CartComponent from './components/Cart'; // Cambié el nombre para evitar conflictos con la palabra "Cart"

function App() {
  // Ya no necesitamos 'user' aquí, el UserContext lo manejará
  // const [user, setUser] = useState(null);

  return (
    // Envuelve toda la aplicación con los Providers
    <UserProvider>
      <CartProvider>
        <Router> {/* Usa BrowserRouter para las rutas */}
          {/* El Header ahora puede obtener el usuario del UserContext */}
          <Header /> {/* Puede que necesites modificar Header para usar useContext(UserContext) */}

          <Routes>
            {/* La página principal ahora muestra los productos */}
            <Route path="/" element={<HomePage/>} /> {/* Intro.jsx ya no es la principal de productos */}
            {/* Ruta del detalle del curso, si sigue siendo relevante */}
            <Route path="/course/:id" element={<CourseDetailPage />} />
            <Route path="/login" element={<Login />} /> {/* Login ya no necesita setUser directamente */}
            <Route path="/register" element={<Register />} />

            {/* Rutas Protegidas */}
            <Route
              path="/account"
              element={
                <PrivateRoute>
                  <Account /> {/* Account también deberá usar UserContext */}
                </PrivateRoute>
              }
            />
            <Route
              path="/admin"
              element={
                <PrivateRoute adminOnly={true}>
                  <AdminDashboard />
                </PrivateRoute>
              }
            />
            {/* Ruta para ver el carrito de compras en una página dedicada (opcional) */}
            <Route path="/cart-view" element={<CartComponent />} />

          </Routes>
        </Router>
      </CartProvider>
    </UserProvider>
  );
}

export default App;