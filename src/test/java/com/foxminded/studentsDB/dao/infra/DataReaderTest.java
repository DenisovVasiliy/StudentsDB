package com.foxminded.studentsDB.dao.infra;

import com.foxminded.studentsDB.dao.QueryConstants;
import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.exceptions.MessagesConstantsDAO;
import com.foxminded.studentsDB.domain.Course;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class DataReaderTest {

    private DataReader dataReader = DataReader.getInstance();

    private static final String TEST_SCRIPTS = "TestScripts.sql";
    private static final String TEST_NAMES = "TestNames.txt";
    private static final String TEST_COURSES = "TestCourses.txt";
    private static final String TEST_ACCESS_WITH_PASSWORD = "TestDatabase.properties";
    private static final String TEST_ACCESS_WITHOUT_PASSWORD = "TestDatabaseH2.properties";
    private final DatabaseAccess ACCESS_WITH_PASSWORD = new DatabaseAccess(
            "jdbc:postgresql://localhost:5432/test1",
            "foxUser", "5825"
    );
    private final DatabaseAccess ACCESS_WITHOUT_PASSWORD = new DatabaseAccess(
            "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            "sa", ""
    );
    private static String[] expectedScripts = new String[3];
    private final String QUERY = "INSERT INTO courses(name, description) VALUES(?, ?);";
    private static List<String> expectedNames = new ArrayList<>();
    private static List<Course> expectedCourses = new ArrayList<>();

    @BeforeAll
    public static void prepare() {
        for (int i = 0; i < 3; i++) {
            expectedNames.add("testName" + i);
            expectedScripts[i] = "Test script " + i;
            expectedCourses.add(new Course("Test Course " + i, "Course for testing"));
        }
    }

    @Test
    public void shouldThrowIAExceptionWhenNullStringPassed() {
        Throwable thrownFromGetAccessData = assertThrows(IllegalArgumentException.class,
                () -> dataReader.getAccessData(null));
        Throwable thrownFromGetScripts = assertThrows(IllegalArgumentException.class,
                () -> dataReader.getScripts(null));
        Throwable thrownFromGetQuery = assertThrows(IllegalArgumentException.class,
                () -> dataReader.getQuery(null));
        Throwable thrownFromGetNames = assertThrows(IllegalArgumentException.class,
                () -> dataReader.getNames(null));
        Throwable thrownFromGetCourses = assertThrows(IllegalArgumentException.class,
                () -> dataReader.getCourses(null));
        String actualFromGetAccessData = thrownFromGetAccessData.getMessage();
        String actualFromGetScripts = thrownFromGetScripts.getMessage();
        String actualFromGetQuery = thrownFromGetQuery.getMessage();
        String actualFromGetNames = thrownFromGetNames.getMessage();
        String actualFromGetCourses = thrownFromGetCourses.getMessage();
        assertEquals(MessagesConstantsDAO.FILE_NAME_NULL, actualFromGetAccessData);
        assertEquals(MessagesConstantsDAO.FILE_NAME_NULL, actualFromGetCourses);
        assertEquals(MessagesConstantsDAO.FILE_NAME_NULL, actualFromGetNames);
        assertEquals(MessagesConstantsDAO.FILE_NAME_NULL, actualFromGetQuery);
        assertEquals(MessagesConstantsDAO.FILE_NAME_NULL, actualFromGetScripts);
    }

    @Test
    public void shouldThrowDAOExceptionWhenPassedFileNotExists() {
        Throwable thrownFromGetAccessData = assertThrows(IllegalArgumentException.class,
                () -> dataReader.getAccessData("FileNotExists.txt"));
        Throwable thrownFromGetScripts = assertThrows(IllegalArgumentException.class,
                () -> dataReader.getScripts("FileNotExists.txt"));
        Throwable thrownFromGetQuery = assertThrows(IllegalArgumentException.class,
                () -> dataReader.getQuery("FileNotExists.txt"));
        Throwable thrownFromGetNames = assertThrows(IllegalArgumentException.class,
                () -> dataReader.getNames("FileNotExists.txt"));
        Throwable thrownFromGetCourses = assertThrows(IllegalArgumentException.class,
                () -> dataReader.getCourses("FileNotExists.txt"));
        String actualFromGetAccessData = thrownFromGetAccessData.getMessage();
        String actualFromGetScripts = thrownFromGetScripts.getMessage();
        String actualFromGetQuery = thrownFromGetQuery.getMessage();
        String actualFromGetNames = thrownFromGetNames.getMessage();
        String actualFromGetCourses = thrownFromGetCourses.getMessage();
        assertEquals(MessagesConstantsDAO.FILE_NOT_FOUND_MESSAGE + "FileNotExists.txt",
                actualFromGetAccessData);
        assertEquals(MessagesConstantsDAO.FILE_NOT_FOUND_MESSAGE + "FileNotExists.txt", actualFromGetCourses);
        assertEquals(MessagesConstantsDAO.FILE_NOT_FOUND_MESSAGE + "FileNotExists.txt", actualFromGetNames);
        assertEquals(MessagesConstantsDAO.FILE_NOT_FOUND_MESSAGE + "FileNotExists.txt", actualFromGetQuery);
        assertEquals(MessagesConstantsDAO.FILE_NOT_FOUND_MESSAGE + "FileNotExists.txt", actualFromGetScripts);
    }

    @Test
    public void shouldReturnAccessDataWithPassword() throws DAOException {
        DatabaseAccess actualAccess = dataReader.getAccessData(TEST_ACCESS_WITH_PASSWORD);
        assertEquals(ACCESS_WITH_PASSWORD, actualAccess);
    }

    @Test
    public void shouldReturnAccessDataWithoutPassword() throws DAOException {
        DatabaseAccess actualAccess = dataReader.getAccessData(TEST_ACCESS_WITHOUT_PASSWORD);
        assertEquals(ACCESS_WITHOUT_PASSWORD, actualAccess);
    }

    @Test
    public void shouldReturnScripts() throws DAOException {
        String[] actual = dataReader.getScripts(TEST_SCRIPTS);
        assertArrayEquals(expectedScripts, actual);
    }

    @Test
    public void shouldReturnQuery() throws DAOException {
        String actualQuery = dataReader.getQuery(QueryConstants.INSERT_COURSES);
        assertEquals(QUERY, actualQuery);
    }

    @Test
    public void shouldReturnNames() throws DAOException {
        List<String> actualNames = dataReader.getNames(TEST_NAMES);
        assertEquals(expectedNames, actualNames);
    }

    @Test
    public void shouldReturnCourses() throws DAOException {
        List<Course> actualCourses = dataReader.getCourses(TEST_COURSES);
        assertEquals(expectedCourses, actualCourses);
    }
}