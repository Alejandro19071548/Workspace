import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './Home.css';

const Home = () => {
  const [productos, setProductos] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:5000/productos')
      .then(res => setProductos(res.data))
      .catch(err => console.error(err));
  }, []);

  return (
    <main className="home-container">
      <h2>Productos Disponibles</h2>
      <div className="productos-grid">
        {productos.map(producto => (
          <div key={producto.id} className="producto">
            <h3>{producto.nombre}</h3>
            <p>{producto.descripcion}</p>
            <p><strong>${producto.precio}</strong></p>
          </div>
        ))}
      </div>
    </main>
  );
};

export default Home;
