package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StudentDAO {
    private DAOFactory daoFactory = DAOFactory.getInstance();
    private DataReader dataReader = DataReader.getInstance();

    public StudentDAO() throws DAOException {
    }

    public void insertStudents(List<Student> students) throws DAOException {
        String script = dataReader.readData("insertStudents.sql").get(0);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
            for (Student student: students) {
                statement.setString(1, student.getFirstName());
                statement.setString(2, student.getLastName());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                for(Student student : students) {
                    if(generatedKeys.next()) {
                        student.setId(generatedKeys.getInt(1));
                    }
                }
            }
            insertToGroup(students);
        } catch (SQLException e) {
            throw new DAOException("Cannot insert list of students:", e);
        }
    }

    private void insertToGroup(List<Student> students) throws DAOException {
        String script = dataReader.readData("insertStudentsToGroup.sql").get(0);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
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
        String script = dataReader.readData("insertStudents.sql").get(0);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                }
            }
            List<Student> students = Collections.singletonList(student);
            insertToGroup(students);
        } catch (SQLException e) {
            throw new DAOException("Cannot insert student:", e);
        }
    }

    public List<Student> getAllStudents() throws DAOException {
        String script = dataReader.readData("getAllStudents.sql").get(0);
        List<Student> result = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script);
             ResultSet resultSet = statement.executeQuery()) {
            result = processStudentsSet(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Cannot get all students:", e);
        }
        return result;
    }

    public List<Student> getByCourseName(String courseName) throws DAOException {
        String script = dataReader.readData("getStudentsByCourseName.sql").get(0);
        List<Student> result = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
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
        String script = dataReader.readData("deleteStudent.sql").get(0);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setInt(1, student.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Cannot delete student:", e);
        }
    }

    public void assignToCourses(Map<Student, List<Course>> assignMap) throws DAOException {
        String script = dataReader.readData("assignToCourse.sql").get(0);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            for(Map.Entry<Student, List<Course>> entry : assignMap.entrySet()) {
                Student student = entry.getKey();
                for(Course course : entry.getValue()) {
                    statement.setInt(1, student.getID());
                    statement.setInt(2, course.getId());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Cannot assign students to courses:", e);
        }
    }

    public boolean assignToCourse(Student student, Course course) throws DAOException {
        String script = dataReader.readData("assignToCourse.sql").get(0);
        boolean done = false;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            List<Integer> coursesID = this.getStudentsAssignments(student);
            if(coursesID != null && !coursesID.contains(course.getId())) {
                statement.setInt(1, student.getID());
                statement.setInt(2, course.getId());
                statement.executeUpdate();
                done = true;
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot assign student to course:", e);
        }
        return done;
    }

    private List<Integer> getStudentsAssignments(Student student) throws DAOException {
        String script = dataReader.readData("getAssignments.sql").get(0);
        List<Integer> result = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script);) {
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
        String script = dataReader.readData("deleteFromCourse.sql").get(0);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setInt(1, student.getID());
            statement.setInt(2, course.getId());
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
