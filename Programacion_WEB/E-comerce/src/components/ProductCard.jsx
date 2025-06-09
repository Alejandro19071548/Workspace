// src/components/ProductCard.jsx
import React from 'react';
import './ProductCard.css'; // Importa el archivo CSS que crearemos

const ProductCard = ({ product, onAddToCart }) => {
    // onAddToCart es una función que se llama cuando se hace clic en "Agregar"

    // La lógica para la "rebaja" (original_price) debe venir de tu backend
    const hasDiscount = product.original_price && product.original_price > product.price;

    return (
        <div className="product-card">
            {hasDiscount && (
                <div className="rebaja-tag">
                    <span>Rebaja</span>
                </div>
            )}
            <div className="product-image-container">
                <img
                    src={product.image_url}
                    alt={product.name}
                    className="product-image"
                />
            </div>
            <div className="product-info">
                <div className="prices">
                    {hasDiscount && (
                        <span className="original-price">${product.original_price.toFixed(2)}</span>
                    )}
                    <span className="current-price">${product.price.toFixed(2)}</span>
                    {hasDiscount && (
                        <span className="discount">
                            Ahora ${Math.abs(product.original_price - product.price).toFixed(2)}
                        </span>
                    )}
                </div>
                <h3 className="product-name">{product.name}</h3>
                {/* Asumo que la descripción es parte del producto, aunque no visible en la imagen */}
                {/* <p className="product-description">{product.description}</p> */}

                <div className="additional-info">
                    {/* Puedes añadir la lógica para meses sin intereses aquí si la tienes */}
                    <span>Hasta 12 meses sin intereses</span>
                </div>
                {/* Asumo una calificación de ejemplo, podrías tener una prop para esto */}
                <div className="rating">
                    <span>⭐️⭐️⭐️⭐️⭐️ (425)</span> {/* Esto es un placeholder */}
                </div>

                <button
                    className="add-to-cart-button"
                    onClick={() => onAddToCart(product)}
                >
                    + Agregar
                </button>
            </div>
        </div>
    );
};

export default ProductCard;