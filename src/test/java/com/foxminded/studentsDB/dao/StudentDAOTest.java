package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.infra.DAOFactory;
import com.foxminded.studentsDB.dao.infra.ScriptExecutor;
import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Group;
import com.foxminded.studentsDB.domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentDAOTest {
    private static List<Student> students = new ArrayList<>();
    private static List<Course> courses = new ArrayList<>();
    private StudentDAO studentDAO = new StudentDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private static DAOFactory daoFactory;

    StudentDAOTest() throws DAOException {
    }

    @BeforeAll
    public static void prepare() throws DAOException {
        daoFactory = DAOFactory.getInstance("TestDatabaseH2.properties");
        for (int i = 0; i < 3; i++) {
            students.add(new Student(i + 1, "Student-" + (i + 1), "Tester"));
            courses.add(new Course("TestCourse-" + (i + 1), "Course for testing."));
        }
    }

    @BeforeEach
    public void createTables() throws DAOException {
        ScriptExecutor scriptExecutor = new ScriptExecutor();
        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);
    }

    @Test
    public void shouldInsertStudentsIntoDB() throws DAOException {
        List<Student> studentsInDB = studentDAO.getAllStudents();
        assertEquals(0, studentsInDB.size());

        studentDAO.insertStudents(students);

        studentsInDB = studentDAO.getAllStudents();
        assertEquals(students, studentsInDB);
    }

    @Test
    public void shouldGetAllStudentsFromDB() throws DAOException {
        studentDAO.insertStudents(students);
        List<Student> studentsInDB = studentDAO.getAllStudents();
        assertEquals(students, studentsInDB);
    }

    @Test
    public void shouldGetStudentById() throws DAOException {
        studentDAO.insertStudents(students);
        Student actualStudent = studentDAO.getStudentById(students.get(0).getId());
        assertEquals(students.get(0), actualStudent);
    }

    @Test
    public void shouldAssignStudentsToCourses() throws DAOException {
        studentDAO.insertStudents(students);
        courseDAO.insertCourses(courses);
        Map<Student, Set<Course>> assignMap = new HashMap<>();
        Set<Course> courseSet = new HashSet<>(courses);
        assignMap.put(students.get(0), courseSet);
        assignMap.put(students.get(1), new HashSet<Course>(Collections.singletonList(courses.get(0))));

        studentDAO.assignToCourses(assignMap);

        for (Student student : students) {
            List<Course> actualAssignments = studentDAO.getStudentsAssignments(student);
            List<Course> expectedAssignments;
            if (assignMap.containsKey(student)) {
                expectedAssignments = new ArrayList<>(assignMap.get(student));
            } else {
                expectedAssignments = new ArrayList<>();
            }
            assertEquals(expectedAssignments, actualAssignments);
        }
    }

    @Test
    public void shouldAssignStudentToCourse() throws DAOException {
        studentDAO.insertStudents(students);
        courseDAO.insertCourses(courses);
        int i = 1;

        studentDAO.assignToCourse(students.get(i), courses.get(0));

        List<Course> actualAssignments = studentDAO.getStudentsAssignments(students.get(i));
        assertEquals(Collections.singletonList(courses.get(0)), actualAssignments);
    }

    @Test
    public void shouldGetStudentsByCourseName() throws DAOException {
        studentDAO.insertStudents(students);
        courseDAO.insertCourses(courses);
        Map<Student, Set<Course>> assignMap = new HashMap<>();
        Set<Course> courseSet = new HashSet<>(courses);
        assignMap.put(students.get(0), courseSet);
        assignMap.put(students.get(1), new HashSet<Course>(Collections.singletonList(courses.get(0))));
        studentDAO.assignToCourses(assignMap);

        List<Student> actualStudents = studentDAO.getByCourseName(courses.get(0).getName());

        List<Student> expectedStudents = students.subList(0, 2);
        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    public void shouldDeleteStudentFromCourse() throws DAOException {
        studentDAO.insertStudents(students);
        courseDAO.insertCourses(courses);
        Map<Student, Set<Course>> assignMap = new HashMap<>();
        Set<Course> courseSet = new HashSet<>(courses);
        assignMap.put(students.get(0), courseSet);
        assignMap.put(students.get(1), courseSet);
        studentDAO.assignToCourses(assignMap);

        List<Course> actualAssignments = studentDAO.getStudentsAssignments(students.get(0));
        assertEquals(courses, actualAssignments);
        actualAssignments = studentDAO.getStudentsAssignments(students.get(1));
        assertEquals(courses, actualAssignments);

        studentDAO.deleteFromCourse(students.get(0), courses.get(0));

        //check that another student wasn't deleted from this course
        actualAssignments = studentDAO.getStudentsAssignments(students.get(1));
        assertEquals(courses, actualAssignments);
        //check that the student was deleted from the course
        actualAssignments = studentDAO.getStudentsAssignments(students.get(0));
        assertEquals(courses.subList(1,3), actualAssignments);
    }

    @Test
    public void shouldDeleteStudent() throws DAOException {
        studentDAO.insertStudents(students);

        studentDAO.deleteStudent(students.get(0));

        List<Student> expectedStudents = students.subList(1, 3);
        List<Student> actualStudents = studentDAO.getAllStudents();

        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    public void shouldGetStudentAssignments() throws DAOException {
        studentDAO.insertStudents(students);
        courseDAO.insertCourses(courses);
        Map<Student, Set<Course>> assignMap = new HashMap<>();
        Set<Course> courseSet = new HashSet<>(courses);
        assignMap.put(students.get(0), courseSet);
        studentDAO.assignToCourses(assignMap);

        List<Course> actualAssignments = studentDAO.getStudentsAssignments(students.get(0));

        assertEquals(courses, actualAssignments);
    }
}