package com.alcorross;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class BotTest {
    private static final Bot bot=new Bot();
    private static final Properties prop=new Properties();

    @BeforeAll
    static void loadProp() {
        try (FileInputStream fis = new FileInputStream("src/test/resources/configTest.properties")) {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void botUsernameShouldNotBeEmpty() {
        assertFalse(bot.getBotUsername().isEmpty(), "Bot username should not be empty");
    }

    @Test
    void botUsernameShouldBeEqualExpectedOne() {
        assertEquals(bot.getBotUsername(), prop.getProperty("username"),"Username must match the expected one");
    }

    @Test
    void botTokenShouldNotBeEmpty() {
        assertFalse(bot.getBotToken().isEmpty(), "Bot token should not be empty");
    }

    @Test
    void botTokenShouldBeEqualExpectedOne() {
        assertEquals(bot.getBotToken(), prop.getProperty("token"),"Token must match the expected one");
    }
}
