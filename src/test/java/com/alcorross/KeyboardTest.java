package com.alcorross;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyboardTest {
    private static final Keyboard keyboard = new Keyboard();
    private static final Map<Set<String>, ReplyKeyboardMarkup> mapTestKeyboard = new HashMap<>();

    static Stream<Set<String>> keyboardProviderFactory() {
        Random r = new Random();
        char[] alphabet = "абвгдежзийклмнопрстуфхцчшщъыьэюя".toCharArray();

        for (int j = 0; j < 1000; j++) {
            ReplyKeyboardMarkup kb = new ReplyKeyboardMarkup();
            KeyboardRow kRow = new KeyboardRow();
            List<KeyboardRow> keyboardRows = new ArrayList<>();
            List<String> unusedCharacter = new ArrayList<>();
            Set<String> usedCharacter = new HashSet<>();

            kb.setResizeKeyboard(true);
            kb.setOneTimeKeyboard(false);
            for (int i = 0; i < r.nextInt(0, alphabet.length); i++) {
                usedCharacter.add(String.valueOf(alphabet[r.nextInt(0, alphabet.length)]));
            }
            for (char c : alphabet) {
                if (!usedCharacter.contains(String.valueOf(c))) unusedCharacter.add(String.valueOf(c).toUpperCase());
            }
            for (String s : unusedCharacter) {
                kRow.add(new KeyboardButton(s));
                if (kRow.size() == 11) {
                    keyboardRows.add(kRow);
                    kRow = new KeyboardRow();
                }
            }
            if (kRow.size() > 0) keyboardRows.add(kRow);
            kb.setKeyboard(keyboardRows);
            mapTestKeyboard.put(usedCharacter, kb);
        }
        return mapTestKeyboard.keySet().stream();
    }

    @Test
    void newGameKeyboardShouldBeEqualKb() {
        ReplyKeyboardMarkup kb = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow kRow = new KeyboardRow();

        kRow.add(new KeyboardButton("Новая игра"));
        keyboardRows.add(kRow);
        kb.setResizeKeyboard(true);
        kb.setOneTimeKeyboard(false);
        kb.setKeyboard(keyboardRows);
        assertEquals(kb, keyboard.getNewGameKeyboard(), "newGameKeyboard should return a keyboard " +
                "equal to kb");
    }

    @ParameterizedTest
    @MethodSource("keyboardProviderFactory")
    void keyboardShouldBeEqualTestKeyboard(Set<String> unusedCharacter) {
        assertEquals(mapTestKeyboard.get(unusedCharacter), keyboard.getKeyboard(unusedCharacter),"getKeyboard" +
                " should return a keyboard equal to the keyboard from mapTestKeyboard");
    }
}
