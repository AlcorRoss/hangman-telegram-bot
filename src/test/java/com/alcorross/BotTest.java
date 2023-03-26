package com.alcorross;

import com.alcorross.model.Bot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class BotTest {
    private static final Bot bot = Bot.getBotInstance();

    @Test
    void botUsernameShouldNotBeEmpty() {
        assertFalse(bot.getBotUsername().isEmpty(), "Bot username should not be empty");
    }
}
