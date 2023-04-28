package com.alcorross.services;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class KeyboardTest {
    private final Keyboard keyboard = Keyboard.getInstance();
    private static final Map<Set<String>, ReplyKeyboardMarkup> MAP_TEST_KEYBOARD = new HashMap<>();

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
            MAP_TEST_KEYBOARD.put(usedCharacter, kb);
        }
        return MAP_TEST_KEYBOARD.keySet().stream();
    }

    @Test
    void getKeyboardInstanceInstanceShouldReturnKeyboard() {
        assertThat(Keyboard.getInstance())
                .withFailMessage("getKeyboardInstance() should return Keyboard")
                .isInstanceOf(Keyboard.class);
    }

    @RepeatedTest(50)
    void newGameKeyboardShouldBeEqualKb() {
        ReplyKeyboardMarkup kb = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow kRow = new KeyboardRow();
        KeyboardRow kRow1 = new KeyboardRow();
        KeyboardRow kRow2 = new KeyboardRow();

        kRow.add(new KeyboardButton("Новая игра"));
        kRow1.add(new KeyboardButton("Продолжить игру"));
        kRow2.add(new KeyboardButton("Моя статистика"));
        keyboardRows.add(kRow);
        keyboardRows.add(kRow1);
        keyboardRows.add(kRow2);
        kb.setResizeKeyboard(true);
        kb.setOneTimeKeyboard(false);
        kb.setKeyboard(keyboardRows);
        assertThat(keyboard.getNewGameKeyboard())
                .withFailMessage("newGameKeyboard should return a keyboard equal to kb").isEqualTo(kb);
    }

    @ParameterizedTest
    @MethodSource("keyboardProviderFactory")
    void keyboardShouldBeEqualTestKeyboard(Set<String> unusedCharacter) {
        assertThat(keyboard.getKeyboard(unusedCharacter)).withFailMessage("getKeyboard should return" +
                                                                          " a keyboard equal to the keyboard from mapTestKeyboard")
                .isEqualTo(MAP_TEST_KEYBOARD.get(unusedCharacter));
    }
}
