package com.alcorross.services;

import com.alcorross.services.commands.Command;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CommandHandlerTest {

    @Test
    void getInstanceShouldReturnCommandHandlerInstance() {
        assertThat(CommandHandler.getInstance()).isInstanceOf(CommandHandler.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/start", "Новая игра", "Продолжить игру", "Моя статистика"})
    void getCommandShouldReturnCommand(String line) {
        assertThat(CommandHandler.getInstance().getCommand(line)).isInstanceOf(Command.class);
    }
}
