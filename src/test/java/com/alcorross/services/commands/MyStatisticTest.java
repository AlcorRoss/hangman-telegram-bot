package com.alcorross.services.commands;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MyStatisticTest {
    @Test
    void getInstanceShouldReturnMyStatisticInstance() {
        assertThat(MyStatistic.getInstance()).isInstanceOf(MyStatistic.class);
    }
}
