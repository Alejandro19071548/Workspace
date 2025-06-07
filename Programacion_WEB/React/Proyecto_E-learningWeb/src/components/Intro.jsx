import './Intro.css';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function Home() {
  const [courses, setCourses] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios.get('http://localhost:5000/api/courses')
      .then((res) => setCourses(res.data))
      .catch((err) => console.error('Error al obtener cursos:', err));
  }, []);

  return (
    <div className="home-container">
      <main className="course-grid">
        {courses.map((course) => (
          <div key={course.id} className="course-card">
            <h2>{course.titulo}</h2>
            <p>{course.descripcion}</p>
            <Link to={`/course/${course.id}`}>
              <button className="btn" onClick={() => navigate(`/course/${course.id}`)}>
              Ver curso
            </button>
            </Link>
            
          </div>
        ))}
      </main>
    </div>
  );
}
