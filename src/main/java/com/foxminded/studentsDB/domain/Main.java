package com.foxminded.studentsDB.domain;

import com.foxminded.studentsDB.dao.DAOException;
import com.foxminded.studentsDB.dao.ScriptExecutor;

import java.io.File;
import java.net.URL;

public class Main {
    private static final String CREATE_TABLES = "CreateTables.sql";
    private static final String FILE_NOT_FOUND_MESSAGE = "File is not found: ";

    public static void main(String[] args) throws DAOException {
        ScriptExecutor scriptExecutor = new ScriptExecutor();
        Main main = new Main();
        File createTables = main.getFileFromResources(CREATE_TABLES);
        scriptExecutor.executeScript(createTables);
    }

    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if(resource == null) {
            throw new IllegalArgumentException(FILE_NOT_FOUND_MESSAGE);
        } else return new File(resource.getFile());
    }
}
