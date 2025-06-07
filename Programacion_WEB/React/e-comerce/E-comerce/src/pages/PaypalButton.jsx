import React, { useEffect } from 'react';
import loadPaypalScript from '../../backend/app/utils/paypal';

const PaypalButton = ({ courseId }) => {
  useEffect(() => {
    loadPaypalScript(() => {
      window.paypal.Buttons({
        createOrder: (data, actions) => {
          return actions.order.create({
            purchase_units: [{
              amount: {
                value: '9.99' // Precio simulado
              }
            }]
          });
        },
        onApprove: (data, actions) => {
          return actions.order.capture().then(() => {
            alert('¡Pago completado! Puedes descargar tu certificado.');
            // Aquí podrías hacer una petición adicional para marcarlo como pagado o generar PDF
          });
        }
      }).render('#paypal-button-container');
    });
  }, []);

  return <div id="paypal-button-container" className="mt-4"></div>;
};

export default PaypalButton;
