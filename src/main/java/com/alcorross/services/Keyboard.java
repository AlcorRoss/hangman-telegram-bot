package com.alcorross.services;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Keyboard {
    private static Keyboard keyboardInstance;
    private static final char[] ALPHABET = "абвгдежзийклмнопрстуфхцчшщъыьэюя".toCharArray();
    private static final ReplyKeyboardMarkup NEW_GAME_KEYBOARD = createNewGameKeyboard();

    private Keyboard() {
    }

    public static Keyboard getInstance() {
        if (keyboardInstance == null) keyboardInstance = new Keyboard();
        return keyboardInstance;
    }

    public ReplyKeyboardMarkup getKeyboard(Set<String> usedCharacter) {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        KeyboardRow kRow = new KeyboardRow();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        List<String> unusedCharacter = identifyUnusedCharacter(usedCharacter);

        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        for (String s : unusedCharacter) {
            kRow.add(new KeyboardButton(s));
            if (kRow.size() == 11) {
                keyboardRows.add(kRow);
                kRow = new KeyboardRow();
            }
        }
        if (kRow.size() > 0) keyboardRows.add(kRow);
        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }

    private List<String> identifyUnusedCharacter(Set<String> usedCharacter) {
        List<String> unusedCharacter = new ArrayList<>();
        for (char c : ALPHABET) {
            if (!usedCharacter.contains(String.valueOf(c))) unusedCharacter.add(String.valueOf(c).toUpperCase());
        }
        return unusedCharacter;
    }

    public ReplyKeyboardMarkup getNewGameKeyboard() {
        return NEW_GAME_KEYBOARD;
    }

    private static ReplyKeyboardMarkup createNewGameKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
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
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }
}
