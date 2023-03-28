package com.alcorross.listeners;

import com.alcorross.model.Bot;
import com.alcorross.model.GameSession;
import com.alcorross.services.Keyboard;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
public class TimeoutCleaner implements Runnable {
    private static TimeoutCleaner timeoutCleanerInstance;
    private static final String MESSAGE = "Кажется, вы обо мне забыли... Тогда до новых встреч! " +
            "Полагаю есть истории, где с подобного начиналось восстание машин..."
            + "\r\n" + "Сыграть еще раз - /start";

    private TimeoutCleaner() {
    }

    public static TimeoutCleaner getTimeCleanInstance() {
        if (timeoutCleanerInstance == null) timeoutCleanerInstance = new TimeoutCleaner();
        return timeoutCleanerInstance;
    }

    @Override
    public void run() {
        Map<String, GameSession> currentSessions = Listener.getListenerInstance().getCurrentSessions();
        Set<String> tempSet = new HashSet<>(currentSessions.keySet());
        currentSessions.entrySet()
                .removeIf(entry -> System.currentTimeMillis() - entry.getValue().getTimeOfLastChange() > 60000);
        for (String s : tempSet) {
            if (!currentSessions.containsKey(s)) {
                Bot.getBotInstance().sendMessage(s, MESSAGE, Keyboard.getKeyboardInstance().getNewGameKeyboard());
                log.info("The response waiting time has been exceeded. The game is forcibly completed.");
            }
        }
    }
}





















