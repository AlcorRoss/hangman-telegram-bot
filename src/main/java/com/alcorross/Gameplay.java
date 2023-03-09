package com.alcorross;

public class Gameplay {
    private String getCharacter(String chatId, CheckMessage checkMessage, Bot bot) {
        String character;
        long timeout = System.currentTimeMillis() + 60000;
        while (true) {
            while (!Bot.getMESSAGES().containsKey(chatId)) {
                if (System.currentTimeMillis() > timeout) {
                    bot.sendMessage(chatId, "Кажется, вы обо мне забыли... Тогда до новых встреч! " +
                            "Полагаю есть истории, где с подобного начиналось восстание машин...");
                    bot.sendMessage(chatId, "Сыграть еще раз - /start");
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
            bot.sendMessage(chatId, "Некорректный ввод! Попробуй снова");
            timeout = System.currentTimeMillis() + 60000;
        }
        return character.toLowerCase();
    }

    public void gameplay(String chatId) {
        Bot bot = new Bot();
        CheckMessage checkMessage = new CheckMessage();
        Dictionary dictionary = new Dictionary();
        int loseCounter = 0;
        int winCounter = 0;
        String word = dictionary.wordChoice();
        String character;
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
            switch (loseCounter) {
                case 0 -> bot.sendMessage(chatId, Pictures.ERR_0.toString());
                case 1 -> bot.sendMessage(chatId, Pictures.ERR_1.toString());
                case 2 -> bot.sendMessage(chatId, Pictures.ERR_2.toString());
                case 3 -> bot.sendMessage(chatId, Pictures.ERR_3.toString());
                case 4 -> bot.sendMessage(chatId, Pictures.ERR_4.toString());
                case 5 -> bot.sendMessage(chatId, Pictures.ERR_5.toString());
            }
            bot.sendMessage(chatId, "Допущено ошибок: " + loseCounter);
            bot.sendMessage(chatId, "Отгаданные буквы: " + st);
            if (loseCounter == 6) {
                bot.sendMessage(chatId, Pictures.ERR_6.toString());
                bot.sendMessage(chatId, "Ты проиграл! Сыграть еще раз - /start");
                bot.sendMessage(chatId, "Слово: " + word);
                Bot.getQUEUE().remove(chatId);
                break;
            } else if (winCounter == word.length()) {
                bot.sendMessage(chatId, "Ты победил! Сыграть еще раз - /start");
                Bot.getQUEUE().remove(chatId);
                break;
            }
        }
    }
}
