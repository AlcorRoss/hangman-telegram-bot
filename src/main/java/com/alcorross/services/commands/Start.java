package com.alcorross.services.commands;

import com.alcorross.model.Bot;
import com.alcorross.model.Dictionary;
import com.alcorross.model.GameSession;
import com.alcorross.services.Gameplay;
import com.alcorross.services.Keyboard;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Slf4j
public class Start implements Command {

    private static Start startInstance;
    Dictionary dictionary = Dictionary.getInstance();
    Gameplay gameplay = Gameplay.getInstance();
    Bot bot = Bot.getInstance();
    Keyboard keyboard = Keyboard.getInstance();

    private Start() {
    }

    public static Start getInstance() {
        if (startInstance == null) startInstance = new Start();
        return startInstance;
    }

    @Override
    public void execute(Message message) {
        String chatId = message.getChatId().toString();
        Optional<GameSession> optGameSession = Optional.ofNullable(gameplay.getCurrentSessions().get(chatId));
        optGameSession.ifPresentOrElse(
                session -> bot.sendMessage(chatId, "Эй, сперва закончите текущий раунд!",
                        keyboard.getKeyboard(gameplay.getCurrentSessions().get(chatId).getUsedCharacter())),
                () -> createNewGameSession(message));
    }

    private void createNewGameSession(Message message) {
        gameplay.getCurrentSessions().put(message.getChatId().toString(),
                new GameSession(dictionary.wordChoice(), message));
        log.info("Start new game. Quantity of players: " + gameplay.getCurrentSessions().size());
    }
}
