package com.alcorross.services;

import com.alcorross.services.commands.*;

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
            case ("Продолжить игру") -> command = ContinueGame.getInstance();
            case ("Моя статистика") -> command = MyStatistic.getInstance();
        }
        return command;
    }
}
