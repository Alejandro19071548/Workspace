// src/pages/HomePage.jsx
import React, { useEffect, useState, useContext } from 'react';
import ProductCard from '../components/ProductCard';
import { CartContext } from '../context/CartContext';
import './HomePage.css';

const HomePage = () => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { addToCart } = useContext(CartContext);

    useEffect(() => {
        const fetchProducts = async () => {
            try {
                // Esta ruta ya trae solo productos activos por defecto gracias a Product.get_all()
                const response = await fetch('http://localhost:5000/api/products');
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const data = await response.json();
                setProducts(data); // data ya solo contiene productos activos
            } catch (err) {
                setError('Failed to fetch products: ' + err.message);
                console.error('Error fetching products:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchProducts();
    }, []);

    if (loading) {
        return <div className="loading-message">Cargando productos...</div>;
    }

    if (error) {
        return <div className="error-message">Error: {error}</div>;
    }

    return (
        <div className="homepage-container">
            <h1>Explora Nuestros Productos</h1>
            <div className="product-list-container">
                {products.length > 0 ? (
                    products.map((product) => (
                        // Asegúrate de que ProductCard pueda manejar la imagen
                        <ProductCard key={product.id} product={product} onAddToCart={addToCart} />
                    ))
                ) : (
                    <p>No hay productos disponibles en este momento.</p>
                )}
            </div>
        </div>
    );
};

export default HomePage;