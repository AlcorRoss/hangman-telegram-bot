package com.alcorross;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
public class Bot extends TelegramLongPollingBot {
    @Getter
    private static final ConcurrentSkipListSet<String> QUEUE = new ConcurrentSkipListSet<>();
    @Getter
    private static final ConcurrentHashMap<String, String> MESSAGES = new ConcurrentHashMap<>();

    @Override
    public void onUpdateReceived(Update update) {
        new CheckMessage().checkMessage(update.getMessage());
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
            log.error("Failed to read file config.properties", e);
        }
        return properties;
    }

    public void sendMessage(String chatId, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        if (keyboard != null) sendMessage.setReplyMarkup(keyboard);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Failed to send message", e);
        }
    }
}
