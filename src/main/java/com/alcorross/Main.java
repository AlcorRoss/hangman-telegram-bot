package com.alcorross;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Bot bot = Bot.getBotInstance();
        Dictionary.getDictionaryInstance().readDictionary();
        log.info("The dictionary has been loaded");
        new Thread(LaunchManager.getLaunchManagerInstance()).start();
        log.info("The LaunchManager is running");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
            log.info("The bot is running");
        } catch (TelegramApiException e) {
            log.error("Failed to register the bot", e);
        }
    }
}
