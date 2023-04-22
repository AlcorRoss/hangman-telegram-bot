package com.alcorross.model;

import com.alcorross.services.Keyboard;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class GameSession {

    @Getter
    @Setter
    private long timeOfLastChange;
    @Getter
    @Setter
    private int loseCounter = 0;
    @Getter
    @Setter
    private int winCounter = 0;
    @Getter
    private final String word;
    @Getter
    private final String chatId;
    private final String userName;
    private final String firstName;
    private final String lastName;
    @Getter
    private final Set<String> usedCharacter = new TreeSet<>();
    @Getter
    private final StringBuilder st = new StringBuilder();
    Keyboard keyboard = Keyboard.getKeyboardInstance();
    Bot bot = Bot.getBotInstance();

    public GameSession(String word, Message message) {
        this.word = word;
        this.chatId = message.getChatId().toString();
        this.userName = message.getFrom().getUserName();
        this.firstName = message.getFrom().getFirstName();
        this.lastName = message.getFrom().getLastName();
        st.append(" _".repeat(word.length()));
        bot.sendMessage(chatId, st + "\r\n" + "Введите букву", keyboard.getKeyboard(usedCharacter));
        timeOfLastChange = System.currentTimeMillis();
    }
}
