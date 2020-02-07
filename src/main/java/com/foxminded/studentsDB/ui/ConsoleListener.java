package com.foxminded.studentsDB.ui;

import com.foxminded.studentsDB.dao.DAOException;
import com.foxminded.studentsDB.dao.GroupDAO;

import java.util.Scanner;

public class ConsoleListener implements Listener {
    private Scanner console = new Scanner(System.in);
    Printer printer = new ConsolePrinter();

    @Override
    public void listen() throws DAOException {
        printer.printString(MessagesConstants.HELP);
        String command = console.nextLine().toLowerCase();
        while (!(command.equals("exit"))) {
            switch (command) {
                case "help": printer.printString(MessagesConstants.HELP);
                break;
                case "f g": printer.printGroups(new GroupDAO().getGroupsByCounter(getCounter()));
                break;
                case "exit": break;
                default:
                    printer.printString(MessagesConstants.UNKNOWN_COMMAND);
                    printer.printString(MessagesConstants.HELP);
            }
            console.nextLine();
            command = console.nextLine().toLowerCase();
        }
        console.close();
    }

    @Override
    public int getCounter() {
        printer.printString(MessagesConstants.INPUT_COUNTER);
        if(console.hasNextInt()) {
            return console.nextInt();
        } else printer.printString(MessagesConstants.COUNTER_INPUT_ERROR);
        console.nextLine();
        return getCounter();
    }
}
