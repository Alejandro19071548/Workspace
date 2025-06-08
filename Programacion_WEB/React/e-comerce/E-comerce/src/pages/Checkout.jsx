import { useContext, useState } from 'react';
import API from '../api/api';
import { AuthContext } from '../context/AuthContext';
import { CartContext } from '../context/CartContext';

export default function Checkout() {
  const { user } = useContext(AuthContext);
  const { cart } = useContext(CartContext);
  const [shipping, setShipping] = useState({ direccion: '', ciudad: '', pais: '' });

  const handleCheckout = async () => {
    await API.post('/checkout', {
      user_id: user.id,
      shipping
    });
    alert('Compra realizada con éxito');
  };

  return (
    <div>
      <h2>Dirección de envío</h2>
      <input placeholder="Dirección" onChange={(e) => setShipping({ ...shipping, direccion: e.target.value })} />
      <input placeholder="Ciudad" onChange={(e) => setShipping({ ...shipping, ciudad: e.target.value })} />
      <input placeholder="País" onChange={(e) => setShipping({ ...shipping, pais: e.target.value })} />
      <button onClick={handleCheckout}>Finalizar Compra</button>
    </div>
  );
}
