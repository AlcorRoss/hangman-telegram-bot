package com.alcorross.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.assertj.core.api.Assertions.assertThat;

class BotTest {
    private final Bot bot = Bot.getBotInstance();
    private static Update update;

    @BeforeAll
    static void prepareMessage() {
        update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Mockito.doReturn(message).when(update).getMessage();
    }

    @Test
    void getBotInstanceShouldReturnBot() {
        assertThat(Bot.getBotInstance()).withFailMessage("getBotInstance() should return Bot")
                .isInstanceOf(Bot.class);
    }

    @Test
    void botUsernameShouldNotBeEmpty() {
        assertThat(bot.getBotUsername()).withFailMessage("Bot username should not be empty").isNotEmpty();
    }

    @RepeatedTest(50)
    void onUpdateReceivedShouldIncreaseMessages() {
        bot.onUpdateReceived(update);
        assertThat(bot.getMessages()).isNotEmpty();
        bot.getMessages().clear();
    }
}
