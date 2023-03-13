package com.alcorross;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyboardTest {
    private static final Keyboard testKeyboard = new Keyboard();

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
        assertEquals(kb, testKeyboard.getNewGameKeyboard(), "newGameKeyboard should return a keyboard " +
                "equal to kb");
    }
}
