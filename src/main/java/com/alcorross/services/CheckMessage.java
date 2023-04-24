package com.alcorross.services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CheckMessage {

    private static CheckMessage checkMessageInstance;
    private static final Pattern PATTERN = Pattern.compile("[а-яёА-ЯЁ]+");
    private static final List<String> COMMAND_LIST = new ArrayList<>(List.of("/Start", "Новая игра"));

    private CheckMessage() {
    }

    public static CheckMessage getInstance() {
        if (checkMessageInstance == null) checkMessageInstance = new CheckMessage();
        return checkMessageInstance;
    }

    public boolean isCommand(String line) {
        return COMMAND_LIST.contains(line);
    }

    public boolean checkCharacter(String character) {
        Matcher matcher = PATTERN.matcher(character);
        return character.length() == 1 && matcher.find();
    }
}
