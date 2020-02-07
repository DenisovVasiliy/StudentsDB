package com.foxminded.studentsDB.domain;

import com.foxminded.studentsDB.dao.DAOException;
import com.foxminded.studentsDB.dao.QueryConstants;
import com.foxminded.studentsDB.dao.ScriptExecutor;

import java.util.List;

public class Main {
    public static void main(String[] args) throws DAOException {
        ScriptExecutor scriptExecutor = new ScriptExecutor();

        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);
        DataCreator dataCreator = new DataCreator();
        TestData testData = dataCreator.createTestData();
    }
}
