// src/components/PrivateRoute.jsx
import React, { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { UserContext } from '../context/UserContext';

const PrivateRoute = ({ children, adminOnly = false }) => {
  const { userToken, userData, isLoading } = useContext(UserContext);

  if (isLoading) {
    return <div>Cargando...</div>; // O un spinner/componente de carga
  }

  // Si no hay token, redirigir al login
  if (!userToken) {
    return <Navigate to="/login" replace />;
  }

  // Si es solo para administradores y el usuario no es admin, redirigir a la página principal
  if (adminOnly && (!userData || !userData.is_admin)) {
    return <Navigate to="/" replace />; // O a una página de "Acceso Denegado"
  }

  return children;
};

export default PrivateRoute;