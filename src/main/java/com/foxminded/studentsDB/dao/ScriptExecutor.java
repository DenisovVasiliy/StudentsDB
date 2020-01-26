package com.foxminded.studentsDB.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class ScriptExecutor {
    private static final String CANNOT_EXECUTE_SCRIPTS = "Cannot execute scripts:";
    public void executeScript(String fileName) throws DAOException {
        DataReader dataReader = DataReader.getInstance();
        execute(dataReader.readData(fileName));
    }

    private void execute(List<String> scripts) throws DAOException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        String[] script = buildScript(scripts);
        try (Connection connection = daoFactory.getConnection();
             Statement statement = connection.createStatement()) {
            for(String line : script) {
                statement.addBatch(line);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException(CANNOT_EXECUTE_SCRIPTS, e);
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
