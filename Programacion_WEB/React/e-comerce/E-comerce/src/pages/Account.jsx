import { useEffect, useState } from "react";
import axios from "axios";
import { useUser } from "../context/UserContext";
import "./AuthForm.css";

export default function Account() {
  const { user, logout, login } = useUser();
  const [formData, setFormData] = useState({
    username: "",
    nombre: "",
    apellido: "",
    correo: "",
    contraseña: ""
  });
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (user) {
      axios.get(`http://localhost:5000/api/account/${user.id}`)
        .then(res => {
          const { username, nombre, apellido, correo } = res.data;
          setFormData({
            username,
            nombre,
            apellido,
            correo,
            contraseña: ""
          });
        })
        .catch(() => setError("Error al cargar los datos del usuario"));
    }
  }, [user]);

  const handleChange = e => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      const res = await axios.put(`http://localhost:5000/api/account/${user.id}`, formData);
      setMessage(res.data.message);
      setError("");
      login({ ...user, ...formData });
    } catch (err) {
      setError("Error al actualizar los datos");
      setMessage("");
    }
  };

  const handleDelete = async () => {
    if (!window.confirm("¿Seguro que quieres desactivar tu cuenta?")) return;
    try {
      await axios.delete(`http://localhost:5000/api/account/${user.id}`);
      logout();
      alert("Cuenta desactivada correctamente");
    } catch {
      alert("Error al desactivar la cuenta");
    }
  };

  return (
    <div className="auth-container">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Configuración de cuenta</h2>
        {error && <p className="form-error">{error}</p>}
        {message && <p className="form-success">{message}</p>}
        
        <input
          name="username"
          placeholder="Nombre de usuario"
          value={formData.username}
          onChange={handleChange}
        />
        <input
          name="nombre"
          placeholder="Nombre"
          value={formData.nombre}
          onChange={handleChange}
        />
        <input
          name="apellido"
          placeholder="Apellido"
          value={formData.apellido}
          onChange={handleChange}
        />
        <input
          name="correo"
          placeholder="Correo"
          value={formData.correo}
          onChange={handleChange}
        />
        <input
          name="contraseña"
          type="password"
          placeholder="Nueva contraseña"
          value={formData.contraseña}
          onChange={handleChange}
        />
        
        <button type="submit">Guardar cambios</button>
        <button type="button" onClick={handleDelete} className="danger">
          Eliminar cuenta
        </button>
      </form>
    </div>
  );
}
