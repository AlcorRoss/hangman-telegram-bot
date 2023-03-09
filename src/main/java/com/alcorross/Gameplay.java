package com.alcorross;

import java.util.HashSet;
import java.util.Set;

public class Gameplay {
    private String getCharacter(String chatId, CheckMessage checkMessage, Bot bot) {
        String character;
        long timeout = System.currentTimeMillis() + 60000;
        while (true) {
            while (!Bot.getMESSAGES().containsKey(chatId)) {
                if (System.currentTimeMillis() > timeout) {
                    bot.sendMessage(chatId, "Кажется, вы обо мне забыли... Тогда до новых встреч! " +
                            "Полагаю есть истории, где с подобного начиналось восстание машин..."
                            + "\r\n" + "Сыграть еще раз - /start");
                    return null;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            character = Bot.getMESSAGES().get(chatId);
            Bot.getMESSAGES().remove(chatId);
            if (checkMessage.checkCharacter(character)) break;
            bot.sendMessage(chatId, "Некорректный ввод! Попробуйте снова");
            timeout = System.currentTimeMillis() + 60000;
        }
        return character.toLowerCase();
    }

    public void gameplay(String chatId) {
        Bot bot = new Bot();
        CheckMessage checkMessage = new CheckMessage();
        Dictionary dictionary = new Dictionary();
        Set<String> usedCharacter = new HashSet<>();
        int loseCounter = 0;
        int winCounter = 0;
        String word = dictionary.wordChoice();
        String character, temp;
        StringBuilder st = new StringBuilder();
        st.append(" _".repeat(word.length()));

        bot.sendMessage(chatId, st.toString());
        while (true) {
            bot.sendMessage(chatId, "Введите букву");
            character = getCharacter(chatId, checkMessage, bot);
            if (character == null) {
                Bot.getQUEUE().remove(chatId);
                break;
            }
            if (usedCharacter.contains(character)) {
                bot.sendMessage(chatId, "Эти буквы вы уже пробовали: " + usedCharacter);
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
            temp = "\r\n" + "Допущено ошибок: " + loseCounter + "\r\n" + "Отгаданные буквы: " + st;
            switch (loseCounter) {
                case 0 -> bot.sendMessage(chatId, Pictures.ERR_0 + temp);
                case 1 -> bot.sendMessage(chatId, Pictures.ERR_1 + temp);
                case 2 -> bot.sendMessage(chatId, Pictures.ERR_2 + temp);
                case 3 -> bot.sendMessage(chatId, Pictures.ERR_3 + temp);
                case 4 -> bot.sendMessage(chatId, Pictures.ERR_4 + temp);
                case 5 -> bot.sendMessage(chatId, Pictures.ERR_5 + temp);
            }
            if (loseCounter == 6) {
                bot.sendMessage(chatId, Pictures.ERR_6 + temp + "\r\n" + "Слово: " + word + "\r\n"
                        + "Ты проиграл! Сыграть еще раз - /start");
                Bot.getQUEUE().remove(chatId);
                break;
            } else if (winCounter == word.length()) {
                bot.sendMessage(chatId, "Слово: " + word + "\r\n" + "Ты победил!" + "\r\n"
                        + "Сыграть еще раз - /start");
                Bot.getQUEUE().remove(chatId);
                break;
            }
        }
    }
}
