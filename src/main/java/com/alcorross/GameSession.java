package com.alcorross;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class GameSession {

    @Getter
    private long timeOfLastChange;
    private int loseCounter = 0;
    private int winCounter = 0;
    private final String WORD;
    @Getter
    private final String CHAT_ID;
    @Getter
    private final Set<String> USED_CHARACTER = new TreeSet<>();
    private final StringBuilder ST = new StringBuilder();
    Keyboard keyboard = Keyboard.getKeyboardInstance();
    Bot bot = Bot.getBotInstance();

    public GameSession(String WORD, String CHAT_ID) {
        this.WORD = WORD;
        this.CHAT_ID = CHAT_ID;
        ST.append(" _".repeat(WORD.length()));
        bot.sendMessage(CHAT_ID, ST + "\r\n" + "Введите букву", keyboard.getKeyboard(USED_CHARACTER));
        timeOfLastChange = System.currentTimeMillis();
    }

    public void makeAMove(String character) {
        if (USED_CHARACTER.contains(character)) {
            bot.sendMessage(CHAT_ID, "Эти буквы вы уже пробовали: "
                    + USED_CHARACTER, keyboard.getKeyboard(USED_CHARACTER));
            return;
        }
        USED_CHARACTER.add(character);
        if (WORD.contains(character)) {
            for (int i = 0; i < WORD.length(); i++) {
                if (WORD.charAt(i) == character.charAt(0)) {
                    ST.setCharAt((i * 2) + 1, character.charAt(0));
                    winCounter++;
                }
            }
        } else {
            loseCounter++;
        }
        sendAnswer();
    }

    private void sendAnswer() {
        if (loseCounter == 6) {
            bot.sendMessage(CHAT_ID, Pictures.ERR_6 + "\r\n" + "Отгаданные буквы: "
                    + "\r\n" + ST + "\r\n" + "Слово: " + WORD + "\r\n"
                    + "Поражение! Сыграть еще раз - /start", keyboard.getNewGameKeyboard());
            Listener.getListenerInstance().getCURRENT_SESSIONS().remove(CHAT_ID);
            log.info("The game has been completed.");
        } else if (winCounter == WORD.length()) {
            bot.sendMessage(CHAT_ID, "Слово: " + WORD + "\r\n" + "Победа!" + "\r\n"
                    + "Сыграть еще раз - /start", keyboard.getNewGameKeyboard());
            Listener.getListenerInstance().getCURRENT_SESSIONS().remove(CHAT_ID);
            log.info("The game has been completed.");
        } else {
            String temp = "\r\n" + "Допущено ошибок: " + loseCounter + "\r\n" + "Отгаданные буквы: "
                    + "\r\n" + ST + "\r\n" + "Введите букву";
            switch (loseCounter) {
                case 0 -> bot.sendMessage(CHAT_ID, Pictures.ERR_0 + temp, keyboard.getKeyboard(USED_CHARACTER));
                case 1 -> bot.sendMessage(CHAT_ID, Pictures.ERR_1 + temp, keyboard.getKeyboard(USED_CHARACTER));
                case 2 -> bot.sendMessage(CHAT_ID, Pictures.ERR_2 + temp, keyboard.getKeyboard(USED_CHARACTER));
                case 3 -> bot.sendMessage(CHAT_ID, Pictures.ERR_3 + temp, keyboard.getKeyboard(USED_CHARACTER));
                case 4 -> bot.sendMessage(CHAT_ID, Pictures.ERR_4 + temp, keyboard.getKeyboard(USED_CHARACTER));
                case 5 -> bot.sendMessage(CHAT_ID, Pictures.ERR_5 + temp, keyboard.getKeyboard(USED_CHARACTER));
            }
            timeOfLastChange = System.currentTimeMillis();
        }
    }
}
