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
    private static final String FILE_NOT_FOUND_MESSAGE = "File is not found: ";
    private static final String FILE_IS_EMPTY_MESSAGE = "File is empty: ";
    private static final String CANNOT_READ_FILE = "Cannot read a file: ";

    private DataReader() {
    }

    public static synchronized DataReader getInstance() {
        if(instance == null) {
            instance = new DataReader();
        }
        return instance;
    }

    public List<String> readData(String fileName) throws DAOException {
        file = getFileFromResources(fileName);
        checkFile();
        return getData();
    }

    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if(resource == null) {
            throw new IllegalArgumentException(FILE_NOT_FOUND_MESSAGE);
        } else return new File(resource.getFile());
    }

    private void checkFile() throws DAOException {
        checkForExistence();
        checkForEmptiness();
    }

    private void checkForExistence() throws DAOException {
        if(!(file.exists())) {
            throw new DAOException(FILE_NOT_FOUND_MESSAGE + file.getAbsolutePath());
        }
    }

    private void checkForEmptiness() throws DAOException {
        if(file.length() == 0) {
            throw new DAOException(FILE_IS_EMPTY_MESSAGE + file.getAbsolutePath());
        }
    }

    private List<String> getData() throws DAOException {
        List<String> list = new ArrayList<>();
        try (Stream<String> stream = lines(get(file.getAbsolutePath()))) {
            list = stream.collect(toList());
        } catch (Exception e) {
            throw new DAOException(CANNOT_READ_FILE + file.getAbsolutePath(), e);
        }
        return list;
    }
}
