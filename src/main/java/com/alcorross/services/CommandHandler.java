package com.alcorross.services;

import com.alcorross.services.commands.Command;
import com.alcorross.services.commands.NewGame;
import com.alcorross.services.commands.Start;

public final class CommandHandler {
    private static CommandHandler commandHandlerInstance;

    private CommandHandler() {
    }

    public static CommandHandler getInstance() {
        if (commandHandlerInstance == null) commandHandlerInstance = new CommandHandler();
        return commandHandlerInstance;
    }

    public Command getCommand(String line) {
        Command command = null;
        switch (line) {
            case ("/start") -> command = Start.getInstance();
            case ("Новая игра") -> command = NewGame.getInstance();
        }
        return command;
    }
}
