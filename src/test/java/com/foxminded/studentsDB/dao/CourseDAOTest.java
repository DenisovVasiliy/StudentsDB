package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.exceptions.MessagesConstantsDAO;
import com.foxminded.studentsDB.dao.infra.DAOFactory;
import com.foxminded.studentsDB.dao.infra.DataReader;
import com.foxminded.studentsDB.dao.infra.ScriptExecutor;
import com.foxminded.studentsDB.domain.Course;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseDAOTest {
    private Course course = new Course("Test Course", "Course for testing.");
    private List<Course> testCourseList = Collections.singletonList(course);
    private DataReader dataReader = DataReader.getInstance();
    private static DAOFactory daoFactory;


    @BeforeAll
    public static void prepare() throws DAOException {
        daoFactory = DAOFactory.getInstance("TestDatabaseH2.properties");
    }

    @BeforeEach
    public void createTables() throws DAOException {
        ScriptExecutor scriptExecutor = new ScriptExecutor();
        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);
    }

    @Test
    public void shouldInsertCoursesIntoDB() throws DAOException {
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = readCoursesFromDB();
        assertEquals(0, courses.size());

        courseDAO.insertCourses(testCourseList);

        List<Course> actualCourses = readCoursesFromDB();

        assertEquals(testCourseList, actualCourses);
    }

    private List<Course> readCoursesFromDB() throws DAOException {
        String script = dataReader.getQuery(QueryConstants.GET_ALL_COURSES);
        List<Course> actualCourses = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script);
             ResultSet resultSet = statement.executeQuery()) {
            actualCourses = processCoursesSet(resultSet);
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_GET_COURSES, e);
        }
        return actualCourses;
    }

    @Test
    public void shouldGetCoursesFromDB() throws DAOException {
        CourseDAO courseDAO = new CourseDAO();
        insertCoursesIntoDB();
        List<Course> actualCourses = courseDAO.getAllCourses();
        assertEquals(testCourseList, actualCourses);
    }

    private void insertCoursesIntoDB() throws DAOException {
        String script = dataReader.getQuery(QueryConstants.INSERT_COURSES);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
            for (Course course : testCourseList) {
                statement.setString(1, course.getName());
                statement.setString(2, course.getDescription());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                for (Course course : testCourseList) {
                    if (resultSet.next()) {
                        course.setId(resultSet.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_INSERT_COURSES, e);
        }
    }


    private List<Course> processCoursesSet(ResultSet resultSet) throws DAOException {
        List<Course> courses = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Course course = new Course(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"));
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_PROCESS_COURSES_SET, e);
        }
        return courses;
    }
}