package com.alcorross.model;

import com.alcorross.util.PropertiesUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class Bot extends TelegramLongPollingBot {

    @Getter
    private final LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    private static Bot botInstance;
    private static final String BOT_NAME = PropertiesUtil.get("bot.name");
    private static final String TOKEN = PropertiesUtil.get("token");

    private Bot(String botToken) {
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            messages.put(update.getMessage());
        } catch (InterruptedException e) {
            log.error("The thread was interrupted", e);
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    public static Bot getInstance() {
        if (botInstance == null) botInstance = new Bot(TOKEN);
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
