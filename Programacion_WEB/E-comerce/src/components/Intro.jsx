// src/pages/HomePage.jsx (Ejemplo, adapta a tu nombre de archivo principal)
import React, { useEffect, useState, useContext } from 'react';
import ProductCard from '../components/ProductCard';
import Cart from '../components/Cart';
import { CartContext } from "/src/context/CartContext";

const HomePage = () => {
  const [products, setProducts] = useState([]);
  const { addToCart } = useContext(CartContext);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await fetch('http://localhost:5000/api/products'); // Ajusta tu URL de backend
        if (response.ok) {
          const data = await response.json();
          setProducts(data);
        } else {
          console.error('Error al obtener productos:', response.statusText);
        }
      } catch (error) {
        console.error('Error de conexión:', error);
      }
    };
    fetchProducts();
  }, []);

  return (
    <div className="home-page">
      <h1>Nuestros Productos</h1>
      <div className="product-list">
        {products.map(product => (
          <ProductCard key={product.id} product={product} onAddToCart={addToCart} />
        ))}
      </div>
      <Cart /> {/* Mostrar el carrito en alguna parte de la página */}
    </div>
  );
};

export default Intro;