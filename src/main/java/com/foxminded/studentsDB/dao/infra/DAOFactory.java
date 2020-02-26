package com.foxminded.studentsDB.dao.infra;

import com.foxminded.studentsDB.dao.exceptions.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOFactory {
    private static DAOFactory instance;
    private DatabaseAccess access;

    private DAOFactory(DatabaseAccess access) {
        this.access = access;
    }

    public static synchronized DAOFactory getInstance() throws DAOException {
        if(instance == null) {
            DataReader dataReader = DataReader.getInstance();
            DatabaseAccess access = dataReader.getAccessData("Database.properties");
            instance = new DAOFactory(access);
        }
        return instance;
    }

    public static synchronized DAOFactory getInstance(String properties) throws DAOException {
        if(instance == null) {
            DataReader dataReader = DataReader.getInstance();
            DatabaseAccess access = dataReader.getAccessData(properties);
            instance = new DAOFactory(access);
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(access.getUrl(), access.getUser(), access.getPassword());
    }
}
