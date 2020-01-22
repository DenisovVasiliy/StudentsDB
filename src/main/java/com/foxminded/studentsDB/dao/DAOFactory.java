package com.foxminded.studentsDB.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOFactory {
    private static DAOFactory instance;
    private String url;
    private String user;
    private String password;

    private DAOFactory(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static synchronized DAOFactory getInstance() {
        if(instance == null) {
            instance = new DAOFactory("jdbc:postgresql://localhost:5432/foxUniversity",
                    "foxUser", "5825");
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
