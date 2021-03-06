package com.foxminded.studentsDB.dao.infra;

import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.exceptions.MessagesConstantsDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ScriptExecutor {
    public void executeScript(String fileName) throws DAOException {
        DataReader dataReader = DataReader.getInstance();
        execute(dataReader.getScripts(fileName));
    }

    private void execute(String[] scripts) throws DAOException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        try (Connection connection = daoFactory.getConnection();
             Statement statement = connection.createStatement()) {
            for (String line : scripts) {
                statement.addBatch(line);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_EXECUTE_SCRIPTS, e);
        }
    }
}
