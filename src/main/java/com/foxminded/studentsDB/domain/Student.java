package com.foxminded.studentsDB.domain;

import com.foxminded.studentsDB.dao.DAOException;
import com.foxminded.studentsDB.dao.StudentDAO;


public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private int groupId;

    public Student(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupID) {
        this.groupId = groupID;
    }

    public boolean assignToCourse(Course course) throws DAOException {
        StudentDAO studentDAO = new StudentDAO();
        return studentDAO.assignToCourse(this, course);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;

        Student student = (Student) o;

        return id == student.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
