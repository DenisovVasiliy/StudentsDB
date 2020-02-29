package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.exceptions.MessagesConstantsDAO;
import com.foxminded.studentsDB.dao.infra.DAOFactory;
import com.foxminded.studentsDB.dao.infra.DataReader;
import com.foxminded.studentsDB.dao.infra.ScriptExecutor;
import com.foxminded.studentsDB.domain.Group;
import com.foxminded.studentsDB.domain.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupDAOTest {
    private Group group = new Group("te-st");
    private List<Group> testGroups = Collections.singletonList(group);
    private DataReader dataReader = DataReader.getInstance();
    private static DAOFactory daoFactory;
    private List<Group> groupList = new ArrayList<>();
    private List<Student> studentList = new ArrayList<>();

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
        GroupDAO groupDAO = new GroupDAO();
        List<Group> groups = readGroupsFromDB();
        assertEquals(0, groups.size());

        groupDAO.insertGroups(testGroups);

        List<Group> actualGroups = readGroupsFromDB();

        assertEquals(testGroups, actualGroups);
    }

    @Test
    public void shouldGetAllGroupsFromDB() throws DAOException {
        GroupDAO groupDAO = new GroupDAO();
        insertGroupsIntoDB(testGroups);
        List<Group> actualGroups = groupDAO.getAllGroups();
        assertEquals(testGroups, actualGroups);
    }

    @Test
    public void shouldGetGroupsWithLessOrEqualsStudentCount() throws DAOException {
        insertGroupsIntoDB(createGroups());
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

    private List<Group> readGroupsFromDB() throws DAOException {
        String script = dataReader.getQuery(QueryConstants.GET_ALL_GROUPS);
        List<Group> groups = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script);
             ResultSet resultSet = statement.executeQuery()) {
            groups = processGroupSet(resultSet);
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_GET_GROUPS, e);
        }
        return groups;
    }

    private void insertGroupsIntoDB(List<Group> groups) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.INSERT_GROUPS);
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
            for (Group group : groups) {
                statement.setString(1, group.getName());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                for (Group group : groups) {
                    if (resultSet.next()) {
                        group.setId(resultSet.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_INSERT_GROUPS, e);
        }
    }

    private List<Group> processGroupSet(ResultSet resultSet) throws DAOException {
        List<Group> groups = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Group group = new Group(resultSet.getInt("id"), resultSet.getString("name"));
                groups.add(group);
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_PROCESS_GROUP_SET, e);
        }
        return groups;
    }
}