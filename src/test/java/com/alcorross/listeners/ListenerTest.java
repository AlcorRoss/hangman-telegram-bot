package com.alcorross.listeners;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ListenerTest {

    @Test
    void getListenerInstanceShouldReturnListener() {
        assertThat(Listener.getInstance())
                .withFailMessage("getListenerInstance() should return Listener")
                .isInstanceOf(Listener.class);
    }

}
