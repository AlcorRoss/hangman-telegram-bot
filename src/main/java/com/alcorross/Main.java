package com.alcorross;

import com.alcorross.listeners.Listener;
import com.alcorross.listeners.TimeoutCleaner;
import com.alcorross.model.Bot;
import com.alcorross.util.DBTableCreator;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
    public static void main(String[] args) {
        DBTableCreator.createDBTable();
        Bot bot = Bot.getInstance();
        Thread listenerThread = new Thread(Listener.getInstance());
        listenerThread.setDaemon(true);
        listenerThread.start();
        log.info("The Listener is running");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
            log.info("The bot is running");
        } catch (TelegramApiException e) {
            log.error("Failed to register the bot", e);
        }
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(TimeoutCleaner.getInstance(), 10, 5, TimeUnit.SECONDS);
        log.info("The TimeoutCleaner is running");
    }
}
