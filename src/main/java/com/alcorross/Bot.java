package com.alcorross;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Bot extends TelegramLongPollingBot {
    @Getter
    private static final Set<String> CURRENT_SESSIONS = Collections.synchronizedSet(new HashSet<>());
    @Getter
    private static final ConcurrentHashMap<String, String> MESSAGES = new ConcurrentHashMap<>();
    private static Bot botInstance;

    private Bot(String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        CheckMessage.getCheckMessageInstance().checkMessage(update.getMessage());
    }

    @Override
    public String getBotUsername() {
        return System.getenv("VAR_NAME");
    }

    public static Bot getBotInstance() {
        if (botInstance == null) botInstance = new Bot(System.getenv("VAR_TOKEN"));
        return botInstance;
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
