package com.alcorross.services;

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
    Keyboard keyboard = Keyboard.getKeyboardInstance();
    Bot bot = Bot.getBotInstance();

    private Gameplay() {
    }

    public static Gameplay getGameplayInstance() {
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
        if (gameSession.getLoseCounter() == 6) {
            bot.sendMessage(gameSession.getChatId(),
                    Pictures.ERR_6 + "\r\n" + "Отгаданные буквы: "
                    + "\r\n" + gameSession.getSt() + "\r\n" + "Слово: " + gameSession.getWord() + "\r\n"
                    + "Поражение! Сыграть еще раз - /start", keyboard.getNewGameKeyboard());
            currentSessions.remove(gameSession.getChatId());
            log.info("The game has been completed.");
        } else if (gameSession.getWinCounter() == gameSession.getWord().length()) {
            bot.sendMessage(gameSession.getChatId(), "Слово: " + gameSession.getWord() + "\r\n" + "Победа!"
                                                     + "\r\n" + "Сыграть еще раз - /start", keyboard.getNewGameKeyboard());
            currentSessions.remove(gameSession.getChatId());
            log.info("The game has been completed.");
        } else {
            String temp = "\r\n" + "Допущено ошибок: " + gameSession.getLoseCounter() + "\r\n" + "Отгаданные буквы: "
                          + "\r\n" + gameSession.getSt() + "\r\n" + "Введите букву";
            switch (gameSession.getLoseCounter()) {
                case 0 -> bot.sendMessage(gameSession.getChatId(),
                        Pictures.ERR_0 + temp, keyboard.getKeyboard(gameSession.getUsedCharacter()));
                case 1 -> bot.sendMessage(gameSession.getChatId(),
                        Pictures.ERR_1 + temp, keyboard.getKeyboard(gameSession.getUsedCharacter()));
                case 2 -> bot.sendMessage(gameSession.getChatId(),
                        Pictures.ERR_2 + temp, keyboard.getKeyboard(gameSession.getUsedCharacter()));
                case 3 -> bot.sendMessage(gameSession.getChatId(),
                        Pictures.ERR_3 + temp, keyboard.getKeyboard(gameSession.getUsedCharacter()));
                case 4 -> bot.sendMessage(gameSession.getChatId(),
                        Pictures.ERR_4 + temp, keyboard.getKeyboard(gameSession.getUsedCharacter()));
                case 5 -> bot.sendMessage(gameSession.getChatId(),
                        Pictures.ERR_5 + temp, keyboard.getKeyboard(gameSession.getUsedCharacter()));
            }
            gameSession.setTimeOfLastChange(System.currentTimeMillis());
        }
    }
}
