import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

const CoursesPage = () => {
  const [courses, setCourses] = useState([]);

  useEffect(() => {
    axios.get('/api/courses')
      .then(res => setCourses(res.data))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-4">Cursos Disponibles</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {courses.map(course => (
          <div key={course.id} className="border p-4 rounded shadow">
            <h2 className="text-xl font-semibold">{course.titulo}</h2>
            <p className="text-gray-600">{course.descripcion}</p>
            <Link
              to={`/course/${course.id}`}
              className="text-blue-500 hover:underline mt-2 block"
            >
              Ver curso
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
};

export default CoursesPage;
