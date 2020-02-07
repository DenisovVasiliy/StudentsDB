package com.foxminded.studentsDB.ui;

import com.foxminded.studentsDB.domain.Group;

import java.util.List;

public interface Printer {
    void printString(String string);
    void printGroups(List<Group> groups);
}
