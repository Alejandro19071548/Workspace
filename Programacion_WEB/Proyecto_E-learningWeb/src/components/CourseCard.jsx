import React from "react";
import { useNavigate } from "react-router-dom";

const CourseCard = ({ course }) => {
  const navigate = useNavigate();
  return (
    <div className="p-4 border rounded-xl shadow hover:bg-gray-100 cursor-pointer" onClick={() => navigate(`/course/${course.id}`)}>
      <h2 className="text-lg font-semibold">{course.titulo}</h2>
      <p>{course.descripcion}</p>
    </div>
  );
};

export default CourseCard;