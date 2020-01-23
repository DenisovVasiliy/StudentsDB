package com.foxminded.studentsDB.domain;

public class Group {
    private int id;
    private String name;

    public Group (String name) {
        this.name = name;
    }

    public Group (int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
