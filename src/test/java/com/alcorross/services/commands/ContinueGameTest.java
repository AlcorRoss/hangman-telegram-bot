package com.alcorross.services.commands;

import com.alcorross.dao.GameSessionDao;
import com.alcorross.dao.UserStatisticDao;
import com.alcorross.model.GameSession;
import com.alcorross.model.UserStatistic;
import com.alcorross.services.Gameplay;
import com.alcorross.util.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

class ContinueGameTest {
    private static final Gameplay GAMEPLAY = Gameplay.getInstance();

    @BeforeAll
    static void insertGameSessionAndUserStatisticIntoDB() {
        UserStatisticDao.getInstance().save(UserStatistic.builder()
                .chatId("1")
                .numberOfWins(1)
                .numberOfLoses(1)
                .build()
        );
        GameSessionDao.getInstance().save(new GameSession(1, 1, "test",
                "1", new TreeSet<>(), new StringBuilder().append("_ _ _ _")));
    }

    @Test
    void getInstanceShouldReturnContinueGameInstance() {
        assertThat(ContinueGame.getInstance()).isInstanceOf(ContinueGame.class);
    }

    @Test
    void executeShouldAddGameSessionIntoCurrentSessions() {
        ContinueGame.getInstance().execute("1");
        assertThat(GAMEPLAY.getCurrentSessions().size()).isEqualTo(1);
    }

    @AfterAll
    static void cleanDBAndCurrentSessions() {
        String sql = """
                DELETE FROM user_statistic
                WHERE chat_id ='1';
                """;
        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        GAMEPLAY.getCurrentSessions().clear();
    }
}
