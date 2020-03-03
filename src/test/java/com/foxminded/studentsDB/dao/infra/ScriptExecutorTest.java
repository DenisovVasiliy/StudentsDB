package com.foxminded.studentsDB.dao.infra;

import com.foxminded.studentsDB.dao.QueryConstants;
import com.foxminded.studentsDB.dao.exceptions.DAOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ScriptExecutorTest {
    private static DAOFactory daoFactory;
    private ScriptExecutor scriptExecutor = new ScriptExecutor();
    private final String EXCEPTION_MESSAGE = "Cannot check table for existence.";

    @BeforeAll
    public static void prepare() throws DAOException {
        daoFactory = DAOFactory.getInstance("TestDatabaseH2.properties");
    }

    @Test
    public void shouldReturnTrueIfStudentsTableWasCreated() throws DAOException {
        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);
        assertTrue(checkTableForExistence("STUDENTS"));
    }

    @Test
    public void shouldReturnTrueIfGroupsTableWasCreated() throws DAOException {
        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);
        assertTrue(checkTableForExistence("GROUPS"));
    }

    @Test
    public void shouldReturnTrueIfCoursesTableWasCreated() throws DAOException {
        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);
        assertTrue(checkTableForExistence("COURSES"));
    }

    @Test
    public void shouldReturnTrueIfStudentsGroupsTableWasCreated() throws DAOException {
        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);
        assertTrue(checkTableForExistence("STUDENTS_GROUPS"));
    }

    @Test
    public void shouldReturnTrueIfStudentsCoursesTableWasCreated() throws DAOException {
        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);
        assertTrue(checkTableForExistence("STUDENTS_COURSES"));
    }

    private boolean checkTableForExistence(String tableName) throws DAOException {
        try (Connection connection = daoFactory.getConnection();
             ResultSet resultSet = connection.getMetaData().getTables(
                     null, null, tableName, null)) {
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new DAOException(EXCEPTION_MESSAGE, e);
        }
        return false;
    }
}