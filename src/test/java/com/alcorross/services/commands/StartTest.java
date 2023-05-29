package com.alcorross.services.commands;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StartTest {

    @Test
    void getInstanceShouldReturnStartInstance() {
        assertThat(Start.getInstance()).isInstanceOf(Start.class);
    }
}
