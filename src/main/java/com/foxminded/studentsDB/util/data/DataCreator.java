package com.foxminded.studentsDB.util.data;

import com.foxminded.studentsDB.dao.CourseDAO;
import com.foxminded.studentsDB.dao.GroupDAO;
import com.foxminded.studentsDB.dao.StudentDAO;
import com.foxminded.studentsDB.dao.infra.DataReader;
import com.foxminded.studentsDB.dao.infra.DataConstants;
import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Group;
import com.foxminded.studentsDB.domain.Student;

import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DataCreator {
    private DataReader dataReader = DataReader.getInstance();
    private Random random = new Random();
    private TestData testData = new TestData();
    private GroupDAO groupDAO = new GroupDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private CourseDAO courseDAO = new CourseDAO();

    public DataCreator() throws DAOException {
    }

    public TestData createTestData() throws DAOException {
        testData.setGroups(createGroups());
        insertGroupsIntoDatabase();
        testData.setCourses(createCourses());
        testData.setStudents(createStudents());
        createGroupsAssignments();
        insertIntoDatabase();
        return testData;
    }

    private List<Group> createGroups() {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            groups.add(new Group(createGroupName()));
        }
        return groups;
    }

    private List<Course> createCourses() throws DAOException {
        List<Course> courses = dataReader.getCourses(DataConstants.COURSES);
        int i = 1;
        for (Course course : courses) {
            course.setId(i++);
        }
        return courses;
    }

    private Map<Student, Set<Course>> createStudents() throws DAOException {
        Map<Student, Set<Course>> students = new HashMap<>();
        List<String> firstNames = dataReader.getNames(DataConstants.FIRST_NAMES);
        List<String> lastNames = dataReader.getNames(DataConstants.LAST_NAMES);
        for (int i = 0; i < 200; i++) {
            Student student = new Student(i + 1, firstNames.get(random.nextInt(firstNames.size())),
                    lastNames.get(random.nextInt(lastNames.size())));
            students.put(student, createCourseAssignments());
        }
        return students;
    }

    private String createGroupName() {
        StringBuilder name = new StringBuilder();
        name.append((char) (random.nextInt(26) + 97));
        name.append((char) (random.nextInt(26) + 97));
        name.append('-');
        name.append(random.nextInt(10));
        name.append(random.nextInt(10));
        return name.toString();
    }

    private Set<Course> createCourseAssignments() {
        int counter = random.nextInt(3) + 1;
        Set<Course> courseAssignments = new HashSet<>();
        List<Course> courses = testData.getCourses();
        for (int i = 0; i < counter; i++) {
            courseAssignments.add(courses.get(random.nextInt(courses.size())));
        }
        return courseAssignments;
    }

    private void createGroupsAssignments() {
        List<Student> freeStudents = new ArrayList<>(testData.getStudents().keySet());
        List<Group> groups = testData.getGroups();
        for (Group group : groups) {
            int counter = random.nextInt(2) * (random.nextInt(21) + 10);
            while (counter > 0) {
                Student student = freeStudents.get(random.nextInt(freeStudents.size()));
                student.setGroupId(group.getId());
                freeStudents.remove(student);
                counter--;
            }
        }
    }

    private void insertGroupsIntoDatabase() throws DAOException {
        groupDAO.insertGroups(testData.getGroups());
    }

    private void insertIntoDatabase() throws DAOException {
        insertCoursesIntoDatabase();
        insertStudentsIntoDatabase();
        insertCourseAssignmentsIntoDatabase();
    }

    private void insertCoursesIntoDatabase() throws DAOException {
        courseDAO.insertCourses(testData.getCourses());
    }

    private void insertStudentsIntoDatabase() throws DAOException {
        List<Student> students = new ArrayList<>(testData.getStudents().keySet());
        studentDAO.insertStudents(students);
    }

    private void insertCourseAssignmentsIntoDatabase() throws DAOException {
        studentDAO.assignToCourses(testData.getStudents());
    }
}
