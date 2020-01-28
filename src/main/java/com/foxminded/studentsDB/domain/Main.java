package com.foxminded.studentsDB.domain;

import com.foxminded.studentsDB.dao.DAOException;
import com.foxminded.studentsDB.dao.ScriptExecutor;

public class Main {
    private static final String CREATE_TABLES = "CreateTables.sql";

    public static void main(String[] args) throws DAOException {
        ScriptExecutor scriptExecutor = new ScriptExecutor();

        scriptExecutor.executeScript(CREATE_TABLES);
    }
}
