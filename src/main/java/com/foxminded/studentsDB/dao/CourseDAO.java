package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.domain.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private static final String GET_ALL = "SELECT * FROM students;";
    private static final String INSERT_COURSES = "INSERT INTO courses(name, description) VALUES(?, ?);";

    private DAOFactory daoFactory = DAOFactory.getInstance();

    public CourseDAO() throws DAOException {
    }

    public List<Course> getAllCourses() throws DAOException {
        List<Course> courses = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            courses = processCoursesSet(resultSet);
        } catch (SQLException e) {
            throw new DAOException("Cannot get all courses:", e);
        }
        return courses;
    }

   public void insertCourses(List<Course> courses) throws DAOException {
       try (Connection connection = daoFactory.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(INSERT_COURSES, Statement.RETURN_GENERATED_KEYS)) {
           for (Course course : courses) {
               statement.setString(1, course.getName());
               statement.setString(2, course.getDescription());
               statement.addBatch();
           }
           statement.executeBatch();
           try (ResultSet resultSet = statement.getGeneratedKeys()) {
               for(Course course : courses) {
                   if(resultSet.next()) {
                       course.setId(resultSet.getInt(1));
                   }
               }
           }
       } catch (SQLException e) {
           throw new DAOException("Cannot insert courses:", e);
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
