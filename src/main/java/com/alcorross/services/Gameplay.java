package com.alcorross.services;

import com.alcorross.dao.UserStatisticDao;
import com.alcorross.enums.Pictures;
import com.alcorross.model.Bot;
import com.alcorross.model.GameSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Gameplay {
    private static Gameplay gameplayInstance;
    @Getter
    private final Map<String, GameSession> currentSessions = new ConcurrentHashMap<>();
    Keyboard keyboard = Keyboard.getInstance();
    Bot bot = Bot.getInstance();
    UserStatisticDao userStatisticDao = UserStatisticDao.getInstance();

    private Gameplay() {
    }

    public static Gameplay getInstance() {
        if (gameplayInstance == null) gameplayInstance = new Gameplay();
        return gameplayInstance;
    }

    public void makeAMove(String character, GameSession gameSession) {
        if (gameSession.getUsedCharacter().contains(character)) {
            bot.sendMessage(gameSession.getChatId(),
                    "Эти буквы вы уже пробовали: "
                    + gameSession.getUsedCharacter(), keyboard.getKeyboard(gameSession.getUsedCharacter()));
            return;
        }
        gameSession.getUsedCharacter().add(character);
        if (gameSession.getWord().contains(character)) {
            for (int i = 0; i < gameSession.getWord().length(); i++) {
                if (gameSession.getWord().charAt(i) == character.charAt(0)) {
                    gameSession.getSt().setCharAt((i * 2) + 1, character.charAt(0));
                    gameSession.setWinCounter(gameSession.getWinCounter() + 1);
                }
            }
        } else {
            gameSession.setLoseCounter(gameSession.getLoseCounter() + 1);
        }
        sendAnswer(gameSession);
    }

    private void sendAnswer(GameSession gameSession) {
        String messagePattern;
        if (gameSession.getLoseCounter() == 6) {
            messagePattern = """
                    %s
                    Поражение!
                    Отгаданные буквы: %s
                    Слово: %s
                    Сыграть еще раз - /start
                    """.formatted(Pictures.ERR_6, gameSession.getSt(), gameSession.getWord());
            bot.sendMessage(gameSession.getChatId(), messagePattern, keyboard.getNewGameKeyboard());
            currentSessions.remove(gameSession.getChatId());
            userStatisticDao.update(gameSession.getChatId(), 1, 0);
            log.info("The game has been completed.");
        } else if (gameSession.getWinCounter() == gameSession.getWord().length()) {
            messagePattern = """
                    Победа!
                    Слово: %s
                    Сыграть еще раз - /start
                    """.formatted(gameSession.getWord());
            bot.sendMessage(gameSession.getChatId(), messagePattern, keyboard.getNewGameKeyboard());
            currentSessions.remove(gameSession.getChatId());
            userStatisticDao.update(gameSession.getChatId(), 0, 1);
            log.info("The game has been completed.");
        } else {
            messagePattern = """
                    %s
                    Допущено ошибок: %s
                    Отгаданные буквы: %s
                    Введите букву
                    """;
            switch (gameSession.getLoseCounter()) {
                case 0 -> messagePattern = messagePattern.formatted(Pictures.ERR_0, 0, gameSession.getSt());
                case 1 -> messagePattern = messagePattern.formatted(Pictures.ERR_1, 1, gameSession.getSt());
                case 2 -> messagePattern = messagePattern.formatted(Pictures.ERR_2, 2, gameSession.getSt());
                case 3 -> messagePattern = messagePattern.formatted(Pictures.ERR_3, 3, gameSession.getSt());
                case 4 -> messagePattern = messagePattern.formatted(Pictures.ERR_4, 4, gameSession.getSt());
                case 5 -> messagePattern = messagePattern.formatted(Pictures.ERR_5, 5, gameSession.getSt());
            }
            bot.sendMessage(gameSession.getChatId(), messagePattern,
                    keyboard.getKeyboard(gameSession.getUsedCharacter()));
            gameSession.setTimeOfLastChange(System.currentTimeMillis());
        }
    }
}
