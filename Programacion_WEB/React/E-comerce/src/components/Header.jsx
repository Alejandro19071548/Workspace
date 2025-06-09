import React, { useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { UserContext } from '../context/UserContext';
import './Header.css'; // Crea o actualiza este archivo CSS

const Header = () => {
    const { userToken, userData, logout } = useContext(UserContext);
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/'); // Redirige al inicio después de cerrar sesión
    };

    return (
        <header className="main-header">
            <div className="container">
                <Link to="/" className="logo">Electronics AEs</Link>
                <nav className="main-nav">
                    <ul>
                        <li><Link to="/">Inicio</Link></li>
                        <li><Link to="/cart-view">Carrito</Link></li>
                        {userData && userData.is_admin && ( // Panel de administrador solo visible para admins
                            <li><Link to="/admin">Panel Administrador</Link></li>
                        )}
                        {userToken ? (
                            <>
                                <li><Link to="/account">Mi Cuenta</Link></li>
                                <li><button onClick={handleLogout} className="logout-button">Cerrar Sesión</button></li>
                            </>
                        ) : (
                            <>
                                <li><Link to="/login">Iniciar Sesión</Link></li>
                                <li><Link to="/register">Registro</Link></li>
                            </>
                        )}
                    </ul>
                </nav>
            </div>
        </header>
    );
};

export default Header;