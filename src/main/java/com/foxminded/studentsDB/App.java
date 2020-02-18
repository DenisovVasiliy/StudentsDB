package com.foxminded.studentsDB;

import com.foxminded.studentsDB.dao.DAOException;
import com.foxminded.studentsDB.dao.QueryConstants;
import com.foxminded.studentsDB.dao.ScriptExecutor;
import com.foxminded.studentsDB.domain.DataCreator;
import com.foxminded.studentsDB.ui.ConsoleListener;
import com.foxminded.studentsDB.ui.Listener;

public class App {
    public static void main(String[] args) throws DAOException {
        ScriptExecutor scriptExecutor = new ScriptExecutor();

        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);

        DataCreator dataCreator = new DataCreator();
        dataCreator.createTestData();
        Listener listener = new ConsoleListener();
        listener.listen();
    }
}
