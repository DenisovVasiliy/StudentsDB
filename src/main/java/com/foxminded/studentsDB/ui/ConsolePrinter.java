package com.foxminded.studentsDB.ui;

import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Group;
import com.foxminded.studentsDB.domain.Student;

import java.util.List;

import static java.lang.System.out;

public class ConsolePrinter implements Printer {
    private int studentIdWidth;
    private int studentNameWidth;

    @Override
    public void printString(String string) {
        out.println(string);
    }

    @Override
    public void printGroups(List<Group> groups, int counter) {
        out.println("Groups (with less or equals " + counter + " students):");
        for (Group group : groups) {
            out.println(group.getName());
        }
    }

    @Override
    public void printCourses(List<Course> courses) {
        out.println("\tCourses:\n" +
                "num | Course\n" +
                "----|" + "-".repeat(getLongestCourseLength(courses)));
        int i = 1;
        for(Course course : courses) {
            if(i < 10) out.print(" ");
            out.println(i + "  | " + course.getName());
            i++;
        }
    }

    @Override
    public void printStudentsFromCourse(String courseName, List<Student> students) {
        out.println(MessagesConstants.STUDENTS_FROM_COURSE + courseName + ": ");
        countStudentIdWidth(students);
        countStudentNameWidth(students);
        out.println(buildStudentsHeader());
        for(Student student : students) {
            out.println(buildStudentLine(student));
        }
    }

    private String buildStudentsHeader() {
        StringBuilder header = new StringBuilder();
        header.append("ID");
        header.append(" ".repeat(studentIdWidth - 2));
        header.append("| Student");
        header.append(" ".repeat(studentNameWidth - 8));
        header.append("|\n");
        header.append("-".repeat(studentIdWidth));
        header.append("|");
        header.append("-".repeat(studentNameWidth));
        header.append("|");
        return header.toString();
    }

    private String buildStudentLine(Student student) {
        StringBuilder line = new StringBuilder();
        line.append(student.getId());
        line.append(" ".repeat(studentIdWidth - String.valueOf(student.getId()).length()));
        line.append("| ");
        line.append(student.getFirstName());
        line.append(" ");
        line.append(student.getLastName());
        line.append(" ".repeat(countSpacesAfterName(student)));
        line.append("|");
        return line.toString();
    }

    private int countSpacesAfterName(Student student) {
        return studentNameWidth - (student.getFirstName().length() + student.getLastName().length() + 2);
    }

    private void countStudentNameWidth(List<Student> students) {
        int length = 7;
        for(Student student : students) {
            int counter = student.getFirstName().length() + student.getLastName().length() + 1;
            if(counter > length) {
                length = counter;
            }
        }
        studentNameWidth = length + 2;
    }

    private void countStudentIdWidth(List<Student> students) {
        int length = 2;
        int maxId = 0;
        for(Student student : students) {
            int id = student.getId();
            if(id > maxId) {
                maxId = id;
            }
        }
        int maxIdLength = String.valueOf(maxId).length();
        studentIdWidth = Math.max(length, maxIdLength) + 1;
    }

    private int getLongestCourseLength(List<Course> courses) {
        int length = 6;
        for(Course course : courses) {
            if(course.getName().length() > length) {
                length = course.getName().length();
            }
        }
        return length + 1;
    }
}
