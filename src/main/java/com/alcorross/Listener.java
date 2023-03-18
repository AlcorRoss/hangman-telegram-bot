package com.alcorross;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Listener implements Runnable {
    private static Listener listenerInstance;
    @Getter
    private final Map<String, GameSession> CURRENT_SESSIONS = new HashMap<>();
    private String chatId;
    Bot bot = Bot.getBotInstance();
    Keyboard keyboard = Keyboard.getKeyboardInstance();
    CheckMessage checkMessage = CheckMessage.getCheckMessageInstance();
    Dictionary dictionary = Dictionary.getDictionaryInstance();

    private Listener() {
    }

    public static Listener getListenerInstance() {
        if (listenerInstance == null) listenerInstance = new Listener();
        return listenerInstance;
    }

    @Override
    public void run() {
        Message message;
        String text;
        try {
            message = bot.getMESSAGES().take();
        } catch (InterruptedException e) {
            log.error("The thread was interrupted", e);
            return;
        }
        chatId = message.getChatId().toString();
        text = message.getText();
        if (checkMessage.isCommand(text)) {
            if (CURRENT_SESSIONS.containsKey(chatId))
                bot.sendMessage(chatId, "Эй, сперва закончите текущий раунд!",
                        keyboard.getKeyboard(CURRENT_SESSIONS.get(chatId).getUSED_CHARACTER()));
            else
                createNewGameSession();
        } else if (CURRENT_SESSIONS.containsKey(chatId)) {
            if (checkMessage.checkCharacter(text)) {
                CURRENT_SESSIONS.get(chatId).makeAMove(text.toLowerCase());
            } else {
                bot.sendMessage(chatId, "Некорректный ввод! Введите букву",
                        keyboard.getKeyboard(CURRENT_SESSIONS.get(chatId).getUSED_CHARACTER()));
            }
        }
    }

    private void createNewGameSession() {
        CURRENT_SESSIONS.put(chatId, new GameSession(dictionary.wordChoice(), chatId));
        log.info("Start new game. Quantity of players: " + CURRENT_SESSIONS.size());
    }
}
