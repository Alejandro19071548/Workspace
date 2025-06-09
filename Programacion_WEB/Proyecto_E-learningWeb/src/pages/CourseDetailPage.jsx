import { useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import axios from 'axios';
import './CourseDetailsPage.css';
import { PayPalScriptProvider, PayPalButtons } from "@paypal/react-paypal-js";
import jsPDF from 'jspdf';

export default function CourseDetailPage() {
  const { id } = useParams();
  const [course, setCourse] = useState(null);
  const [completed, setCompleted] = useState(false);

  const userId = 1; // Temporal: ID simulado del usuario

  useEffect(() => {
    axios.get(`http://localhost:5000/api/course/${id}`)
      .then((res) => setCourse(res.data))
      .catch((err) => console.error('Error al cargar el curso:', err));
  }, [id]);

  const handleDownloadCertificate = () => {
    const doc = new jsPDF();

    const completionDate = new Date().toLocaleDateString();

    doc.setFontSize(22);
    doc.text('Certificado de Finalización', 20, 30);

    doc.setFontSize(16);
    doc.text(`Otorgado a: Usuario #${userId}`, 20, 50); // Puedes sustituir por nombre real si lo tienes
    doc.text(`Curso: ${course.title}`, 20, 60);
    doc.text(`Fecha: ${completionDate}`, 20, 70);

    doc.setFontSize(12);
    doc.text('Este certificado reconoce que el usuario ha completado satisfactoriamente el curso.', 20, 90, { maxWidth: 170 });

    doc.save(`certificado_${course.title}.pdf`);
  };
    
  if (!course) return <p>Cargando curso...</p>;

  return (
    <div className="course-detail">
      <h1>{course.title}</h1>
      <p>{course.description}</p>
      <hr />
      {course.contents.map((section) => (
        <div key={section.id} className="course-section">
          <h3>{section.title}</h3>
          <p>{section.content}</p>
        </div>
      ))}

      {!completed ? (
        <div className="paypal-container">
          <p style={{ marginBottom: '1rem' }}>
            🎓 Al finalizar el curso, podrás obtener un certificado de finalización. 
            Para ello, se requiere un pequeño pago de <strong>0.49 USD</strong>.
          </p>
          <PayPalScriptProvider options={{ "client-id": "ARB2RAItoNVcyg3Rk2GmyFGi1D1-F0gRWszi9fm6lhRKOtF2e-Wfsm9gxdRrK-ZjYIIv0ufhveOx16Va", currency: "USD" }}>
            <PayPalButtons
              style={{ layout: "vertical" }}
              createOrder={(data, actions) => {
                return actions.order.create({
                  purchase_units: [{
                    amount: { value: "0.49" }
                  }]
                });
              }}
              onApprove={(data, actions) => {
                return actions.order.capture().then(() => {
                  axios.post(`http://localhost:5000/api/course/${id}/complete`, {
                    user_id: userId
                  }).then(() => setCompleted(true));
                });
              }}
              onError={(err) => {
                console.error("Error en el pago:", err);
              }}
            />
          </PayPalScriptProvider>
        </div>
      ) : (
        <div className="completion-message">
          <>
            <p>🎓 Puedes descargar tu certificado de finalización:</p>
            <button className="btn-cert" onClick={handleDownloadCertificate}>
              Descargar certificado
            </button>
          </>
        </div>
      )}
    </div>
  );
}
