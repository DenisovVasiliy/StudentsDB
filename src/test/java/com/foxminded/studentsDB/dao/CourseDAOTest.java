package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.infra.DAOFactory;
import com.foxminded.studentsDB.dao.infra.ScriptExecutor;
import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseDAOTest {
    private static List<Course> testCourseList = new ArrayList<>();
    private static DAOFactory daoFactory;


    @BeforeAll
    public static void prepare() throws DAOException {
        daoFactory = DAOFactory.getInstance("TestDatabaseH2.properties");
        for (int i = 0; i < 2; i++) {
            testCourseList.add(new Course("TestCourse-" + (i + 1), "Course for testing."));
        }
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

    @Test
    public void shouldGetCourseById() throws DAOException {
        CourseDAO courseDAO = new CourseDAO();
        courseDAO.insertCourses(testCourseList);
        Course actualCourse = courseDAO.getCourseById(testCourseList.get(1).getId());
        assertEquals(testCourseList.get(1), actualCourse);
    }
}