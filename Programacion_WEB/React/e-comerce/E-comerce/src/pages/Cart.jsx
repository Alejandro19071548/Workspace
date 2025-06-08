import { useCart } from "../context/CartContext";
import { Link } from "react-router-dom";

export default function Cart() {
  const { cartItems, removeFromCart } = useCart();

  const total = cartItems.reduce((acc, item) => acc + item.precio * item.cantidad, 0);

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Tu carrito</h2>

      {cartItems.length === 0 ? (
        <p className="text-gray-600">Tu carrito está vacío.</p>
      ) : (
        <div className="space-y-4">
          {cartItems.map(item => (
            <div
              key={item.id}
              className="flex justify-between items-center border p-4 rounded-md"
            >
              <div>
                <h3 className="text-lg font-semibold">{item.nombre}</h3>
                <p>Cantidad: {item.cantidad}</p>
                <p>Precio: ${item.precio}</p>
              </div>
              <button
                onClick={() => removeFromCart(item.id)}
                className="bg-red-500 text-white px-4 py-1 rounded hover:bg-red-600"
              >
                Eliminar
              </button>
            </div>
          ))}

          <div className="text-right mt-6">
            <p className="text-xl font-semibold">Total: ${total.toFixed(2)}</p>
            <Link
              to="/checkout"
              className="inline-block mt-2 bg-green-600 text-white px-6 py-2 rounded hover:bg-green-700"
            >
              Proceder al pago
            </Link>
          </div>
        </div>
      )}
    </div>
  );
}
