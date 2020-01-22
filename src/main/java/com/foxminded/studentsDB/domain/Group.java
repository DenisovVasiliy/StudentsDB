package com.foxminded.studentsDB.domain;

public class Group {
    private final int ID;
    private String name;

    public Group (int id, String name) {
        this.ID = id;
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
