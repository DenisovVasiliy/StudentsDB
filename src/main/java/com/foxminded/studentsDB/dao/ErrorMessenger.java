package com.foxminded.studentsDB.dao;

public class ErrorMessenger {
    private static ErrorMessenger instance;
    private static final String FILE_NOT_FOUND_MESSAGE = "File is not found: ";
    private static final String FILE_IS_EMPTY_MESSAGE = "File is empty: ";
    private static final String CANNOT_READ_FILE = "Cannot read a file: ";
    private static final String CANNOT_INSERT_STUDENTS_LIST = "Cannot insert list of students: ";
    private static final String CANNOT_INSERT_STUDENT = "Cannot insert student: ";
    private static final String CANNOT_EXECUTE_SCRIPTS = "Cannot execute scripts: ";
    private static final String CANNOT_GET_ALL_STUDENTS = "Cannot get all students: ";
    private static final String CANNOT_GET_STUDENTS_BY_COURSE = "Cannot get students by course name: ";
    private static final String CANNOT_DELETE_STUDENT = "Cannot delete student: ";
    private static final String CANNOT_ASSIGN_TO_COURSES = "Cannot assign students to courses: ";
    private static final String CANNOT_ASSIGN_TO_COURSE = "Cannot assign student to course: ";
    private static final String CANNOT_GET_ASSIGNMENTS = "Cannot get student's assignments: ";
    private static final String CANNOT_DELETE_FROM_COURSE = "Cannot delete student from course: ";
    private static final String CANNOT_PROCESS_STUDENTS_SET = "Cannot process set of students: ";
    private static final String CANNOT_PROCESS_ASSIGNMENTS_SET = "Cannot process set of assignments: ";

    private ErrorMessenger() {
    }

    public static ErrorMessenger getInstance() {
        if (instance == null) {
            instance = new ErrorMessenger();
        }
        return instance;
    }

    public String getFileNotFoundMessage() {
        return FILE_NOT_FOUND_MESSAGE;
    }

    public String getFileIsEmptyMessage() {
        return FILE_IS_EMPTY_MESSAGE;
    }

    public String getCannotReadFile() {
        return CANNOT_READ_FILE;
    }

    public String getCannotInsertStudentsList() {
        return CANNOT_INSERT_STUDENTS_LIST;
    }

    public String getCannotExecuteScripts() {
        return CANNOT_EXECUTE_SCRIPTS;
    }

    public String getCannotInsertStudent() {
        return CANNOT_INSERT_STUDENT;
    }

    public String getCannotGetAllStudents() {
        return CANNOT_GET_ALL_STUDENTS;
    }

    public String getCannotGetStudentsByCourse() {
        return CANNOT_GET_STUDENTS_BY_COURSE;
    }

    public String getCannotDeleteStudent() {
        return CANNOT_DELETE_STUDENT;
    }

    public String getCannotAssignToCourses() {
        return CANNOT_ASSIGN_TO_COURSES;
    }

    public String getCannotAssignToCourse() {
        return CANNOT_ASSIGN_TO_COURSE;
    }

    public String getCannotGetAssignments() {
        return CANNOT_GET_ASSIGNMENTS;
    }

    public String getCannotDeleteFromCourse() {
        return CANNOT_DELETE_FROM_COURSE;
    }

    public String getCannotProcessStudentsSet() {
        return CANNOT_PROCESS_STUDENTS_SET;
    }

    public String getCannotProcessAssignmentsSet() {
        return CANNOT_PROCESS_ASSIGNMENTS_SET;
    }
}
