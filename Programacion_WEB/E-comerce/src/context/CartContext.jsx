// src/context/CartContext.jsx
import React, { createContext, useState, useEffect, useContext } from 'react';
import { UserContext } from './UserContext'; // Para obtener el token del usuario

export const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const [cartItems, setCartItems] = useState(() => {
        const localData = localStorage.getItem('cartItems');
        return localData ? JSON.parse(localData) : [];
    });
    const { userToken } = useContext(UserContext); // Asumiendo que userToken está disponible aquí

    useEffect(() => {
        localStorage.setItem('cartItems', JSON.stringify(cartItems));
    }, [cartItems]);

    const addToCart = (product) => {
        setCartItems(prevItems => {
            const existingItem = prevItems.find(item => item.product.id === product.id);
            if (existingItem) {
                return prevItems.map(item =>
                    item.product.id === product.id
                        ? { ...item, quantity: item.quantity + 1 }
                        : item
                );
            } else {
                return [...prevItems, { product, quantity: 1 }];
            }
        });
    };

    const removeFromCart = (productId) => {
        setCartItems(prevItems => prevItems.filter(item => item.product.id !== productId));
    };

    const clearCart = () => {
        setCartItems([]);
    };

    const getTotalPrice = () => {
        return cartItems.reduce((total, item) => total + (item.product.price * item.quantity), 0);
    };

    const checkout = async () => {
        if (!userToken) {
            alert("Debes iniciar sesión para realizar la compra.");
            return;
        }

        try {
            const response = await fetch('http://localhost:5000/api/orders', { // Ajusta tu URL de backend
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${userToken}`
                },
                body: JSON.stringify({
                    items: cartItems.map(item => ({
                        product_id: item.product.id,
                        quantity: item.quantity
                    }))
                })
            });

            if (response.ok) {
                const data = await response.json();
                alert(`Compra realizada con éxito! ID de Orden: ${data.id}`);
                clearCart();
                // Redirigir a una página de confirmación de compra o perfil del usuario
            } else {
                const errorData = await response.json();
                alert(`Error al procesar la compra: ${errorData.msg || 'Inténtalo de nuevo.'}`);
            }
        } catch (error) {
            console.error('Error al realizar el checkout:', error);
            alert('Hubo un problema con la conexión. Inténtalo de nuevo.');
        }
    };

    return (
        <CartContext.Provider value={{ cartItems, addToCart, removeFromCart, clearCart, getTotalPrice, checkout }}>
            {children}
        </CartContext.Provider>
    );
};