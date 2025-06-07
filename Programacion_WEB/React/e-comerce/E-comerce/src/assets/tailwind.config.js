// src/pages/Concienciacion.jsx
export default function Concienciacion() {
  return (
    <div className="min-h-screen bg-gradient-to-b from-white to-blue-50 py-12 px-4">
      <div className="max-w-4xl mx-auto bg-white shadow-lg rounded-2xl p-8">
        <h2 className="text-4xl font-extrabold text-center text-blue-900 mb-8">Concienciación y Educación</h2>

        <p className="text-lg text-gray-700 mb-4">
          La pobreza en México es un problema que afecta a millones de niños y niñas, obligándolos a trabajar en condiciones de explotación laboral. 
          Esta situación impide que accedan a una educación de calidad y limita sus oportunidades.
        </p>
        <p className="text-lg text-gray-700 mb-8">
          Nuestra meta es sensibilizar a la sociedad sobre esta realidad y promover acciones que protejan los derechos de la infancia. 
          Aquí encontrarás datos, recursos educativos y testimonios que invitan a la reflexión.
        </p>

        <section className="mb-10">
          <h3 className="text-2xl font-bold text-blue-800 mb-4">¿Qué es el trabajo infantil?</h3>
          <p className="text-gray-700 text-lg mb-4">
            El trabajo infantil es cualquier actividad económica o de supervivencia que obliga a un niño o niña a abandonar su educación, 
            asumir responsabilidades para adultos o exponerse a riesgos físicos y emocionales.
            Este tipo de trabajo les roba su infancia, limita su desarrollo y vulnera sus derechos.
          </p>
          <p className="text-gray-700 text-lg">
            No todo trabajo realizado por menores es considerado explotación. Ayudar en casa o aprender un oficio de forma segura y voluntaria puede formar parte de su crecimiento. 
            Pero cuando el trabajo interfiere con su educación, descanso o bienestar, estamos ante una forma de violencia silenciosa.
          </p>
        </section>

        <section className="mb-10">
          <h3 className="text-2xl font-bold text-blue-800 mb-4">¿Por qué ocurre el trabajo infantil?</h3>
          <ul className="list-disc list-inside text-gray-700 text-lg space-y-2">
            <li><strong>Pobreza extrema:</strong> Medio desesperado para sobrevivir.</li>
            <li><strong>Falta de acceso a educación:</strong> Escuelas lejanas o inaccesibles.</li>
            <li><strong>Débil aplicación de leyes:</strong> Las normas no siempre se cumplen.</li>
            <li><strong>Normalización cultural:</strong> Se ve como algo necesario o natural.</li>
            <li><strong>Violencia y abandono:</strong> Mayor exposición a la explotación.</li>
          </ul>
        </section>

        <section className="mb-10">
          <h3 className="text-2xl font-bold text-blue-800 mb-4">¿Cuáles son las consecuencias?</h3>
          <ul className="list-disc list-inside text-gray-700 text-lg space-y-2">
            <li><strong>Abandono escolar:</strong> Se perpetúa el ciclo de pobreza.</li>
            <li><strong>Daños físicos y emocionales:</strong> Maltratos, agotamiento y secuelas.</li>
            <li><strong>Explotación y abuso:</strong> Mayor riesgo en espacios no regulados.</li>
            <li><strong>Futuro limitado:</strong> Se repite el ciclo sin educación ni oportunidades.</li>
          </ul>
        </section>

        <section className="mb-10">
          <h3 className="text-2xl font-bold text-blue-800 mb-4">Historias reales</h3>
          <blockquote className="border-l-4 border-blue-600 pl-4 italic text-gray-700 mb-4">
            “Trabajo desde los 8 años vendiendo chicles. A veces no como en todo el día, pero ayudo a mi abuela.” – Niño de 10 años, Veracruz.
          </blockquote>
          <blockquote className="border-l-4 border-blue-600 pl-4 italic text-gray-700">
            “Quiero volver a la escuela, pero tengo que cuidar a mis hermanos mientras mis papás trabajan.” – Niña de 12 años, Chiapas.
          </blockquote>
        </section>

        <section className="mb-10">
          <h3 className="text-2xl font-bold text-blue-800 mb-4">¿Qué dicen las leyes?</h3>
          <p className="text-gray-700 text-lg mb-2">Los niños y niñas tienen derecho a:</p>
          <ul className="list-disc list-inside text-gray-700 text-lg space-y-2 mb-4">
            <li>Educación gratuita y de calidad.</li>
            <li>Descanso, juego y protección.</li>
            <li>Estar libres de trabajo peligroso o forzado.</li>
          </ul>
          <p className="text-gray-700 text-lg">
            México es parte de la <strong>Convención sobre los Derechos del Niño</strong> y cuenta con leyes que prohíben el trabajo infantil 
            en condiciones peligrosas o a edades inapropiadas. Sin embargo, estas leyes no siempre se respetan.
          </p>
        </section>

        <section className="mb-10">
          <h3 className="text-2xl font-bold text-blue-800 mb-4">¿Qué puedo hacer yo?</h3>
          <ul className="list-disc list-inside text-gray-700 text-lg space-y-2">
            <li>Infórmate y comparte esta información.</li>
            <li>Denuncia si conoces un caso de explotación infantil.</li>
            <li>Apoya iniciativas que promueven la educación y el bienestar infantil.</li>
            <li>No consumas productos que puedan estar hechos con mano de obra infantil.</li>
          </ul>
        </section>

        <section className="text-center">
          <h3 className="text-2xl font-bold text-blue-800 mb-4">Acciones concretas</h3>
          <div className="space-y-2">
            <a href="/mapa" className="text-blue-700 underline block hover:text-blue-900">📍 Ver Mapa de Casos</a>
            <a href="/denuncia" className="text-blue-700 underline block hover:text-blue-900">🚨 Hacer una Denuncia Anónima</a>
            <a href="/recursos" className="text-blue-700 underline block hover:text-blue-900">🧰 Ver Recursos de Apoyo</a>
          </div>
        </section>
      </div>
    </div>
  );
}
