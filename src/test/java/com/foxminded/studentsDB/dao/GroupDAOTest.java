package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.infra.DAOFactory;
import com.foxminded.studentsDB.dao.infra.ScriptExecutor;
import com.foxminded.studentsDB.domain.Group;
import com.foxminded.studentsDB.domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDAOTest {
    private Group group = new Group("te-st");
    private List<Group> testGroups = Collections.singletonList(group);
    private static DAOFactory daoFactory;
    private List<Group> groupList = new ArrayList<>();
    private List<Student> studentList = new ArrayList<>();
    private GroupDAO groupDAO = new GroupDAO();

    GroupDAOTest() throws DAOException {
    }

    @BeforeAll
    public static void prepare() throws DAOException {
        daoFactory = DAOFactory.getInstance("TestDatabaseH2.properties");
    }

    @BeforeEach
    public void createTables() throws DAOException {
        ScriptExecutor scriptExecutor = new ScriptExecutor();
        scriptExecutor.executeScript(QueryConstants.CREATE_TABLES);
    }

    @Test
    public void shouldInsertGroupIntoDB() throws DAOException {
        List<Group> groups = groupDAO.getAllGroups();
        assertEquals(0, groups.size());

        groupDAO.insertGroups(testGroups);

        List<Group> actualGroups = groupDAO.getAllGroups();

        assertEquals(testGroups, actualGroups);
    }

    @Test
    public void shouldGetAllGroupsFromDB() throws DAOException {
        groupDAO.insertGroups(testGroups);
        List<Group> actualGroups = groupDAO.getAllGroups();
        assertEquals(testGroups, actualGroups);
    }

    @Test
    public void shouldGetGroupsWithLessOrEqualsStudentCount() throws DAOException {
        groupDAO.insertGroups(createGroups());
        new StudentDAO().insertStudents(createStudents());
        List<Group> expectedGroups = new ArrayList<>();
        expectedGroups.add(groupList.get(0));
        expectedGroups.add(groupList.get(1));
        expectedGroups.add(groupList.get(3));

        List<Group> actualGroups = new GroupDAO().getGroupsByCounter(3);

        assertEquals(expectedGroups, actualGroups);
    }

    private List<Student> createStudents() {
        for (int i = 0; i < 10; i++) {
            studentList.add(new Student(i + 1, "Student", "Tester-" + i));
        }
        insertStudentsToGroups();
        return studentList;
    }

    private void insertStudentsToGroups() {
        for (int i = 0; i < 2; i++) {
            studentList.get(i).setGroupId(groupList.get(0).getId());
        }
        for (int i = 2; i < 5; i++) {
            studentList.get(i).setGroupId(groupList.get(1).getId());
        }
        for (int i = 5; i < 9; i++) {
            studentList.get(i).setGroupId(groupList.get(2).getId());
        }
    }

    private List<Group> createGroups() {
        for (int i = 0; i < 4; i++) {
            groupList.add(new Group("test-0" + i));
        }
        return groupList;
    }
}