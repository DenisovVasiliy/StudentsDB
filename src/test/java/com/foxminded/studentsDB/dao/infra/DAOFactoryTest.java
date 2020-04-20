package com.foxminded.studentsDB.dao.infra;

import com.foxminded.studentsDB.dao.exceptions.DAOException;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DAOFactoryTest {


    @Test
    void getConnection() throws DAOException, SQLException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        try (Connection connection = daoFactory.getConnection()) {
            assertTrue(connection.isValid(1));
            assertFalse(connection.isClosed());
        }
    }

    @Test
    void getTestConnection() throws DAOException, SQLException {
        DAOFactory daoFactory = DAOFactory.getInstance("TestDatabaseH2.properties");
        try (Connection connection = daoFactory.getConnection()) {
            assertTrue(connection.isValid(1));
            assertFalse(connection.isClosed());
        }
    }
}