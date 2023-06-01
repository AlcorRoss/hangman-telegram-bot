package com.alcorross.util;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class DataSourceTest {

    @Test
    void getConnectionShouldReturnConnection() throws SQLException {
        assertThat(DataSource.getConnection()).isInstanceOf(Connection.class);
    }
}
