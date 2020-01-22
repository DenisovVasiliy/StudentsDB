package com.foxminded.studentsDB.domain;

import com.foxminded.studentsDB.dao.DAOException;
import com.foxminded.studentsDB.dao.DAOFactory;
import com.foxminded.studentsDB.dao.StudentDAO;

import java.sql.SQLException;
import java.util.List;

public class Student {
    private final int ID;
    private String firstName;
    private String lastName;
    private int groupID;

    public Student (int id, String firstName, String lastName) {
        this.ID = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID (int groupID) {
        this.groupID = groupID;
    }

    public boolean assignToCourse(Course course) throws DAOException {
        StudentDAO studentDAO = new StudentDAO();
        return studentDAO.assignToCourse(this, course);
    }
}
