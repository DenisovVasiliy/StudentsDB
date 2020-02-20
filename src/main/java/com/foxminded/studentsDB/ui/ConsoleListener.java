package com.foxminded.studentsDB.ui;

import com.foxminded.studentsDB.dao.exceptions.DAOException;
import com.foxminded.studentsDB.service.Requester;

import java.util.Scanner;

public class ConsoleListener implements Listener {
    private Scanner console = new Scanner(System.in);
    Printer printer = new ConsolePrinter();

    @Override
    public void listen() throws DAOException {
        printer.printString(MessagesConstants.HELP);
        Requester requester = new Requester();
        String command = console.nextLine().toLowerCase();
        while (!(command.equals(CommandsConstants.EXIT))) {
            switch (command) {
                case CommandsConstants.HELP:
                    printer.printString(MessagesConstants.HELP);
                    break;
                case CommandsConstants.FIND_GROUPS:
                    requester.requestGetGroupsByCounter(getCounter());
                    break;
                case CommandsConstants.FIND_STUDENTS_BY_COURSE:
                    requester.requestGetStudentsByCourse();
                    break;
                default:
                    printer.printString(MessagesConstants.UNKNOWN_COMMAND);
                    printer.printString(MessagesConstants.HELP);
            }
            if (command.equals(CommandsConstants.FIND_GROUPS)) console.nextLine();
            command = console.nextLine().toLowerCase();
        }
        console.close();
    }

    @Override
    public int getCounter() {
        printer.printString(MessagesConstants.INPUT_COUNTER);
        if (console.hasNextInt()) {
            return console.nextInt();
        } else printer.printString(MessagesConstants.COUNTER_INPUT_ERROR);
        console.nextLine();
        return getCounter();
    }

    @Override
    public int getCourseNumber(int limit) {
        printer.printString(MessagesConstants.INPUT_COURSE_NUMBER);
        int courseNumber;
        if (console.hasNextInt()) {
            courseNumber = console.nextInt();
            if ((courseNumber > 0) && (courseNumber <= limit)) {
                return courseNumber;
            }
        } else printer.printString(MessagesConstants.COURSE_NUMBER_INPUT_ERROR);
        console.nextLine();
        return getCounter();
    }
}
