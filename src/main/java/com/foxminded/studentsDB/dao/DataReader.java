package com.foxminded.studentsDB.dao;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.nio.file.Files.lines;
import static java.nio.file.Paths.get;

public class DataReader {
    private static DataReader instance;
    private File file;

    private DataReader() {
    }

    public static synchronized DataReader getInstance() {
        if(instance == null) {
            instance = new DataReader();
        }
        return instance;
    }

    public DatabaseAccess getAccessData(String fileName) throws DAOException {
        file = getFileFromResources(fileName);
        checkFile();
        List<String> data =  getData();
        return new DatabaseAccess(data.get(0), data.get(1), data.get(2));
    }

    public String[] getScripts(String fileName) throws DAOException {
        file = getFileFromResources(fileName);
        checkFile();
        return buildScripts(getData());
    }

    public String getQuery(String fileName) throws DAOException {
        file = getFileFromResources(fileName);
        checkFile();
        return getData().get(0);
    }

    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if(resource == null) {
            ErrorMessenger messenger = ErrorMessenger.getInstance();
            throw new IllegalArgumentException(messenger.getFileNotFoundMessage());
        } else return new File(resource.getFile());
    }

    private void checkFile() throws DAOException {
        checkForExistence();
        checkForEmptiness();
    }

    private void checkForExistence() throws DAOException {
        if(!(file.exists())) {
            ErrorMessenger messenger = ErrorMessenger.getInstance();
            throw new DAOException(messenger.getFileNotFoundMessage() + file.getAbsolutePath());
        }
    }

    private void checkForEmptiness() throws DAOException {
        if(file.length() == 0) {
            ErrorMessenger messenger = ErrorMessenger.getInstance();
            throw new DAOException(messenger.getFileIsEmptyMessage() + file.getAbsolutePath());
        }
    }

    private List<String> getData() throws DAOException {
        List<String> list = new ArrayList<>();
        try (Stream<String> stream = lines(get(file.getAbsolutePath()))) {
            list = stream.collect(toList());
        } catch (Exception e) {
            ErrorMessenger messenger = ErrorMessenger.getInstance();
            throw new DAOException(messenger.getCannotReadFile() + file.getAbsolutePath(), e);
        }
        return list;
    }

    private String[] buildScripts(List<String> list) {
        StringBuilder script = new StringBuilder();
        for(String line : list) {
            script.append(line);
        }
        return script.toString().split(";");
    }
}
