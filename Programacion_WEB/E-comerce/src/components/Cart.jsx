// src/components/Cart.jsx
import React, { useContext, useState, useEffect } from 'react';
import { CartContext } from '../context/CartContext'; // Asegúrate de que la ruta sea correcta
import { UserContext } from '../context/UserContext'; // Para obtener el token JWT
import { PayPalScriptProvider, PayPalButtons } from "@paypal/react-paypal-js";
import axios from 'axios';
import './Cart.css'; // Asegúrate de que tienes estilos para el carrito

const Cart = () => {
  const { cartItems, removeFromCart, clearCart, getTotalPrice } = useContext(CartContext);
  const { user, authToken } = useContext(UserContext); // Obtener user y authToken del contexto
  const [checkoutSuccessful, setCheckoutSuccessful] = useState(false);
  const [paypalError, setPaypalError] = useState(null);

  // Opciones para el script de PayPal
  // Asegúrate de que este Client ID sea de tu aplicación de Sandbox
  const paypalScriptOptions = {
    "client-id": "ARB2RAItoNVcyg3Rk2GmyFGi1D1-F0gRWszi9fm6lhRKOtF2e-Wfsm9gxdRrK-ZjYIIv0ufhveOx16Va", // Reemplaza con tu CLIENT ID de SANDBOX
    currency: "USD"
  };

  // Función para crear la orden de PayPal en el backend
  const createOrder = async (data, actions) => {
    setPaypalError(null); // Limpiar errores anteriores
    if (!authToken || !user) {
      alert("Debes iniciar sesión para proceder con el pago.");
      throw new Error("Usuario no autenticado");
    }

    if (cartItems.length === 0) {
      alert("El carrito está vacío.");
      throw new Error("El carrito está vacío");
    }

    try {
      const response = await axios.post(
        'http://localhost:5000/api/checkout/create-paypal-order',
        {
          items: cartItems.map(item => ({
            product_id: item.product.id,
            quantity: item.quantity
          })),
          user_id: user.id // Envía el user_id para que el backend lo asocie a la orden
        },
        {
          headers: {
            Authorization: `Bearer ${authToken}` // Envía el token JWT
          }
        }
      );
      const order = response.data;
      return order.id; // Devuelve el ID de la orden de PayPal
    } catch (error) {
      console.error("Error al crear la orden de PayPal en el backend:", error.response ? error.response.data : error);
      setPaypalError(error.response?.data?.msg || "Hubo un error al crear la orden de PayPal. Intenta de nuevo.");
      alert("Hubo un error al iniciar el pago. Revisa la consola para más detalles.");
      throw error; // Propagar el error para que PayPal lo maneje
    }
  };

  // Función para capturar la orden de PayPal en el backend
  const onApprove = async (data, actions) => {
    setPaypalError(null); // Limpiar errores anteriores
    if (!authToken || !user) {
      alert("Debes iniciar sesión para completar el pago.");
      throw new Error("Usuario no autenticado");
    }

    try {
      const response = await axios.post(
        'http://localhost:5000/api/checkout/capture-paypal-order',
        {
          order_id: data.orderID, // ID de la orden de PayPal
          user_id: user.id // Envía el user_id
        },
        {
          headers: {
            Authorization: `Bearer ${authToken}` // Envía el token JWT
          }
        }
      );

      if (response.status === 200) {
        setCheckoutSuccessful(true);
        clearCart(); // Limpiar el carrito después de una compra exitosa
        alert("¡Compra realizada con éxito! Revisa tus órdenes.");
        // Redirigir al usuario a una página de confirmación o de historial de órdenes
        // navigate('/order-confirmation'); // Si usas react-router-dom
      } else {
        setPaypalError(response.data?.msg || "El pago fue aprobado, pero hubo un error al registrar la orden.");
        alert("El pago fue aprobado, pero hubo un error al registrar la orden. Contacta soporte.");
        console.error("Error al registrar la orden en el backend:", response.data);
      }
    } catch (error) {
      console.error("Error al capturar la orden de PayPal en el backend:", error.response ? error.response.data : error);
      setPaypalError(error.response?.data?.msg || "Hubo un error al procesar el pago final. Intenta de nuevo o contacta soporte.");
      alert("Hubo un error al procesar el pago final. Revisa la consola para más detalles.");
      throw error; // Propagar el error
    }
  };

  // Manejo de errores del botón de PayPal
  const onError = (err) => {
    console.error("PayPal button error:", err);
    setPaypalError("Ocurrió un error con el botón de PayPal. Por favor, inténtalo de nuevo.");
    alert("Ocurrió un error con el botón de PayPal. Revisa la consola para más detalles.");
  };

  // Manejo de la cancelación de PayPal
  const onCancel = (data) => {
    console.log("Pago de PayPal cancelado:", data);
    setPaypalError("El proceso de pago fue cancelado.");
    alert("El pago ha sido cancelado.");
  };


  if (cartItems.length === 0) {
    return (
      <div className="cart">
        <h2>Tu Carrito</h2>
        <p>¡Tu carrito está vacío!</p>
        {checkoutSuccessful && (
          <p className="success-message">¡Tu compra se ha completado con éxito!</p>
        )}
      </div>
    );
  }

  return (
    <div className="cart">
      <h2>Tu Carrito</h2>
      <div className="cart-items-list">
        {cartItems.map(item => (
          <div key={item.product.id} className="cart-item">
            <span className="item-name">{item.product.name}</span>
            <span className="item-quantity">Cantidad: {item.quantity}</span>
            <span className="item-price">Precio: ${(item.product.price * item.quantity).toFixed(2)}</span>
            <button
              className="remove-button"
              onClick={() => removeFromCart(item.product.id)}
            >
              Eliminar
            </button>
          </div>
        ))}
      </div>

      <div className="cart-summary">
        <h3>Total: ${getTotalPrice().toFixed(2)}</h3>
        <button className="clear-cart-button" onClick={clearCart}>
          Vaciar Carrito
        </button>
      </div>

      {paypalError && (
        <div className="error-message" style={{ color: 'red', marginTop: '10px' }}>
          {paypalError}
        </div>
      )}

      {/* Contenedor del botón de PayPal */}
      {user && ( // Solo muestra los botones de PayPal si el usuario está logueado
        <div className="paypal-button-container">
          <PayPalScriptProvider options={paypalScriptOptions}>
            <PayPalButtons
              style={{ layout: "vertical" }}
              createOrder={createOrder}
              onApprove={onApprove}
              onError={onError}
              onCancel={onCancel}
            />
          </PayPalScriptProvider>
        </div>
      )}

      {!user && (
        <p style={{ marginTop: '20px', textAlign: 'center' }}>
          Por favor, <a href="/login">inicia sesión</a> para proceder al pago.
        </p>
      )}
    </div>
  );
};

export default Cart;