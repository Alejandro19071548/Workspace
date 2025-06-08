import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css';

const Navbar = () => {
  return (
    <header className="navbar">
      <h1 className="logo">Mi Tienda</h1>
      <nav>
        <Link to="/">Inicio</Link>
        <Link to="/cart">Carrito</Link>
        <Link to="/login">Iniciar sesión</Link>
        <Link to="/registro">Registro</Link>
      </nav>
    </header>
  );
};

export default Navbar;
