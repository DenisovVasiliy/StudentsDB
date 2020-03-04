package com.foxminded.studentsDB.dao;

import com.foxminded.studentsDB.dao.infra.DAOFactory;
import com.foxminded.studentsDB.dao.infra.DataReader;
import com.foxminded.studentsDB.domain.Course;
import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.dao.exceptions.MessagesConstantsDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private DAOFactory daoFactory = DAOFactory.getInstance();
    private DataReader dataReader = DataReader.getInstance();

    public CourseDAO() throws DAOException {
    }

    public List<Course> getAllCourses() throws DAOException {
        String script = dataReader.getQuery(QueryConstants.GET_ALL_COURSES);
        List<Course> courses = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script);
             ResultSet resultSet = statement.executeQuery()) {
            courses = processCoursesSet(resultSet);
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_GET_COURSES, e);
        }
        return courses;
    }

    public Course getCourseById(int id) throws DAOException {
        String script = dataReader.getQuery(QueryConstants.GET_COURSE_BY_ID);
        List<Course> courses = null;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(script);) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                courses = processCoursesSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(MessagesConstantsDAO.CANNOT_GET_COURSE_BY_ID, e);
        }
        return courses.get(0);
    }

   public void insertCourses(List<Course> courses) throws DAOException {
       String script = dataReader.getQuery(QueryConstants.INSERT_COURSES);
       try (Connection connection = daoFactory.getConnection();
            PreparedStatement statement =
                    connection.prepareStatement(script, Statement.RETURN_GENERATED_KEYS)) {
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
           throw new DAOException(MessagesConstantsDAO.CANNOT_INSERT_COURSES, e);
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
            throw new DAOException(MessagesConstantsDAO.CANNOT_PROCESS_COURSES_SET, e);
        }
        return courses;
   }
}
