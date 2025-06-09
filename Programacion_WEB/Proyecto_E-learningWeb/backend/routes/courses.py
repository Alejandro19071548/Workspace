from flask import Blueprint, request, jsonify
from db import mysql
from datetime import datetime

courses = Blueprint('courses', __name__)

# Obtener todos los cursos
@courses.route('/api/courses', methods=['GET'])
def get_courses():
    cur = mysql.connection.cursor()
    cur.execute("SELECT * FROM courses")
    result = cur.fetchall()
    column_names = [desc[0] for desc in cur.description]
    cur.close()

    courses_list = [dict(zip(column_names, row)) for row in result]
    return jsonify(courses_list)
# # Obtener el contenido de un curso por ID
# @courses.route('/api/course/<int:course_id>', methods=['GET'])
# def get_course_detail(course_id):
#     cur = mysql.connection.cursor()
#     # Info del curso
#     cur.execute("SELECT id, titulo, descripcion FROM courses WHERE id = %s", (course_id,))
#     course = cur.fetchone()
#     # Contenido del curso
#     cur.execute("SELECT id, titulo, contenido FROM course_contents WHERE curso_id = %s", (course_id,))
#     contents = cur.fetchall()
#     cur.close()

#     if not course:
#         return jsonify({'error': 'Curso no encontrado'}), 404

#     return jsonify({
#         'curso': course,
#         'contenidos': contents
#     })

# Marcar un curso como completado y generar certificado
@courses.route('/api/course/<int:course_id>/complete', methods=['POST'])
def complete_course(course_id):
    data = request.get_json()
    user_id = data.get('user_id')  # Esto vendría del frontend (temporalmente)

    if not user_id:
        return jsonify({'error': 'Falta user_id'}), 400

    cur = mysql.connection.cursor()
    # Verifica si ya existe
    cur.execute("SELECT id FROM user_courses WHERE user_id = %s AND course_id = %s", (user_id, course_id))
    existing = cur.fetchone()

    if existing:
        cur.execute("""
            UPDATE user_courses
            SET completed = 1, completion_date = %s
            WHERE id = %s
        """, (datetime.now(), existing[0]))
    else:
        cur.execute("""
            INSERT INTO user_courses (user_id, course_id, completed, completion_date)
            VALUES (%s, %s, 1, %s)
        """, (user_id, course_id, datetime.now()))

    mysql.connection.commit()
    cur.close()

    return jsonify({'message': 'Curso completado'})


@courses.route('/api/course/<int:course_id>', methods=['GET'])
def get_course_content(course_id):
    cur = mysql.connection.cursor()
    cur.execute("SELECT id, titulo, descripcion FROM courses WHERE id = %s", (course_id,))
    course = cur.fetchone()

    cur.execute("SELECT id, titulo, contenido FROM course_contents WHERE curso_id = %s", (course_id,))
    contents = cur.fetchall()
    cur.close()

    if not course:
        return jsonify({'error': 'Curso no encontrado'}), 404

    return jsonify({
        'id': course[0],
        'title': course[1],
        'description': course[2],
        'contents': [{'id': c[0], 'title': c[1], 'content': c[2]} for c in contents]
    })