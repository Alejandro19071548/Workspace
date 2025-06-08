import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom'; // <-- aquí agregamos BrowserRouter
import { AuthProvider } from './context/AuthContext';
import { CartProvider } from './context/CartContext';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import Cart from './pages/Cart';
import Checkout from './pages/Checkout';
import ProductDetail from './pages/ProductDetail';
import Orders from './pages/Orders';
import './App.css';

function App() {
  return (
      <AuthProvider>
        <CartProvider>
          <Navbar />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/registro" element={<Register />} />
            <Route path="/cart" element={<Cart/>} />
            <Route path="/checkout" element={<Checkout />} />
            <Route path="/producto/:id" element={<ProductDetail />} />
            <Route path="/pedidos" element={<Orders />} />
          </Routes>
        </CartProvider>
      </AuthProvider>
  );
}

export default App;
