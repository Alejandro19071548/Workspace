// src/pages/Concienciacion.jsx
import '../styles.css';

export default function Concienciacion() {
  return (
    <div className="concienciacion-wrapper">
      <div className="concienciacion-content">
        <h2 className="text-3xl font-bold mb-4 text-blue-900">Concienciación y Educación</h2>
       
        <p className="mb-4 text-lg text-gray-700">
        La pobreza en México es un problema que afecta a millones de niños y niñas, 
        obligándolos a trabajar en condiciones de explotación laboral. Esta situación 
        impide que accedan a una educación de calidad y limita sus oportunidades.
      </p>
      <p className="parrafo parrafo-espaciado">
        Nuestra meta es sensibilizar a la sociedad sobre esta realidad y promover acciones 
        que protejan los derechos de la infancia. Aquí encontrarás datos, recursos educativos, 
        y testimonios que invitan a la reflexión.
      </p>
      <figure style={{ textAlign: "center", margin: "20px 0" }}>
        <img 
          src="/niña.jpg" 
          alt="Niños trabajando" 
          style={{ maxWidth: "50%", height: "auto", borderRadius: "8px" }}
        />
        <figcaption style={{ fontSize: "0.9rem", color: "#555" }}>
          El trabajo a corta edad priva de derechos a las niñas y niños.
        </figcaption>
      </figure>
      <h2 className="titulo-secundario">¿Qué es el trabajo infantil?</h2>
      <p className="parrafo">
        El trabajo infantil es cualquier actividad económica o de supervivencia que obliga a un niño o niña a abandonar su educación, asumir responsabilidades para adultos o exponerse a riesgos físicos y emocionales.
        Este tipo de trabajo les roba su infancia, limita su desarrollo y vulnera sus derechos.
      </p>
      <p className="parrafo parrafo-espaciado">
        No todo trabajo realizado por menores es considerado explotación. Ayudar en casa o aprender un oficio de forma segura y voluntaria puede formar parte de su crecimiento. Pero cuando el trabajo interfiere con su educación, descanso o bienestar, estamos ante una forma de violencia silenciosa.
      </p>

      <h2 className="titulo-secundario">¿Por qué ocurre el trabajo infantil?</h2>
      <ul className="lista">
        <li><strong>Pobreza extrema:</strong> Muchas familias recurren al trabajo infantil como un medio desesperado para sobrevivir.</li>
        <li><strong>Falta de acceso a educación:</strong> En zonas marginadas, la escuela no siempre está cerca ni es gratuita en la práctica.</li>
        <li><strong>Débil aplicación de leyes:</strong> Aunque existen leyes contra el trabajo infantil, no siempre se aplican ni se vigilan.</li>
        <li><strong>Normalización cultural:</strong> En algunas comunidades, trabajar desde pequeño es visto como algo normal o necesario.</li>
        <li><strong>Violencia y abandono:</strong> Niños que viven situaciones de maltrato, abandono o violencia intrafamiliar están más expuestos a la explotación.</li>
      </ul>

      <h2 className="titulo-secundario">¿Cuáles son las consecuencias?</h2>
      <ul className="lista">
        <li><strong>Abandono escolar:</strong> Muchos niños no pueden terminar la escuela, lo que perpetúa el ciclo de pobreza.</li>
        <li><strong>Daños físicos y emocionales:</strong> Jornadas largas, carga pesada, calor extremo, maltratos o abuso emocional dejan secuelas duraderas.</li>
        <li><strong>Explotación y abuso:</strong> Niños que trabajan solos, en la calle o en espacios no regulados están en alto riesgo de abuso sexual o laboral.</li>
        <li><strong>Futuro limitado:</strong> Al crecer sin educación ni oportunidades, muchos de estos niños repiten el mismo ciclo en su adultez.</li>
      </ul>

    

      <h2 className="titulo-secundario">¿Qué dicen las leyes?</h2>
      <p className="parrafo">
        Los niños y niñas tienen derecho a:
      </p>
      <ul className="lista">
        <li>Educación gratuita y de calidad.</li>
        <li>Descanso, juego y protección.</li>
        <li>Estar libres de trabajo peligroso o forzado.</li>
      </ul>
      <p className="parrafo parrafo-espaciado">
        México es parte de la <strong>Convención sobre los Derechos del Niño</strong> y cuenta con leyes que prohíben el trabajo infantil en condiciones peligrosas o a edades inapropiadas. Sin embargo, estas leyes no siempre se respetan.
      </p>

      <h2 className="titulo-secundario">¿Qué puedo hacer yo?</h2>
      <ul className="lista">
        <li>Infórmate y comparte esta información.</li>
        <li>Denuncia si conoces un caso de explotación infantil.</li>
        <li>Apoya iniciativas que promueven la educación y el bienestar infantil.</li>
        <li>No consumas productos que puedan estar hechos con mano de obra infantil.</li>
      </ul>
    </div>
    </div>
  );
}
