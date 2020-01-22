package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.domain.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private static final String GET_ALL = "SELECT * FROM students;";
    private static final String INSERT_COURSES = "INSERT INTO courses(name, description) VALUES(?, ?);";

    private DAOFactory daoFactory = DAOFactory.getInstance();

    public List<Course> getAllCourses() throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Course> courses = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(GET_ALL);
            resultSet = statement.executeQuery();
            courses = processCoursesSet(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Cannot get all courses:", e);
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

        return courses;
    }

   public void insertCourses(List<Course> courses) throws DAOException {
       Connection connection = null;
       PreparedStatement statement = null;
       try {
           connection = daoFactory.getConnection();
           statement = connection.prepareStatement(INSERT_COURSES);
           for (Course course : courses) {
               statement.setString(1, course.getName());
               statement.setString(2, course.getDescription());
               statement.addBatch();
           }
           statement.executeBatch();
       } catch (SQLException e) {
           throw new DAOException("Cannot insert courses:", e);
       } finally {
           try {
               if(statement != null) {
                   statement.close();
               }
               if(connection != null) {
                   connection.close();
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
   }

   private List<Course> processCoursesSet(ResultSet resultSet) throws DAOException {
        List<Course> courses = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Course course = new Course(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"));
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot process result set of courses:", e);
        }
        return courses;
   }
}
