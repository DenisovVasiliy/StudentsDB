SELECT students.id, students.first_name, students.last_name, students_groups.group_id FROM students
LEFT JOIN students_groups on students_groups.student_id = students.id WHERE students.id = ?;