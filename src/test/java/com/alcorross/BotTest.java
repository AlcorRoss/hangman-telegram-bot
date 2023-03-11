package com.alcorross;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class BotTest {

    @Test
    void botUsernameShouldNotBeEmpty() {
        Bot bot = new Bot();
        assertFalse(bot.getBotUsername().isEmpty(), "Bot username should not be empty");
    }

    @Test
    void botTokenShouldNotBeEmpty() {
        Bot bot = new Bot();
        assertFalse(bot.getBotToken().isEmpty(), "Bot token should not be empty");
    }
}
