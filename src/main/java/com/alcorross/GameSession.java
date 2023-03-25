package com.alcorross;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class GameSession {

    @Getter @Setter
    private long timeOfLastChange;
    @Getter @Setter
    private int loseCounter = 0;
    @Getter @Setter
    private int winCounter = 0;
    @Getter
    private final String word;
    @Getter
    private final String chatId;
    @Getter
    private final Set<String> usedCharacter = new TreeSet<>();
    @Getter
    private final StringBuilder st = new StringBuilder();
    Keyboard keyboard = Keyboard.getKeyboardInstance();
    Bot bot = Bot.getBotInstance();

    public GameSession(String word, String chatId) {
        this.word = word;
        this.chatId = chatId;
        st.append(" _".repeat(word.length()));
        bot.sendMessage(chatId, st + "\r\n" + "Введите букву", keyboard.getKeyboard(usedCharacter));
        timeOfLastChange = System.currentTimeMillis();
    }
}
