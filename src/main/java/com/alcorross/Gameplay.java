package com.alcorross;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class Gameplay {

    private static Gameplay gameplayInstance;

    private Gameplay() {
    }

    public static Gameplay getGameplayInstance() {
        if (gameplayInstance == null) gameplayInstance = new Gameplay();
        return gameplayInstance;
    }

    private String getCharacter(String chatId, CheckMessage checkMessage,
                                Bot bot, Keyboard keyboard, Set<String> usedCharacter) {
        String character;
        long timeout = System.currentTimeMillis() + 60000;
        while (true) {
            while (!Bot.getMESSAGES().containsKey(chatId)) {
                if (System.currentTimeMillis() > timeout) {
                    bot.sendMessage(chatId, "Кажется, вы обо мне забыли... Тогда до новых встреч! " +
                            "Полагаю есть истории, где с подобного начиналось восстание машин..."
                            + "\r\n" + "Сыграть еще раз - /start", keyboard.getNewGameKeyboard());
                    log.info("The response waiting time has been exceeded. The game is forcibly completed.");
                    return null;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    log.error("The thread was interrupted", e);
                }
            }
            character = Bot.getMESSAGES().get(chatId);
            Bot.getMESSAGES().remove(chatId);
            if (checkMessage.checkCharacter(character)) break;
            bot.sendMessage(chatId, "Некорректный ввод! Попробуйте снова", keyboard.getKeyboard(usedCharacter));
            timeout = System.currentTimeMillis() + 60000;
        }
        return character.toLowerCase();
    }

    public void gameplay(String chatId) {
        Keyboard keyboard = Keyboard.getKeyboardInstance();
        Bot bot = Bot.getBotInstance();
        CheckMessage checkMessage = CheckMessage.getCheckMessageInstance();
        Dictionary dictionary = Dictionary.getDictionaryInstance();
        Set<String> usedCharacter = new TreeSet<>();
        int loseCounter = 0;
        int winCounter = 0;
        String word = dictionary.wordChoice();
        String character, temp;
        StringBuilder st = new StringBuilder();
        st.append(" _".repeat(word.length()));

        bot.sendMessage(chatId, st.toString(), null);
        while (true) {
            bot.sendMessage(chatId, "Введите букву", keyboard.getKeyboard(usedCharacter));
            character = getCharacter(chatId, checkMessage, bot, keyboard, usedCharacter);
            if (character == null) {
                Bot.getCURRENT_SESSIONS().remove(chatId);
                break;
            }
            if (usedCharacter.contains(character)) {
                bot.sendMessage(chatId, "Эти буквы вы уже пробовали: "
                        + usedCharacter, keyboard.getKeyboard(usedCharacter));
                continue;
            }
            usedCharacter.add(character);
            if (word.contains(character)) {
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == character.charAt(0)) {
                        st.setCharAt((i * 2) + 1, character.charAt(0));
                        winCounter++;
                    }
                }
            } else {
                loseCounter++;
            }
            temp = "\r\n" + "Допущено ошибок: " + loseCounter + "\r\n" + "Отгаданные буквы: " + "\r\n" + st;
            switch (loseCounter) {
                case 0 -> bot.sendMessage(chatId, Pictures.ERR_0 + temp, null);
                case 1 -> bot.sendMessage(chatId, Pictures.ERR_1 + temp, null);
                case 2 -> bot.sendMessage(chatId, Pictures.ERR_2 + temp, null);
                case 3 -> bot.sendMessage(chatId, Pictures.ERR_3 + temp, null);
                case 4 -> bot.sendMessage(chatId, Pictures.ERR_4 + temp, null);
                case 5 -> bot.sendMessage(chatId, Pictures.ERR_5 + temp, null);
            }
            if (loseCounter == 6) {

                bot.sendMessage(chatId, Pictures.ERR_6 + temp + "\r\n" + "Слово: " + word + "\r\n"
                        + "Поражение! Сыграть еще раз - /start", keyboard.getNewGameKeyboard());
                Bot.getCURRENT_SESSIONS().remove(chatId);
                break;
            } else if (winCounter == word.length()) {
                bot.sendMessage(chatId, "Слово: " + word + "\r\n" + "Победа!" + "\r\n"
                        + "Сыграть еще раз - /start", keyboard.getNewGameKeyboard());
                Bot.getCURRENT_SESSIONS().remove(chatId);
                break;
            }
        }
    }
}
