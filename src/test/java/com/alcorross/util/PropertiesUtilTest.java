package com.alcorross.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PropertiesUtilTest {

    @Test
    void getShouldReturnString() {
        assertAll(
                () -> assertThat(PropertiesUtil.get("db.password")).isInstanceOf(String.class),
                () -> assertThat(PropertiesUtil.get("db.username")).isInstanceOf(String.class),
                () -> assertThat(PropertiesUtil.get("db.url")).isInstanceOf(String.class),
                () -> assertThat(PropertiesUtil.get("db.pool.size")).isInstanceOf(String.class),
                () -> assertThat(PropertiesUtil.get("bot.name")).isInstanceOf(String.class),
                () -> assertThat(PropertiesUtil.get("token")).isInstanceOf(String.class)
        );
    }
}
