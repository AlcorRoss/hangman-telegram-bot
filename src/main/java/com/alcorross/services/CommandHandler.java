package com.alcorross.services;

import com.alcorross.services.commands.Command;
import com.alcorross.services.commands.Start;

public final class CommandHandler {
    private static CommandHandler commandHandlerInstance;

    private CommandHandler() {
    }

    public static CommandHandler getCommandHandlerInstance() {
        if (commandHandlerInstance == null) commandHandlerInstance = new CommandHandler();
        return commandHandlerInstance;
    }

    public Command getCommand(String line) {
        Command command = null;
        switch (line) {
            case ("/start"), ("Новая игра") -> command = Start.getStartInstance();
        }
        return command;
    }
}
