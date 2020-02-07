package com.foxminded.studentsDB.ui;

import com.foxminded.studentsDB.domain.Group;

import java.util.List;

import static java.lang.System.out;

public class ConsolePrinter implements Printer {
    @Override
    public void printString(String string) {
        out.println(string);
    }

    @Override
    public void printGroups(List<Group> groups) {
        out.println("groups||");
    }
}
