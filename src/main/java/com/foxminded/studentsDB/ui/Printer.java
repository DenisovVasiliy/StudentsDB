package com.foxminded.studentsDB.ui;

import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Group;
import com.foxminded.studentsDB.domain.Student;

import java.util.List;

public interface Printer {
    void printString(String string);
    void printGroups(List<Group> groups, int counter);
    void printCourses(List<Course> courses);
    void printStudentsFromCourse(String courseName, List<Student> students);
}
