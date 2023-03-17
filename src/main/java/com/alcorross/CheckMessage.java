package com.alcorross;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CheckMessage {

    private static CheckMessage checkMessageInstance;

    private CheckMessage() {
    }

    public static CheckMessage getCheckMessageInstance() {
        if (checkMessageInstance == null) checkMessageInstance = new CheckMessage();
        return checkMessageInstance;
    }

    public boolean isCommand(String line) {
        return line.equals("/start") || line.equals("Новая игра");
    }

    public boolean checkCharacter(String character) {
        Pattern pattern = Pattern.compile("[а-яёА-ЯЁ]+");
        Matcher matcher = pattern.matcher(character);
        return character.length() == 1 && matcher.find();
    }
}
