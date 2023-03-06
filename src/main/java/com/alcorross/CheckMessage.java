package com.alcorross;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckMessage {

    public static boolean checkCharacter(String character) {
        Pattern pattern = Pattern.compile("[а-яёА-ЯЁ]+");
        Matcher matcher = pattern.matcher(character);
        return character.length() == 1 && matcher.find();
    }
}
