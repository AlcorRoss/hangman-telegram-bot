package com.alcorross.services.commands;

import com.alcorross.dao.GameSessionDao;
import com.alcorross.model.Bot;
import com.alcorross.model.GameSession;
import com.alcorross.services.Gameplay;
import com.alcorross.services.Keyboard;

import java.util.Optional;

public class ContinueGame implements Command {
    private static ContinueGame instance;
    Bot bot = Bot.getInstance();
    GameSessionDao gameSessionDao = GameSessionDao.getInstance();
    Keyboard keyboard = Keyboard.getInstance();
    Gameplay gameplay = Gameplay.getInstance();

    private ContinueGame() {
    }

    public static ContinueGame getInstance() {
        if (instance == null) instance = new ContinueGame();
        return instance;
    }

    @Override
    public void execute(String chatId) {
        Optional<GameSession> optionalGameSession = Optional.ofNullable(gameplay.getCurrentSessions().get(chatId));
        optionalGameSession.ifPresentOrElse(
                gameSession -> bot.sendMessage(chatId, "Эй, сперва закончите текущий раунд!",
                        keyboard.getKeyboard(gameSession.getUsedCharacter())),
                () -> {
                    var optionalByChatId = gameSessionDao.getByChatId(chatId);
                    optionalByChatId.ifPresentOrElse(
                            session -> {
                                gameplay.getCurrentSessions().put(chatId, session);
                                bot.sendMessage(chatId, session.getSt() + "\r\n" + "Введите букву",
                                        keyboard.getKeyboard(session.getUsedCharacter()));
                                gameSessionDao.delete(chatId);
                            },
                            () -> bot.sendMessage(chatId, "У вас нет сохраненных игр",
                                    keyboard.getNewGameKeyboard())
                    );
                }
        );
    }
}
