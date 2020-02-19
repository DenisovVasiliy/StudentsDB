package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.dao.exceptions.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOFactory {
    private static DAOFactory instance;
    private String url;
    private String user;
    private String password;

    private DAOFactory(DatabaseAccess access) {
        this.url = access.getUrl();
        this.user = access.getUser();
        this.password = access.getPassword();
    }

    public static synchronized DAOFactory getInstance() throws DAOException {
        if(instance == null) {
            DataReader dataReader = DataReader.getInstance();
            DatabaseAccess access = dataReader.getAccessData("database.properties");
            instance = new DAOFactory(access);
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
