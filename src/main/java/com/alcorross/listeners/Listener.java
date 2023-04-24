package com.alcorross.listeners;

import com.alcorross.model.Bot;
import com.alcorross.model.GameSession;
import com.alcorross.services.CheckMessage;
import com.alcorross.services.CommandHandler;
import com.alcorross.services.Gameplay;
import com.alcorross.services.Keyboard;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Slf4j
public class Listener implements Runnable {
    private static Listener listenerInstance;
    Bot bot = Bot.getInstance();
    Keyboard keyboard = Keyboard.getInstance();
    CheckMessage checkMessage = CheckMessage.getInstance();
    Gameplay gameplay = Gameplay.getInstance();
    CommandHandler commandHandler = CommandHandler.getInstance();

    private Listener() {
    }

    public static Listener getInstance() {
        if (listenerInstance == null) listenerInstance = new Listener();
        return listenerInstance;
    }

    @Override
    public void run() {
        Message message;
        String chatId;
        String text;
        while (true) {
            try {
                message = bot.getMessages().take();
            } catch (InterruptedException e) {
                log.error("The thread was interrupted", e);
                return;
            }
            text = message.getText();
            if (checkMessage.isCommand(text)) {
                commandHandler.getCommand(text).execute(message);
                continue;
            }
            chatId = message.getChatId().toString();
            Optional<GameSession> optGameSession = Optional.ofNullable(gameplay.getCurrentSessions().get(chatId));
            if (optGameSession.isPresent()) {
                if (checkMessage.checkCharacter(text)) gameplay.makeAMove(text.toLowerCase(), optGameSession.get());
                else bot.sendMessage(chatId, "Некорректный ввод! Введите букву",
                        keyboard.getKeyboard(optGameSession.get().getUsedCharacter()));
            }
        }
    }
}

