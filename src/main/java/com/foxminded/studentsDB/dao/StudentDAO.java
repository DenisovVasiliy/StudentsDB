package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Student;
import com.foxminded.studentsDB.dao.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudentDAO {
    private DAOFactory daoFactory = DAOFactory.getInstance();
    private DataReader dataReader = DataReader.getInstance();

    public StudentDAO() throws DAOException {
    }

    public void insertStudents(List<Student> students) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.INSERT_STUDENT_FILE);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
            for (Student student : students) {
                statement.setString(1, student.getFirstName());
                statement.setString(2, student.getLastName());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                for (Student student : students) {
                    if (generatedKeys.next()) {
                        student.setId(generatedKeys.getInt(1));
                    }
                }
            }
            insertToGroup(students);
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_INSERT_STUDENTS_LIST, e);
        }
    }

    private void insertToGroup(List<Student> students) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.INSERT_TO_GROUP_FILE);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            for (Student student : students) {
                if (student.getGroupId() > 0) {
                    statement.setInt(1, student.getId());
                    statement.setInt(2, student.getGroupId());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_INSERT_STUDENTS_LIST, e);
        }
    }

    public void insertStudent(Student student) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.INSERT_STUDENT_FILE);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, student.getFirstName());
            statement.setString(2, student.getLastName());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                }
            }
            List<Student> students = Collections.singletonList(student);
            insertToGroup(students);
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_INSERT_STUDENT, e);
        }
    }

    public List<Student> getAllStudents() throws DAOException {
        String script = dataReader.getQuery(QueryConstants.GET_STUDENTS_FILE);
        List<Student> result = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script);
             ResultSet resultSet = statement.executeQuery()) {
            result = processStudentsSet(resultSet);
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_GET_ALL_STUDENTS, e);
        }
        return result;
    }

    public Student getStudentById(int id) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.GET_STUDENT_BY_ID);
        List<Student> result = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                result = processStudentsSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_GET_STUDENT_BY_ID, e);
        }
        return result.get(0);
    }

    public List<Student> getByCourseName(String courseName) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.GET_STUDENT_BY_COURSE_FILE);
        List<Student> result = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setString(1, courseName);
            try (ResultSet resultSet = statement.executeQuery()) {
                result = processStudentsSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_GET_STUDENTS_BY_COURSE, e);
        }
        return result;
    }

    public void deleteStudent(Student student) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.DELETE_STUDENT_FILE);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setInt(1, student.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_DELETE_STUDENT, e);
        }
    }

    public void assignToCourses(Map<Student, Set<Course>> assignMap) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.ASSIGN_TO_COURSE_FILE);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            for (Map.Entry<Student, Set<Course>> entry : assignMap.entrySet()) {
                Student student = entry.getKey();
                List<Integer> coursesId = this.getStudentsAssignments(student);
                for (Course course : entry.getValue()) {
                    if (!coursesId.contains(course.getId())) {
                        statement.setInt(1, student.getId());
                        statement.setInt(2, course.getId());
                        statement.addBatch();
                    }
                }
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_ASSIGN_TO_COURSES, e);
        }
    }

    public boolean assignToCourse(Student student, Course course) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.ASSIGN_TO_COURSE_FILE);
        boolean done = false;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            List<Integer> coursesId = this.getStudentsAssignments(student);
            if (!coursesId.contains(course.getId())) {
                statement.setInt(1, student.getId());
                statement.setInt(2, course.getId());
                statement.executeUpdate();
                done = true;
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_ASSIGN_TO_COURSE, e);
        }
        return done;
    }

    private List<Integer> getStudentsAssignments(Student student) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.GET_ASSIGNMENTS_FILE);
        List<Integer> result = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script);) {
            statement.setInt(1, student.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                result = processAssignmentsSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_GET_ASSIGNMENTS, e);
        }
        return result;
    }

    public void deleteFromCourse(Student student, Course course) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.DELETE_FROM_COURSE_FILE);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_DELETE_FROM_COURSE, e);
        }
    }

    private List<Student> processStudentsSet(ResultSet resultSet) throws DAOException {
        List<Student> result = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Student student =
                        new Student(resultSet.getInt("id"), resultSet.getString("first_name"),
                                resultSet.getString("last_name"));
                student.setGroupId(resultSet.getInt("group_id"));
                result.add(student);
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_PROCESS_STUDENTS_SET, e);
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
            throw new DAOException(MessagesConstantsDAO.CANNOT_PROCESS_ASSIGNMENTS_SET, e);
        }
        return result;
    }
}
