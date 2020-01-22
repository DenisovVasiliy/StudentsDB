package com.foxminded.studentsDB.domain;

public class Course {
    private final int ID;
    private String name;
    private String description;

    public Course (int id, String name, String description) {
        this.ID = id;
        this.name = name;
        this.description = description;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
