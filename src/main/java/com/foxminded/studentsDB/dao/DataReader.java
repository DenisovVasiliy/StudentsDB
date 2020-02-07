package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.domain.Course;

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

    public List<String> getNames(String fileName) throws DAOException {
        file = getFileFromResources(fileName);
        checkFile();
        return getData();
    }

    public List<Course> getCourses(String fileName) throws DAOException {
        file = getFileFromResources(fileName);
        checkFile();
        List<Course> courses = new ArrayList<>();
        try (Stream<String> stream = lines(get(file.getAbsolutePath()))) {
            courses = stream.map(this::createCourse).collect(toList());
        } catch (Exception e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_READ_FILE + file.getAbsolutePath(), e);
        }
        return courses;
    }

    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if(resource == null) {
            throw new IllegalArgumentException(MessagesConstantsDAO.FILE_NOT_FOUND_MESSAGE + fileName);
        } else return new File(resource.getFile());
    }

    private void checkFile() throws DAOException {
        checkForExistence();
        checkForEmptiness();
    }

    private void checkForExistence() throws DAOException {
        if(!(file.exists())) {
            throw new DAOException(MessagesConstantsDAO.FILE_NOT_FOUND_MESSAGE + file.getAbsolutePath());
        }
    }

    private void checkForEmptiness() throws DAOException {
        if(file.length() == 0) {
            throw new DAOException(MessagesConstantsDAO.FILE_IS_EMPTY_MESSAGE + file.getAbsolutePath());
        }
    }

    private List<String> getData() throws DAOException {
        List<String> list = new ArrayList<>();
        try (Stream<String> stream = lines(get(file.getAbsolutePath()))) {
            list = stream.collect(toList());
        } catch (Exception e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_READ_FILE + file.getAbsolutePath(), e);
        }
        return list;
    }

    private Course createCourse(String line) {
        String[] courseString = line.split("_");
        return new Course(courseString[0], courseString[1]);
    }

    private String[] buildScripts(List<String> list) {
        StringBuilder script = new StringBuilder();
        for(String line : list) {
            script.append(line);
        }
        return script.toString().split(";");
    }
}
