package com.foxminded.studentsDB.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

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

    public static synchronized DAOFactory getInstance() throws DAOException {
        if(instance == null) {
            DataReader dataReader = DataReader.getInstance();
            List<String> properties = dataReader.readData(
                    new File("G:\\Java Learn\\StudentsDB\\src\\main\\resources\\database.properties"));
            instance = new DAOFactory(properties.get(0),
                    properties.get(1), properties.get(2));
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
