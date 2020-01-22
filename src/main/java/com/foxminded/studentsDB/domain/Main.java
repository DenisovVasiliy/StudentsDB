package com.foxminded.studentsDB.domain;

import com.foxminded.studentsDB.dao.DAOException;
import com.foxminded.studentsDB.dao.GroupDAO;
import com.foxminded.studentsDB.dao.ScriptExecutor;
import com.foxminded.studentsDB.dao.StudentDAO;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String CREATE_TABLES = "CreateTables.sql";
    private static final String FILE_NOT_FOUND_MESSAGE = "File is not found: ";

    public static void main(String[] args) throws DAOException {
        ScriptExecutor scriptExecutor = new ScriptExecutor();
        Main main = new Main();
        File createTables = main.getFileFromResources(CREATE_TABLES);
        scriptExecutor.executeScript(createTables);

        List<Group> groups = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            groups.add(new Group(i + 1, "group #" + 11 + (i + 1) + 0));
            Student student = new Student(i+1, "Name-" + i, "Sur-" + i + 0);
            student.setGroupID(4-i);
            students.add(student);
        }

        for(int i = 0; i < 5; i++) {
            Student student = new Student(i+6, "Name-" + (i + 5), "Sur-" + i);
            student.setGroupID(3-i);
            students.add(student);
        }

        GroupDAO groupDAO = new GroupDAO();
        StudentDAO studentDAO = new StudentDAO();
        groupDAO.insertGroups(groups);
        studentDAO.insertStudents(students);
    }

    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if(resource == null) {
            throw new IllegalArgumentException(FILE_NOT_FOUND_MESSAGE);
        } else return new File(resource.getFile());
    }
}
