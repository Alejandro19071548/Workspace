import { useEffect, useState, useContext } from 'react';
import { useParams } from 'react-router-dom';
import API from '../api/api';
import { AuthContext } from '../context/AuthContext';
import { CartContext } from '../context/CartContext';

export default function ProductDetail() {
  const { id } = useParams();
  const [producto, setProducto] = useState(null);
  const { user } = useContext(AuthContext);
  const { addToCart } = useContext(CartContext);

  useEffect(() => {
    API.get(`/productos/${id}`).then(res => setProducto(res.data));
  }, [id]);

  if (!producto) return <p>Cargando...</p>;

  return (
    <div className="p-4">
      <h2 className="text-2xl">{producto.nombre}</h2>
      <p>{producto.descripcion}</p>
      <p className="font-bold">${producto.precio}</p>
      {user && (
        <button onClick={() => addToCart(user.id, producto.id)} className="bg-blue-600 text-white px-4 py-2 mt-2">
          Agregar al carrito
        </button>
      )}
    </div>
  );
}
