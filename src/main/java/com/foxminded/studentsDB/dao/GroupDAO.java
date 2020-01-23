package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.domain.Group;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
             PreparedStatement statement = connection.prepareStatement(INSERT_GROUPS)) {
            for(Group group : groups) {
                statement.setString(1, group.getName());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Cannot insert list of groups:", e);
        }
    }

    public List<Group> getGroupsByCounter(int counter) throws DAOException {
        List<Group> groups = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(GET_BY_COUNTER);
            statement.setInt(1, counter);
            resultSet = statement.executeQuery();
            groups = processGroupSet(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Cannot get groups with less or equals student count:", e);
        } finally {
            try {

                if(statement != null) {
                    statement.close();
                }
                if(resultSet != null) {
                    resultSet.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
