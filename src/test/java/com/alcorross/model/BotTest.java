package com.alcorross.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BotTest {
    private static final Bot bot = Bot.getBotInstance();

    @Test
    void getBotInstanceShouldReturnBot() {
        assertThat(Bot.getBotInstance()).withFailMessage("getBotInstance() should return Bot")
                .isInstanceOf(Bot.class);
    }

    @Test
    void botUsernameShouldNotBeEmpty() {
        assertThat(bot.getBotUsername()).withFailMessage("Bot username should not be empty").isNotEmpty();
    }
}
