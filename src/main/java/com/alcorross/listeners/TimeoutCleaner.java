package com.alcorross.listeners;

import com.alcorross.dao.GameSessionDao;
import com.alcorross.model.Bot;
import com.alcorross.model.GameSession;
import com.alcorross.services.Gameplay;
import com.alcorross.services.Keyboard;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class TimeoutCleaner implements Runnable {
    private static TimeoutCleaner timeoutCleanerInstance;
    private static final String MESSAGE = "Кажется, вы обо мне забыли. Сохраню ваш прогресс. " +
                                          "Возвращайтесь, если захотите продолжить. " +
                                          "Кстати, мне кажется, есть истории, где с подобного " +
                                          "начиналось восстание машин...";
    Keyboard keyboard = Keyboard.getInstance();
    GameSessionDao gameSessionDao = GameSessionDao.getInstance();
    Bot bot = Bot.getInstance();

    private TimeoutCleaner() {
    }

    public static TimeoutCleaner getInstance() {
        if (timeoutCleanerInstance == null) timeoutCleanerInstance = new TimeoutCleaner();
        return timeoutCleanerInstance;
    }

    @Override
    public void run() {
        Map<String, GameSession> currentSessions = Gameplay.getInstance().getCurrentSessions();
        Set<String> tempSet = new HashSet<>();

        for (var chatId : currentSessions.keySet()) {
            var optGameSession = Optional.ofNullable(currentSessions.get(chatId));
            if (optGameSession.isPresent()
                && System.currentTimeMillis() - optGameSession.get().getTimeOfLastChange() > 60000) {
                tempSet.add(chatId);
                var optByChatId = gameSessionDao.getByChatId(chatId);
                optByChatId.ifPresentOrElse(
                        gameSession -> gameSessionDao.update(optGameSession.get()),
                        () -> gameSessionDao.save(optGameSession.get())
                );
                log.info("The response waiting time has been exceeded. The game is saved and forcibly terminated.");
            }
        }
        for (String chatId : tempSet) {
            currentSessions.remove(chatId);
            bot.sendMessage(chatId, MESSAGE, keyboard.getNewGameKeyboard());
        }
    }
}





















