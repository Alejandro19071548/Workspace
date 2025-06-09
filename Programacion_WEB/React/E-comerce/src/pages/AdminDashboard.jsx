import React, { useState, useEffect, useContext } from 'react';
import { UserContext } from '../context/UserContext';

const AdminDashboard = () => {
  const { userToken, userData } = useContext(UserContext);
  const [products, setProducts] = useState([]);
  const [newProduct, setNewProduct] = useState({ name: '', description: '', price: '', stock: '' });

  useEffect(() => {
    if (userData && userData.is_admin) {
      fetchProducts();
    }
  }, [userData]);

  const fetchProducts = async () => {
    try {
      const response = await fetch('http://localhost:5000/api/products', {
        headers: { 'Authorization': `Bearer ${userToken}` }
      });
      if (response.ok) {
        const data = await response.json();
        setProducts(data);
      } else {
        console.error('Error al obtener productos para admin:', response.statusText);
      }
    } catch (error) {
      console.error('Error de conexión:', error);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewProduct({ ...newProduct, [name]: value });
  };

 const handleAddProduct = async (e) => {
  e.preventDefault();

  const formData = new FormData();
  formData.append('name', newProduct.name);
  formData.append('description', newProduct.description);
  formData.append('price', newProduct.price);
  formData.append('stock', newProduct.stock);
  // Si quieres agregar imagen en el futuro:
  // formData.append('image', fileInput.current.files[0]);

  try {
    const response = await fetch('http://localhost:5000/api/products', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${userToken}`,
        // ¡NO pongas 'Content-Type' manualmente aquí!
      },
      body: formData,
    });

    if (response.ok) {
      alert('Producto agregado con éxito!');
      setNewProduct({ name: '', description: '', price: '', stock: '' });
      fetchProducts();
    } else {
      const errorData = await response.json();
      alert(`Error al agregar producto: ${errorData.msg || 'Inténtalo de nuevo.'}`);
    }
  } catch (error) {
    console.error('Error al agregar producto:', error);
    alert('Hubo un problema con la conexión.');
  }
};


  const handleDeleteProduct = async (productId) => {
    if (window.confirm('¿Estás seguro de que quieres eliminar este producto?')) {
      try {
        const response = await fetch(`http://localhost:5000/api/products/${productId}`, {
          method: 'DELETE',
          headers: { 'Authorization': `Bearer ${userToken}` }
        });
        if (response.ok) {
          alert('Producto eliminado con éxito!');
          fetchProducts();
        } else {
          const errorData = await response.json();
          alert(`Error al eliminar producto: ${errorData.msg || 'Inténtalo de nuevo.'}`);
        }
      } catch (error) {
        console.error('Error al eliminar producto:', error);
        alert('Hubo un problema con la conexión.');
      }
    }
  };

  if (!userData || !userData.is_admin) {
    return <p className="access-denied">Acceso denegado. Solo administradores pueden ver esta página.</p>;
  }

  return (
    <div className="admin-dashboard">
      <h2 className="dashboard-title">Panel de Administración de Productos</h2>

      <section className="product-form-section">
        <h3>Agregar Nuevo Producto</h3>
        <form className="product-form" onSubmit={handleAddProduct}>
          <input
            type="text"
            name="name"
            placeholder="Nombre del Producto"
            value={newProduct.name}
            onChange={handleInputChange}
            required
          />
          <textarea
            name="description"
            placeholder="Descripción"
            value={newProduct.description}
            onChange={handleInputChange}
          ></textarea>
          <input
            type="number"
            name="price"
            placeholder="Precio"
            step="0.01"
            value={newProduct.price}
            onChange={handleInputChange}
            required
          />
          <input
            type="number"
            name="stock"
            placeholder="Cantidad (Stock)"
            value={newProduct.stock}
            onChange={handleInputChange}
            required
          />
          <button type="submit" className="add-button">Agregar Producto</button>
        </form>
      </section>

      <section className="product-list-section">
        <h3>Productos Existentes</h3>
        <div className="product-list">
          {products.map(product => (
            <div key={product.id} className="product-item">
              <div className="product-info">
                <strong>{product.name}</strong> - ${product.price.toFixed(2)} - Stock: {product.stock}
              </div>
              <button
                className="delete-button"
                onClick={() => handleDeleteProduct(product.id)}
              >
                Eliminar
              </button>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
};

export default AdminDashboard;
