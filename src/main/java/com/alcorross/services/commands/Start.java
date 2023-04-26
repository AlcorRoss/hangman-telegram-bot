package com.alcorross.services.commands;

import com.alcorross.model.Bot;
import com.alcorross.model.Dictionary;
import com.alcorross.model.GameSession;
import com.alcorross.services.Gameplay;
import com.alcorross.services.Keyboard;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class Start implements Command {

    private static Start startInstance;
    Dictionary dictionary = Dictionary.getInstance();
    Gameplay gameplay = Gameplay.getInstance();
    Bot bot = Bot.getInstance();
    Keyboard keyboard = Keyboard.getInstance();

    private Start() { //TODO Проверять есть ли в БД, если нет - добавлять
    }

    public static Start getInstance() {
        if (startInstance == null) startInstance = new Start();
        return startInstance;
    }

    @Override
    public void execute(String chatId) {
        Optional<GameSession> optGameSession = Optional.ofNullable(gameplay.getCurrentSessions().get(chatId));
        optGameSession.ifPresentOrElse(
                session -> bot.sendMessage(chatId, "Эй, сперва закончите текущий раунд!",
                        keyboard.getKeyboard(gameplay.getCurrentSessions().get(chatId).getUsedCharacter())),
                () -> createNewGameSession(chatId));
    }

    private void createNewGameSession(String chatId) {
        gameplay.getCurrentSessions().put(chatId,
                new GameSession(dictionary.wordChoice(), chatId));
        log.info("Start new game. Quantity of players: " + gameplay.getCurrentSessions().size());
    }
}
