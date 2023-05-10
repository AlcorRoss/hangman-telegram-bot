package com.alcorross.listeners;

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
import static org.junit.jupiter.api.Assertions.assertAll;

class TimeoutCleanerTest {
    static Gameplay gameplay = Gameplay.getInstance();

    @BeforeAll
    static void insertUserStatisticAndGameSessionIntoDB() {
        UserStatisticDao.getInstance().save(UserStatistic.builder()
                .chatId("1")
                .numberOfWins(1)
                .numberOfLoses(1)
                .build()
        );
        UserStatisticDao.getInstance().save(UserStatistic.builder()
                .chatId("2")
                .numberOfWins(1)
                .numberOfLoses(1)
                .build()
        );
        GameSessionDao.getInstance().save(new GameSession(1, 1, "test",
                "2", new TreeSet<>(), new StringBuilder().append("_ _ _ _")));
    }

    @Test
    void getInstanceShouldReturnTimeoutCleaner() {
        assertThat(TimeoutCleaner.getInstance())
                .withFailMessage("getTimeCleanInstance() should return TimeoutCleaner")
                .isInstanceOf(TimeoutCleaner.class);
    }

    @Test
    void runShouldInsertGameSessionIntoDbAndRemoveItFromCurrentSessions() {
        gameplay.getCurrentSessions().put("1", new GameSession(1, 1, "test",
                "1", new TreeSet<>(), new StringBuilder().append("_ _ _ _")));
        gameplay.getCurrentSessions().get("1").setTimeOfLastChange(System.currentTimeMillis() - 70000);
        new Thread(TimeoutCleaner.getInstance()).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertAll(
                () -> assertThat(gameplay.getCurrentSessions()).isEmpty(),
                () -> assertThat(GameSessionDao.getInstance().getByChatId("1")).isPresent()
        );
    }

    @Test
    void runShouldUpdateGameSessionIntoDB() {
        GameSession gameSession = new GameSession(1, 1, "newTest",
                "2", new TreeSet<>(), new StringBuilder().append("_ _ _ _ _ _ _"));
        gameplay.getCurrentSessions().put("2", gameSession);
        gameplay.getCurrentSessions().get("2").setTimeOfLastChange(System.currentTimeMillis() - 70000);
        new Thread(TimeoutCleaner.getInstance()).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        var optionalGameSession = GameSessionDao.getInstance().getByChatId("2");
        assertThat(optionalGameSession).isPresent();
        assertAll(
                () -> assertThat(optionalGameSession.get().getLoseCounter()).isEqualTo(gameSession.getLoseCounter()),
                () -> assertThat(optionalGameSession.get().getWinCounter()).isEqualTo(gameSession.getWinCounter()),
                () -> assertThat(optionalGameSession.get().getWord()).isEqualTo(gameSession.getWord()),
                () -> assertThat(optionalGameSession.get().getChatId()).isEqualTo(gameSession.getChatId())
        );
    }

    @AfterAll
    static void cleanDB() {
        String sql = """
                DELETE FROM user_statistic
                WHERE chat_id IN('1','2');
                """;
        GameSessionDao.getInstance().delete("1");
        GameSessionDao.getInstance().delete("2");
        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
