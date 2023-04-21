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
    Dictionary dictionary = Dictionary.getDictionaryInstance();
    Gameplay gameplay = Gameplay.getGameplayInstance();
    Bot bot = Bot.getBotInstance();
    Keyboard keyboard = Keyboard.getKeyboardInstance();

    private Start() {
    }

    public static Start getStartInstance() {
        if (startInstance == null) startInstance = new Start();
        return startInstance;
    }

    @Override
    public void execute(Message message) {
        //TODO Получать имя и фамилию и передавать в новую GameSession, чтобы потом добавлять в БД System.out.println(message.getFrom().getFirstName());
        String chatId = message.getChatId().toString();
        Optional<GameSession> optGameSession = Optional.ofNullable(gameplay.getCurrentSessions().get(chatId));
        optGameSession.ifPresentOrElse(
                session -> bot.sendMessage(chatId, "Эй, сперва закончите текущий раунд!",
                        keyboard.getKeyboard(gameplay.getCurrentSessions().get(chatId).getUsedCharacter())),
                () -> createNewGameSession(chatId));
    }

    private void createNewGameSession(String chatId) {
        gameplay.getCurrentSessions().put(chatId, new GameSession(dictionary.wordChoice(), chatId));
        log.info("Start new game. Quantity of players: " + gameplay.getCurrentSessions().size());
    }
}
