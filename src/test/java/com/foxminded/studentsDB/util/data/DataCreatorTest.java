package com.foxminded.studentsDB.util.data;


import com.foxminded.studentsDB.dao.CourseDAO;
import com.foxminded.studentsDB.dao.GroupDAO;
import com.foxminded.studentsDB.dao.QueryConstants;
import com.foxminded.studentsDB.dao.StudentDAO;
import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.infra.DAOFactory;
import com.foxminded.studentsDB.dao.infra.ScriptExecutor;
import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.domain.Group;
import com.foxminded.studentsDB.domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DataCreatorTest {
    private StudentDAO studentDAO = new StudentDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private GroupDAO groupDAO = new GroupDAO();
    private TestData testData;

    DataCreatorTest() throws DAOException {
    }

    @BeforeAll
    public static void prepare() throws DAOException {
        DAOFactory daoFactory = DAOFactory.getInstance("TestDatabase.properties");
    }

    @BeforeEach
    public void cleanTables() throws DAOException {
        ScriptExecutor scriptExecutor = new ScriptExecutor();
        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);
    }

    @Test
    public void shouldCreateDataAndPutItToDB() throws DAOException {
        List<Student> students = studentDAO.getAllStudents();
        assertEquals(0, students.size());
        List<Course> courses = courseDAO.getAllCourses();
        assertEquals(0, courses.size());
        List<Group> groups = groupDAO.getAllGroups();
        assertEquals(0, groups.size());

        DataCreator dataCreator = new DataCreator();
        testData = dataCreator.createTestData();

        students = studentDAO.getAllStudents();
        assertEquals(testData.getStudents().keySet().size(),students.size());
        assertTrue(students.containsAll(testData.getStudents().keySet()));

        courses = courseDAO.getAllCourses();
        assertEquals(testData.getCourses().size(),courses.size());
        assertTrue(courses.containsAll(testData.getCourses()));

        groups = groupDAO.getAllGroups();
        assertEquals(testData.getGroups().size(), groups.size());
        assertTrue(groups.containsAll(testData.getGroups()));
    }

    @Test
    public void groupsShouldContainZeroOrTenToThirtyStudents() throws DAOException {
        DataCreator dataCreator = new DataCreator();
        testData = dataCreator.createTestData();

        List<Student> students = studentDAO.getAllStudents();
        Map<Integer, Integer> groupsCounter = new HashMap<>();
        for (Student student : students) {
            Integer groupId = student.getGroupId();
            if(groupId > 0) {
                groupsCounter.put(groupId, groupsCounter.getOrDefault(groupId, 0) + 1);
            }
        }
        for(Integer groupId : groupsCounter.keySet()) {
            Integer counter = groupsCounter.get(groupId);
            assertTrue((counter >= 10) && (counter <= 30));
        }
    }
}