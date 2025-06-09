import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import "./styles.css";
import { BrowserRouter } from 'react-router-dom';
import { UserProvider } from './context/UserContext.jsx';
import { CartProvider } from './context/CartContext.jsx';
ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
        <UserProvider>
          <CartProvider> {/* Asegúrate de que CartProvider envuelva tu aplicación */}
            <App />
          </CartProvider>
        </UserProvider>
      </React.StrictMode>,
);