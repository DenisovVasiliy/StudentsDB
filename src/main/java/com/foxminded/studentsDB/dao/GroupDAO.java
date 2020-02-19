package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.domain.Group;
import com.foxminded.studentsDB.dao.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {
    private DAOFactory daoFactory = DAOFactory.getInstance();
    private DataReader dataReader = DataReader.getInstance();

    public GroupDAO() throws DAOException {
    }

    public void insertGroups(List<Group> groups) throws DAOException {
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

    public List<Group> getGroupsByCounter(int counter) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.GET_GROUPS_BY_COUNTER);
        List<Group> groups = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script)) {
            statement.setInt(1, counter);
            try (ResultSet resultSet = statement.executeQuery()) {
                groups = processGroupSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_GET_GROUPS_BY_COUNTER, e);
        }
        return groups;
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
