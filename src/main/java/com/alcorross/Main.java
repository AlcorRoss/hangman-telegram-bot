package com.alcorross;

import com.alcorross.exceptions.DictionaryLoadException;
import com.alcorross.listeners.Listener;
import com.alcorross.listeners.TimeoutCleaner;
import com.alcorross.model.Bot;
import com.alcorross.model.Dictionary;
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
        Bot bot = Bot.getBotInstance();
        try {
            Dictionary.getDictionaryInstance().readDictionary();
        } catch (DictionaryLoadException e) {
            log.error(e.getMessage(), e);
            System.exit(1);
        }
        log.info("The dictionary has been loaded");
        Thread listenerThread = new Thread(Listener.getListenerInstance());
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
        service.scheduleWithFixedDelay(TimeoutCleaner.getTimeCleanInstance(), 10, 5, TimeUnit.SECONDS);
        log.info("The TimeoutCleaner is running");
    }
}
