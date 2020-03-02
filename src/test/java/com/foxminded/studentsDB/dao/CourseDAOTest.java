package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.infra.DAOFactory;
import com.foxminded.studentsDB.dao.infra.ScriptExecutor;
import com.foxminded.studentsDB.domain.Course;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseDAOTest {
    private Course course = new Course("Test Course", "Course for testing.");
    private List<Course> testCourseList = Collections.singletonList(course);
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
        List<Course> courses = courseDAO.getAllCourses();
        assertEquals(0, courses.size());

        courseDAO.insertCourses(testCourseList);

        List<Course> actualCourses = courseDAO.getAllCourses();

        assertEquals(testCourseList, actualCourses);
    }

    @Test
    public void shouldGetCoursesFromDB() throws DAOException {
        CourseDAO courseDAO = new CourseDAO();
        courseDAO.insertCourses(testCourseList);
        List<Course> actualCourses = courseDAO.getAllCourses();
        assertEquals(testCourseList, actualCourses);
    }
}