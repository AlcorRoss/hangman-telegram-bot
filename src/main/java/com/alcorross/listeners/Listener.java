package com.alcorross.listeners;

import com.alcorross.model.Bot;
import com.alcorross.model.Dictionary;
import com.alcorross.model.GameSession;
import com.alcorross.services.CheckMessage;
import com.alcorross.services.Gameplay;
import com.alcorross.services.Keyboard;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class Listener implements Runnable {
    private static Listener listenerInstance;
    @Getter
    private final Map<String, GameSession> currentSessions = new ConcurrentHashMap<>(); //TODO Переместить в Gameplay
    Bot bot = Bot.getBotInstance();
    Keyboard keyboard = Keyboard.getKeyboardInstance();
    CheckMessage checkMessage = CheckMessage.getCheckMessageInstance();
    Dictionary dictionary = Dictionary.getDictionaryInstance();
    Gameplay gameplay = Gameplay.getGameplayInstance();

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
        while (true) {
            try {
                message = bot.getMessages().take();
            } catch (InterruptedException e) {
                log.error("The thread was interrupted", e);
                return;
            }
            //TODO Получать имя и фамилию и передавать в GameSession, чтобы потом добавлять в БД System.out.println(message.getFrom().getFirstName());
            final String chatId = message.getChatId().toString();
            text = message.getText();
            Optional<GameSession> optGameSession = Optional.ofNullable(currentSessions.get(chatId));
            if (checkMessage.isCommand(text)) {
                optGameSession.ifPresentOrElse(
                        session -> bot.sendMessage(chatId, "Эй, сперва закончите текущий раунд!",
                                keyboard.getKeyboard(currentSessions.get(chatId).getUsedCharacter())),
                        () -> createNewGameSession(chatId));
            } else if (optGameSession.isPresent()) {
                if (checkMessage.checkCharacter(text)) {
                    gameplay.makeAMove(text.toLowerCase(), optGameSession.get());
                } else {
                    bot.sendMessage(chatId, "Некорректный ввод! Введите букву",
                            keyboard.getKeyboard(optGameSession.get().getUsedCharacter()));
                }
            }
        }
    }

    private void createNewGameSession(String chatId) {
        currentSessions.put(chatId, new GameSession(dictionary.wordChoice(), chatId));
        log.info("Start new game. Quantity of players: " + currentSessions.size());
    }
}
