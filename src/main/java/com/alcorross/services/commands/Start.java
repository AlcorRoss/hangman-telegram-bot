package com.alcorross.services.commands;

import com.alcorross.model.Bot;
import com.alcorross.model.GameSession;
import com.alcorross.services.Gameplay;
import com.alcorross.services.Keyboard;

import java.util.Optional;

public class Start implements Command {

    private static Start instance;
    Keyboard keyboard = Keyboard.getInstance();
    Bot bot = Bot.getInstance();
    Gameplay gameplay = Gameplay.getInstance();

    private Start() {
    }

    public static Start getInstance() {
        if (instance == null) instance = new Start();
        return instance;
    }

    @Override
    public void execute(String chatId) {
        Optional<GameSession> optGameSession = Optional.ofNullable(gameplay.getCurrentSessions().get(chatId));
        optGameSession.ifPresentOrElse(
                session -> bot.sendMessage(chatId, "Эй, сперва закончите текущий раунд!",
                        keyboard.getKeyboard(session.getUsedCharacter())),
                () -> bot.sendMessage(chatId, "Выберите действие", keyboard.getNewGameKeyboard())
        );
    }
}
