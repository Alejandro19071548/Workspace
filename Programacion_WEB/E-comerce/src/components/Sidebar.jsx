export default function Sidebar() {
  return (
    <aside className="sidebar">
      <h3>Noticias recientes</h3>
      <ul className="noticias">
        <li>
          <a href="https://www.unicef.org/mexico/comunicados-prensa/un-buen-sexenio-para-la-niñez-méxico-cuenta-con-unicef-para-erradicar-la-pobreza" target="_blank" rel="noopener noreferrer">
            UNICEF: Pobreza infantil y adolescente en México
          </a>
        </li>
        <li>
          <a href="https://www.unicef.org/mexico/informes/normas-mínimas-para-la-protección-de-la-niñez-y-adolescencia-en-la-acción-humanitaria/" target="_blank" rel="noopener noreferrer">
            Reportaje sobre trabajo infantil en zonas rurales
          </a>
        </li>
        <li>
          <a href="https://mexico.unfpa.org/es/publications" target="_blank" rel="noopener noreferrer">
            Normas mínimas para la protección de la niñez y adolescencia en la acción humanitaria
          </a>
        </li>
        <li>
          <a href="https://www.gob.mx/inafed/articulos/contribuyamos-a-erradicar-el-trabajo-infantil-en-nuestros-municipios" target="_blank" rel="noopener noreferrer">
            Contribuyamos a erradicar el trabajo infantil en nuestros municipios
          </a>
        </li>
      </ul>

      <h3>Videos informativos</h3>
      <div className="videos">
        <div className="video">
          <iframe
            width="100%"
            height="200"
            src="https://www.youtube.com/embed/Jg2aBzBRGYE?si=_wddb5LAid7eR-d2"
            title="Video sobre pobreza infantil"
            frameBorder="0"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowFullScreen
          ></iframe>
        </div>

        <div className="video">
          <iframe
            width="100%"
            height="200"
            src="https://www.youtube.com/embed/L3bhGEDtKeY?si=so3HtPUoLGyXxg-8"
            title="Trabajo infantil en Latinoamérica"
            frameBorder="0"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowFullScreen
          ></iframe>
        </div>
        <div className="video">
          <iframe
            width="100%"
            height="200"
            src="https://www.youtube.com/embed/ohErvz6v-eM?si=YTQw5toFPs3yeRi3"
            title="Trabajo infantil en Latinoamérica"
            frameBorder="0"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowFullScreen
          ></iframe>
          <div className="video">
          <iframe
            width="100%"
            height="200"
            src="https://www.youtube.com/embed/3InQ8ubd-BE?si=5LVVOWhKnTJF1_1R"
            title="Trabajo infantil en Latinoamérica"
            frameBorder="0"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowFullScreen
          ></iframe>
        </div>
        <div className="video">
          <iframe
            width="100%"
            height="200"
            src="https://www.youtube.com/embed/D9nagsS2ARU?si=veuQYtmaNxg828I7"
            title="Trabajo infantil en Latinoamérica"
            frameBorder="0"
            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
            allowFullScreen
          ></iframe>
        </div>
          
        </div>
      </div>
    </aside>
  );
}
