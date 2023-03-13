package com.alcorross;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameplayTest {

    private static boolean flag = true;

    void characterSupplier() {
        Random r = new Random();
        char[] chars = ("абвгдеёжзийклмнопрстуфхцчшщъыьэюя").toCharArray();
        while (flag) {
            Bot.getMESSAGES().put("1", String.valueOf(chars[r.nextInt(0, chars.length)]));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void setQueueShouldBeEmpty() {
        Bot.getQUEUE().add("1");
        new Thread(this::characterSupplier).start();
        new Gameplay().gameplay("1");
        flag = false;
        Bot.getMESSAGES().clear();
        assertTrue(Bot.getQUEUE().isEmpty());
    }
}
