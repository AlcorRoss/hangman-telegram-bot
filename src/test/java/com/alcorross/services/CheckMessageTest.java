package com.alcorross.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CheckMessageTest {
    private static final CheckMessage CHECK_MESSAGE = CheckMessage.getCheckMessageInstance();

    static Stream<String> falseArgsProviderFactory() {
        List<String> argsList = new ArrayList<>();
        char[] chars = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
                "!@#$%^&*()_+-=/~.,?1234567890").toCharArray();
        char[] rChar = ("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя" +
                "!@#$%^&*()_+-=/~.,?1234567890").toCharArray();
        Random r = new Random();

        for (int i = 0; i < 1000; i++) argsList.add(String.valueOf(r.nextInt(-100000, 100000)));
        for (char aChar : chars) argsList.add(String.valueOf(aChar));
        for (int i = 0; i < 1000; i++) {
            StringBuilder st = new StringBuilder();
            for (int j = 0; j < r.nextInt(1, 51); j++) st.append(chars[r.nextInt(0, chars.length)]);
            argsList.add(st.toString());
        }
        for (int i = 0; i < 1000; i++) {
            StringBuilder st = new StringBuilder();
            for (int j = 0; j < r.nextInt(2, 51); j++) st.append(rChar[r.nextInt(0, rChar.length)]);
            argsList.add(st.toString());
        }
        return argsList.stream();
    }

    static Stream<String> trueArgsProviderFactory() {
        List<String> argsList = new ArrayList<>();
        char[] rChar = ("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя").toCharArray();
        for (char c : rChar) argsList.add(String.valueOf(c));
        return argsList.stream();
    }

    @Test
    void getCheckMessageInstanceShouldReturnCheckMessage() {
        assertThat(CheckMessage.getCheckMessageInstance())
                .withFailMessage("getCheckMessageInstance() should return CheckMessage")
                .isInstanceOf(CheckMessage.class);
    }

    @ParameterizedTest
    @MethodSource("trueArgsProviderFactory")
    void checkCharacterShouldReturnTrue(String line) {
        assertThat(CHECK_MESSAGE.checkCharacter(line))
                .withFailMessage("The method must return true for the value " + line).isTrue();
    }

    @ParameterizedTest
    @MethodSource("falseArgsProviderFactory")
    void checkCharacterShouldReturnFalse(String line) {
        assertThat(CHECK_MESSAGE.checkCharacter(line))
                .withFailMessage("The method must return false for the value " + line).isFalse();
    }

    @ParameterizedTest
    @MethodSource("falseArgsProviderFactory")
    void isCommandShouldReturnFalse(String line) {
        assertThat(CHECK_MESSAGE.isCommand(line))
                .withFailMessage("The method must return false for the value " + line).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/start", "Новая игра"})
    void isCommandShouldReturnTrue(String line) {
        assertThat(CHECK_MESSAGE.isCommand(line)).withFailMessage("The method must return true").isTrue();
    }
}
