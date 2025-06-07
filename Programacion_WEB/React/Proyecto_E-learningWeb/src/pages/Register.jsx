import { useState } from "react";
import axios from "axios";
import "./AuthForm.css";

export default function Register() {
  const [formData, setFormData] = useState({
    username: "",
    name: "",
    apellidos: "",
    email: "",
    password: ""
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = e => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async e => {
    e.preventDefault();
    setError("");
    setSuccess("");

    // Validación local de contraseña
    if (formData.password.length < 6) {
      setError("La contraseña debe tener al menos 6 caracteres.");
      return;
    }

    try {
      const res = await axios.post("http://localhost:5000/api/auth/register", formData);
      setSuccess(res.data.message);
      setFormData({
        username: "",
        name: "",
        apellidos: "",
        email: "",
        password: ""
      });
    } catch (err) {
      if (err.response?.data?.error) {
        setError(err.response.data.error);
      } else {
        setError("Error al registrar el usuario.");
      }
    }
  };

  return (
    <div className="auth-container">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Registro</h2>
        {error && <p className="form-error">{error}</p>}
        {success && <p className="form-success">{success}</p>}

        <input
          name="username"
          placeholder="Nombre de usuario"
          value={formData.username}
          onChange={handleChange}
        />
        <input
          name="name"
          placeholder="Nombre"
          value={formData.name}
          onChange={handleChange}
        />
        <input
          name="apellidos"
          placeholder="Apellidos"
          value={formData.apellidos}
          onChange={handleChange}
        />
        <input
          name="email"
          placeholder="Correo electrónico"
          type="email"
          value={formData.email}
          onChange={handleChange}
        />
        <input
          name="password"
          placeholder="Contraseña"
          type="password"
          value={formData.password}
          onChange={handleChange}
        />

        <button type="submit">Registrarse</button>
      </form>
    </div>
  );
}
