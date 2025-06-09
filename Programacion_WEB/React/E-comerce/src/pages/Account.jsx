// src/pages/Account.jsx (Modificación)
import React, { useContext } from 'react';
import { UserContext } from "../context/UserContext"; // <-- Usa esta línea// Importar UserContext

const Account = () => {
  const { userData, logout } = useContext(UserContext); // Obtener userData y logout del contexto

  if (!userData) {
    return <div>Cargando perfil o no autenticado.</div>; // Esto debería ser manejado por PrivateRoute
  }

  return (
    <div>
      <h2>Mi Cuenta</h2>
      <p>Bienvenido, {userData.username}!</p>
      <p>Email: {userData.email}</p>
      {userData.is_admin && <p>¡Eres un administrador!</p>} {/* Muestra si es admin */}
      <button onClick={logout}>Cerrar Sesión</button>
      {/* Aquí podrías añadir más detalles del perfil o un historial de órdenes */}
      <h3>Mis Órdenes (Futura Implementación)</h3>
      {/* Aquí podrías cargar las órdenes del usuario desde el backend */}
    </div>
  );
};

export default Account;