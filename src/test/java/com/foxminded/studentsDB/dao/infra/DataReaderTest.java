package com.foxminded.studentsDB.dao.infra;

import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.exceptions.MessagesConstantsDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataReaderTest {

    private DataReader dataReader = DataReader.getInstance();

    private static final String ERROR_MESSAGE = "File's name shouldn't be null!";

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
        assertEquals(ERROR_MESSAGE, actualFromGetAccessData);
        assertEquals(ERROR_MESSAGE, actualFromGetCourses);
        assertEquals(ERROR_MESSAGE, actualFromGetNames);
        assertEquals(ERROR_MESSAGE, actualFromGetQuery);
        assertEquals(ERROR_MESSAGE, actualFromGetScripts);
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
}