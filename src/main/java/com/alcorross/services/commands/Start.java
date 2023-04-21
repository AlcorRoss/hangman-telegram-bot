package com.alcorross.services.commands;

import com.alcorross.listeners.Listener;
import com.alcorross.model.Dictionary;
import com.alcorross.model.GameSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Start implements Command {

    private static Start startInstance;
    Dictionary dictionary = Dictionary.getDictionaryInstance();
    Listener listener = Listener.getListenerInstance();

    private Start() {
    }

    public static Start getStartInstance() {
        if (startInstance == null) startInstance = new Start();
        return startInstance;
    }

    @Override
    public void execute() {

    }

    private void createNewGameSession(String chatId) {
        listener.getCurrentSessions().put(chatId, new GameSession(dictionary.wordChoice(), chatId));
        log.info("Start new game. Quantity of players: " + listener.getCurrentSessions().size());
    }
}
