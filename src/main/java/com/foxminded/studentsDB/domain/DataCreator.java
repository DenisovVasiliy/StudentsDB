package com.foxminded.studentsDB.domain;

import com.foxminded.studentsDB.dao.DAOException;
import com.foxminded.studentsDB.dao.DataConstants;
import com.foxminded.studentsDB.dao.DataReader;
import com.foxminded.studentsDB.dao.GroupDAO;

import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DataCreator {
    private DataReader dataReader = DataReader.getInstance();
    private final Random random = new Random();
    private TestData testData = new TestData();

    public TestData createTestData() throws DAOException {
        testData.setGroups(createGroups());
        testData.setCourses(createCourses());
        testData.setStudents(createStudents());
        createGroupsAssignments();
        return testData;
    }

    private List<Group> createGroups() throws DAOException {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            groups.add(new Group(createGroupName()));
        }
        GroupDAO groupDAO = new GroupDAO();
        groupDAO.insertGroups(groups);
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
        name.append((char) (random.nextInt(27) + 96));
        name.append((char) (random.nextInt(27) + 96));
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
                student.setGroupID(group.getId());
                freeStudents.remove(student);
                counter--;
            }
        }
    }
}
