package com.foxminded.studentsDB.util.data;

import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Group;
import com.foxminded.studentsDB.domain.Student;

import java.util.Set;
import java.util.List;
import java.util.Map;

public class TestData {
    Map<Student, Set<Course>> students;
    List<Course> courses;
    List<Group> groups;

    public Map<Student, Set<Course>> getStudents() {
        return students;
    }

    public void setStudents(Map<Student, Set<Course>> students) {
        this.students = students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
