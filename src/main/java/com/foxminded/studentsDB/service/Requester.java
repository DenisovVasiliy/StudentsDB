package com.foxminded.studentsDB.service;

import com.foxminded.studentsDB.dao.CourseDAO;
import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.GroupDAO;
import com.foxminded.studentsDB.dao.StudentDAO;
import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Student;
import com.foxminded.studentsDB.ui.ConsoleListener;
import com.foxminded.studentsDB.ui.ConsolePrinter;
import com.foxminded.studentsDB.ui.Listener;
import com.foxminded.studentsDB.ui.Printer;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Requester {
    private Printer printer = new ConsolePrinter();
    private StudentDAO studentDAO = new StudentDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private GroupDAO groupDAO = new GroupDAO();
    private Listener listener = new ConsoleListener();

    public Requester() throws DAOException {
    }


    public void requestGetGroupsByCounter(int counter) throws DAOException {
        printer.printGroups(groupDAO.getGroupsByCounter(counter), counter);
    }

    public void requestGetStudentsByCourse() throws DAOException {
        List<Course> courses = requestGetAllCourses();
        Course course = courses.get(listener.getCourseNumber(courses.size()) - 1);
        printer.printStudentsFromCourse(course.getName(), studentDAO.getByCourseName(course.getName()));
    }

    public void requestDeleteStudent(int studentId) throws DAOException {
        studentDAO.deleteStudent(studentDAO.getStudentById(studentId));
    }

    public List<Course> requestGetAllCourses() throws DAOException {
        List<Course> courses = courseDAO.getAllCourses();
        printer.printCourses(courses);
        return courses;
    }

    public void requestAssignStudentsToCourses(Map<Student, Set<Course>> assignMap) throws DAOException {
        studentDAO.assignToCourses(assignMap);
    }

    public void requestAssignStudentToCourse(Student student, Course course) throws DAOException {
        studentDAO.assignToCourse(student, course);
    }
}
