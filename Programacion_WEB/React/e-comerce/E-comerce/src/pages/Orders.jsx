import { useEffect, useState, useContext } from 'react';
import API from '../api/api';
import { AuthContext } from '../context/AuthContext';

export default function Orders() {
  const { user } = useContext(AuthContext);
  const [ordenes, setOrdenes] = useState([]);

  useEffect(() => {
    if (user) {
      API.get(`/my-orders/${user.id}`).then(res => setOrdenes(res.data));
    }
  }, [user]);

  return (
    <div className="p-4">
      <h2 className="text-xl mb-4">Mis pedidos</h2>
      {ordenes.length === 0 ? (
        <p>No tienes pedidos todavía.</p>
      ) : (
        ordenes.map(orden => (
          <div key={orden.id} className="border-b py-2">
            <p><strong>ID:</strong> {orden.id}</p>
            <p><strong>Fecha:</strong> {orden.fecha}</p>
            <p><strong>Total:</strong> ${orden.total}</p>
            <p><strong>Dirección:</strong> {orden.direccion}</p>
          </div>
        ))
      )}
    </div>
  );
}
