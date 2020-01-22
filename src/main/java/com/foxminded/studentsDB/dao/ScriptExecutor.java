package com.foxminded.studentsDB.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ScriptExecutor {
    private static final String CANNOT_EXECUTE_SCRIPTS = "Cannot execute scripts:";
    public void executeScript(File file) throws DAOException {
        DataReader dataReader = DataReader.getInstance();
        execute(dataReader.readData(file));
    }

    private void execute(List<String> scripts) throws DAOException {
        Connection connection = null;
        Statement statement = null;
        String[] script = buildScript(scripts);
        try {
            DAOFactory daoFactory = DAOFactory.getInstance();
            connection = daoFactory.getConnection();
            statement = connection.createStatement();
            for(String line : script) {
                statement.addBatch(line);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException(CANNOT_EXECUTE_SCRIPTS, e);
        } finally {
            try {
                if(statement != null) {
                    statement.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] buildScript(List<String> list) {
        StringBuilder script = new StringBuilder();
        for(String line : list) {
            script.append(line);
        }
        return script.toString().split(";");
    }
}
