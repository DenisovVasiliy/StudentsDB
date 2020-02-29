package com.foxminded.studentsDB.service;

import com.foxminded.studentsDB.dao.CourseDAO;
import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.GroupDAO;
import com.foxminded.studentsDB.dao.StudentDAO;
import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Group;
import com.foxminded.studentsDB.domain.Student;
import com.foxminded.studentsDB.ui.Listener;
import com.foxminded.studentsDB.ui.Printer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequesterTest {
    private final static Group GROUP = new Group(1, "te-st");
    private static List<Group> groups = Collections.singletonList(GROUP);
    private final static Course COURSE = new Course(1, "test course", "course for testing");
    private static List<Course> courses = Collections.singletonList(COURSE);
    private final static Student STUDENT = new Student(1, "Test", "Student");
    private static List<Student> students = Collections.singletonList(STUDENT);
    private static Map<Student, Set<Course>> assignMap = new HashMap<>();
    @Mock
    private StudentDAO studentDAO;
    @Mock
    private CourseDAO courseDAO;
    @Mock
    private GroupDAO groupDAO;
    @Mock
    private Printer printer;
    @Mock
    private Listener listener;
    @InjectMocks
    private Requester requester = new Requester();

    RequesterTest() throws DAOException {
    }

    @BeforeAll
    public static void prepare() {
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(COURSE);
        assignMap.put(STUDENT, courseSet);
    }

    @Test
    public void shouldCallGetGroupsByCounter() throws DAOException {
        int i = 1;
        requester.requestGetGroupsByCounter(i);
        verify(groupDAO).getGroupsByCounter(i);
    }

    @Test
    public void shouldCallFourMethods() throws DAOException {
        when(courseDAO.getAllCourses()).thenReturn(courses);
        when(listener.getCourseNumber(1)).thenReturn(1);
        when(studentDAO.getByCourseName(COURSE.getName())).thenReturn(students);
        requester.requestGetStudentsByCourse();
        verify(courseDAO).getAllCourses();
        verify(listener).getCourseNumber(1);
        verify(studentDAO).getByCourseName(COURSE.getName());
        verify(printer).printStudentsFromCourse(COURSE.getName(), students);
    }

    @Test
    public void shouldCallDeleteStudent() throws DAOException {
        when(studentDAO.getStudentById(1)).thenReturn(STUDENT);
        requester.requestDeleteStudent(1);
        verify(studentDAO).getStudentById(1);
        verify(studentDAO).deleteStudent(STUDENT);
    }

    @Test
    public void shouldCallAssignToCourse() throws DAOException {
        requester.requestAssignStudentToCourse(STUDENT, COURSE);
        verify(studentDAO).assignToCourse(STUDENT, COURSE);
    }

    @Test
    public void shouldCallAssignToCourses() throws DAOException {
        requester.requestAssignStudentsToCourses(assignMap);
        verify(studentDAO).assignToCourses(assignMap);
    }
}