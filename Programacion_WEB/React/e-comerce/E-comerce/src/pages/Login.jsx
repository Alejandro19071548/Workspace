import { useState } from 'react';
import axios from 'axios';
import './AuthForm.css';
import { useUser } from '../context/UserContext';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const { login } = useUser();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!email || !password) {
      setError('Todos los campos son obligatorios');
      return;
    }

    try {
      const res = await axios.post('http://localhost:5000/api/auth/login', {
        email,
        password,
      });

      setSuccess(res.data.message);
      setError('');
      login(res.data.user); // res.data.user ahora incluye `id`
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Inicio de sesión fallido');
      setSuccess('');
    }
  };

  return (
    <div className="auth-container">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h2>Iniciar Sesión</h2>
        {error && <p className="form-error">{error}</p>}
        {success && <p className="form-success">{success}</p>}
        <input
          type="email"
          placeholder="Correo electrónico"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Ingresar</button>
      </form>
    </div>
  );
}
