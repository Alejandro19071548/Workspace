import { Link } from "react-router-dom";
import "/src/styles.css";
import { useUser } from "../context/UserContext";

export default function Header() {
  const { user, logout } = useUser();

  return (
    <header className="bg-blue-800 text-white p-4 shadow-md">
      <div className="container mx-auto flex justify-between items-center">
        <div className="text-xl font-bold">EduPlatform</div>
        <nav className="space-x-4">
          {user ? (
            <>
              <Link to="/account" className="hover:underline">Cuenta</Link>
              <button onClick={logout} className="hover:underline">Cerrar sesión</button>
            </>
          ) : (
            <>
              <Link to="/login" className="hover:underline">Iniciar Sesión</Link>
              <Link to="/register" className="hover:underline">Registrarse</Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
}
