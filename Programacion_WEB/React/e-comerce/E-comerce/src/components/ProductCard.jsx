import { useCart } from "../context/CartContext";

export default function ProductCard({ producto }) {
  const { addToCart } = useCart();

  return (
    <div className="border p-4 rounded-md shadow-md">
      <h3 className="text-lg font-bold">{producto.nombre}</h3>
      <p>${producto.precio}</p>
      <button
        onClick={() => addToCart(producto)}
        className="mt-2 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
      >
        Agregar al carrito
      </button>
    </div>
  );
}
