package com.alcorross.listeners;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TimeoutCleanerTest {

    @Test
    void getTimeCleanInstanceShouldReturnTimeoutCleaner() {
        assertThat(TimeoutCleaner.getInstance())
                .withFailMessage("getTimeCleanInstance() should return TimeoutCleaner")
                .isInstanceOf(TimeoutCleaner.class);
    }

}
