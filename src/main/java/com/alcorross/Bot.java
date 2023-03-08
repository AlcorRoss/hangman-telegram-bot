package com.alcorross;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Bot extends TelegramLongPollingBot {
    private final ConcurrentSkipListSet<String> QUEUE = new ConcurrentSkipListSet<>();
    private final ConcurrentHashMap<String, String> MESSAGES = new ConcurrentHashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String chatId = message.getChatId().toString();
        boolean flag = message.getText().equals("/start") || message.getText().equals("Новая игра");
        boolean flag2 = QUEUE.contains(chatId);

        if (flag && flag2) {
            sendMessage(chatId, "Эй, сперва закончи текущий раунд!");
        } else if (flag) {
            if (QUEUE.size() <= 20) {
                QUEUE.add(chatId);
                new Thread(() -> gameplay(chatId)).start();
            } else sendMessage(chatId, "В настоящий момент превышено " +
                    "количество пользователей, возвращайся позднее!");
        } else if (flag2) {
            MESSAGES.put(chatId, message.getText());
        }
    }

    @Override
    public String getBotUsername() {
        Properties prop = getProp();
        return prop.getProperty("username");
    }

    @Override
    public String getBotToken() {
        Properties prop = getProp();
        return prop.getProperty("token");
    }

    private Properties getProp() {
        Properties properties = new Properties();
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(is);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private void sendMessage(String chatId, String text) {
        try {
            execute(new SendMessage(chatId, text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

   private String getMessage(String chatId, CheckMessage checkMessage) {
        String character;
        long timeout = System.currentTimeMillis() + 60000;
        while (true) {
            while (!MESSAGES.containsKey(chatId)) {
                if (System.currentTimeMillis() > timeout) {
                    sendMessage(chatId, "Кажется, вы обо мне забыли... Тогда до новых встреч! " +
                            "Полагаю есть истории, где с подобного начиналось восстание машин...");
                    sendMessage(chatId, "Сыграть еще раз - /start");
                    return null;
                }
            }
            character = MESSAGES.get(chatId);
            MESSAGES.remove(chatId);
            if (checkMessage.checkCharacter(character)) break;
            sendMessage(chatId, "Некорректный ввод! Попробуй снова");
            timeout = System.currentTimeMillis() + 60000;
        }
        return character.toLowerCase();
    }

    private void gameplay(String chatId) {
        CheckMessage checkMessage = new CheckMessage();
        Dictionary dictionary = new Dictionary();
        int loseCounter = 0;
        int winCounter = 0;
        String word = dictionary.wordChoice();
        String character;
        StringBuilder st = new StringBuilder();
        st.append(" _".repeat(word.length()));

        sendMessage(chatId, st.toString());
        while (true) {
            sendMessage(chatId, "Введите букву");
            character = getMessage(chatId, checkMessage);
            if (character == null) {
                QUEUE.remove(chatId);
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
                case 0 -> sendMessage(chatId, Pictures.ERR_0.toString());
                case 1 -> sendMessage(chatId, Pictures.ERR_1.toString());
                case 2 -> sendMessage(chatId, Pictures.ERR_2.toString());
                case 3 -> sendMessage(chatId, Pictures.ERR_3.toString());
                case 4 -> sendMessage(chatId, Pictures.ERR_4.toString());
                case 5 -> sendMessage(chatId, Pictures.ERR_5.toString());
            }
            sendMessage(chatId, "Допущено ошибок: " + loseCounter);
            sendMessage(chatId, "Отгаданные буквы: " + st);
            if (loseCounter == 6) {
                sendMessage(chatId, Pictures.ERR_6.toString());
                sendMessage(chatId, "Ты проиграл! Сыграть еще раз - /start");
                sendMessage(chatId, "Слово: " + word);
                QUEUE.remove(chatId);
                break;
            } else if (winCounter == word.length()) {
                sendMessage(chatId, "Ты победил! Сыграть еще раз - /start");
                QUEUE.remove(chatId);
                break;
            }
        }
    }
}
