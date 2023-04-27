package com.alcorross.model;

import com.alcorross.services.Keyboard;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Builder
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
    @Getter
    private final Set<String> usedCharacter;
    @Getter
    private final StringBuilder st;
    Keyboard keyboard = Keyboard.getInstance();
    Bot bot = Bot.getInstance();

    public GameSession(String word, String chatId) {
        this.word = word;
        this.chatId = chatId;
        st = new StringBuilder();
        st.append(" _".repeat(word.length()));
        usedCharacter = new TreeSet<>();
        bot.sendMessage(chatId, st + "\r\n" + "Введите букву", keyboard.getKeyboard(usedCharacter));
        timeOfLastChange = System.currentTimeMillis();
    }
}
