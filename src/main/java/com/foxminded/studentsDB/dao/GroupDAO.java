package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.domain.Group;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {
    private static final String GET_BY_COUNTER = "SELECT groups.id, groups.name, COUNT(*) FROM groups " +
            "LEFT JOIN students_groups on students_groups.group_id = groups.id " +
            "GROUP BY groups.id HAVING COUNT(*) <= ?;";
    private static final String INSERT_GROUPS = "INSERT INTO groups(name) VALUES (?);";

    DAOFactory daoFactory = DAOFactory.getInstance();

    public GroupDAO() throws DAOException {
    }

    public void insertGroups(List<Group> groups) throws DAOException {
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(INSERT_GROUPS, Statement.RETURN_GENERATED_KEYS)) {
            for(Group group : groups) {
                statement.setString(1, group.getName());
                statement.addBatch();
            }
            statement.executeBatch();
            try (ResultSet resultSet = statement.getResultSet()) {
                for(Group group : groups) {
                    if(resultSet.next()) {
                        group.setId(resultSet.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot insert list of groups:", e);
        }
    }

    public List<Group> getGroupsByCounter(int counter) throws DAOException {
        List<Group> groups = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_COUNTER)) {
            statement.setInt(1, counter);
            try (ResultSet resultSet = statement.executeQuery()) {
                groups = processGroupSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot get groups with less or equals student count:", e);
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
            throw new DAOException("Cannot process groups set: ", e);
        }
        return groups;
    }
}
