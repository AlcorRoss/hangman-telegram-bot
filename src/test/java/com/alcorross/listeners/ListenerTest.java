package com.alcorross.listeners;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ListenerTest {

    @Test
    void getListenerInstanceShouldReturnListener() {
        assertAll(
                () -> assertThat(Listener.getListenerInstance())
                        .withFailMessage("getListenerInstance should not return null")
                        .isNotNull(),
                () -> assertThat(Listener.getListenerInstance())
                        .withFailMessage("getListenerInstance should return Listener")
                        .isInstanceOf(Listener.class)
        );
    }
}
