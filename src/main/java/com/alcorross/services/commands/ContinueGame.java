package com.alcorross.services.commands;

public class ContinueGame implements Command {
    private static ContinueGame instance;

    private ContinueGame() {
    }

    public static ContinueGame getInstance() {
        if (instance == null) instance = new ContinueGame();
        return instance;
    }

    @Override
    public void execute(String chatId) {
        //TODO
    }
}
