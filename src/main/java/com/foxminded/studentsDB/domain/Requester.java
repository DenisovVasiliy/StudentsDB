package com.foxminded.studentsDB.domain;

import com.foxminded.studentsDB.dao.CourseDAO;
import com.foxminded.studentsDB.dao.DAOException;
import com.foxminded.studentsDB.dao.GroupDAO;
import com.foxminded.studentsDB.dao.StudentDAO;
import com.foxminded.studentsDB.ui.ConsoleListener;
import com.foxminded.studentsDB.ui.ConsolePrinter;
import com.foxminded.studentsDB.ui.Listener;
import com.foxminded.studentsDB.ui.Printer;

import java.util.List;

public class Requester {
    Printer printer = new ConsolePrinter();
    public void requestGetGroupsByCounter(int counter) throws DAOException {
        printer.printGroups(new GroupDAO().getGroupsByCounter(counter), counter);
    }

    public void requestGetStudentsByCourse() throws DAOException {
        List<Course> courses = requestGetAllCourses();
        Listener listener = new ConsoleListener();
        StudentDAO studentDAO = new StudentDAO();
        Course course = courses.get(listener.getCourseNumber(courses.size()) - 1);
        printer.printStudentsFromCourse(course.getName(), studentDAO.getByCourseName(course.getName()));
    }

    private List<Course> requestGetAllCourses() throws DAOException {
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courses = courseDAO.getAllCourses();
        printer.printCourses(courses);
        return courses;
    }
}
