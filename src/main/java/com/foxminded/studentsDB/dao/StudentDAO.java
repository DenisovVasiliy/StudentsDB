package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Student;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentDAO {
    private static final String GET_ALL =
            "SELECT students.id, students.first_name, students.last_name, students_groups.group_id FROM students " +
                    "LEFT JOIN students_groups on students_groups.student_id = students.id;";
    private static final String INSERT = "INSERT INTO students(first_name, last_name) VALUES(?, ?);";
    private static final String INSERT_TO_GROUP = "INSERT INTO students_groups(student_id, group_id) VALUES(?, ?);";
    private static final String GET_BY_COURSE_NAME =
            "SELECT students.id, students.first_name, students.last_name, students_groups.group_id " +
            "FROM students INNER JOIN students_courses ON students.id = students_courses.student_id " +
            "LEFT JOIN students_groups ON students_groups.student_id = students.id " +
            "INNER JOIN courses ON courses.id = students_courses.course_id WHERE courses.name = ?;";
    private static final String DELETE_STUDENT = "DELETE FROM students WHERE id = ?;";
    private static final String ASSIGN_TO_COURSE = "INSERT INTO students_courses(student_id, course_id) VALUES(?, ?);";
    private static final String DELETE_FROM_COURSE = "DELETE FROM students_courses " +
            "WHERE student_id = ? AND course_id = ?;";
    private static final String GET_ASSIGNMENTS_BY_STUDENT_ID = "SELECT course_id FROM students_courses " +
            "WHERE student_id = ?;";

    private DAOFactory daoFactory = DAOFactory.getInstance();

    public StudentDAO() throws DAOException {
    }

    public void insertStudents(List<Student> students) throws DAOException {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (Student student: students) {
                statement.setString(1, student.getFirstName());
                statement.setString(2, student.getLastName());
                statement.addBatch();
            }
            statement.executeBatch();
            insertToGroup(students);
        } catch (SQLException e) {
            throw new DAOException("Cannot insert list of students:", e);
        }
    }

    private void insertToGroup(List<Student> students) throws DAOException {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_TO_GROUP)) {
            for (Student student: students) {
                if(student.getGroupID() > 0) {
                    statement.setInt(1, student.getID());
                    statement.setInt(2, student.getGroupID());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Cannot insert list of students:", e);
        }
    }

    public void insertStudent(Student student) throws DAOException {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_TO_GROUP)) {
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.executeUpdate();
            List<Student> students = new ArrayList<>();
            students.add(student);
            insertToGroup(students);
        } catch (SQLException e) {
            throw new DAOException("Cannot insert student:", e);
        }
    }

    public List<Student> getAllStudents() throws DAOException {
        List<Student> result = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            result = processStudentsSet(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Cannot get all students:", e);
        }
        return result;
    }

    public List<Student> getByCourseName(String courseName) throws DAOException {
        List<Student> result = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_COURSE_NAME)) {
            statement.setString(1, courseName);
            try (ResultSet resultSet = statement.executeQuery()) {
                result = processStudentsSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot get students by course name:", e);
        }
        return result;
    }

    public void deleteStudent(Student student) throws DAOException {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT)) {
            statement.setInt(1, student.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Cannot delete student:", e);
        }
    }

    public void assignToCourses(Map<Student, List<Course>> assignMap) throws DAOException {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(ASSIGN_TO_COURSE)) {
            for(Map.Entry<Student, List<Course>> entry : assignMap.entrySet()) {
                Student student = entry.getKey();
                for(Course course : entry.getValue()) {
                    statement.setInt(1, student.getID());
                    statement.setInt(2, course.getID());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Cannot assign students to courses:", e);
        }
    }

    public boolean assignToCourse(Student student, Course course) throws DAOException {
        boolean done = false;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(ASSIGN_TO_COURSE)) {
            List<Integer> coursesID = this.getStudentsAssignments(student);
            if(coursesID != null && !coursesID.contains(course.getID())) {
                statement.setInt(1, student.getID());
                statement.setInt(2, course.getID());
                statement.executeUpdate();
                done = true;
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot assign student to course:", e);
        }
        return done;
    }

    private List<Integer> getStudentsAssignments(Student student) throws DAOException {
        List<Integer> result = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ASSIGNMENTS_BY_STUDENT_ID);) {
            statement.setInt(1, student.getID());
            try (ResultSet resultSet = statement.executeQuery()) {
                result = processAssignmentsSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot get student's assignments:", e);
        }
        return result;
    }

    public void deleteFromCourse(Student student, Course course) throws DAOException {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_FROM_COURSE)) {
            statement.setInt(1, student.getID());
            statement.setInt(2, course.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Cannot delete student from course:", e);
        }
    }

    private List<Student> processStudentsSet(ResultSet resultSet) throws DAOException {
        List<Student> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Student student =
                        new Student(resultSet.getInt("id"), resultSet.getString("first_name"),
                                resultSet.getString("last_name"));
                student.setGroupID(resultSet.getInt("group_id"));
                result.add(student);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot process set of students:", e);
        }
        return result;
    }

    private List<Integer> processAssignmentsSet(ResultSet resultSet) throws DAOException {
        List<Integer> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Integer courseID = resultSet.getInt("course_id");
                result.add(courseID);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot process set of assignments:", e);
        }
        return result;
    }
}
