package com.alcorross.dao;

import com.alcorross.model.GameSession;
import com.alcorross.model.UserStatistic;
import com.alcorross.util.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class GameSessionDaoTest {

    @BeforeAll
    static void insertGameSessionIntoDB() {
        UserStatisticDao.getInstance().save(UserStatistic
                .builder().chatId("1")
                .numberOfLoses(1)
                .numberOfWins(1)
                .build()
        );
        UserStatisticDao.getInstance().save(UserStatistic
                .builder().chatId("2")
                .numberOfLoses(1)
                .numberOfWins(1)
                .build()
        );
        UserStatisticDao.getInstance().save(UserStatistic
                .builder().chatId("3")
                .numberOfLoses(1)
                .numberOfWins(1)
                .build()
        );
        GameSessionDao.getInstance().save(new GameSession("word", "1"));
        GameSessionDao.getInstance().save(new GameSession("word", "3"));
    }

    @Test
    void getInstanceShouldReturnInstance() {
        assertThat(GameSessionDao.getInstance())
                .withFailMessage("getInstance should return GameSessionDao")
                .isInstanceOf(GameSessionDao.class);
    }

    @Test
    void saveShouldInsertGamesSessionIntoDB() {
        GameSessionDao.getInstance().save(new GameSession("word", "2"));
        var optionalGameSession = GameSessionDao.getInstance().getByChatId("2");
        assertThat(optionalGameSession).isPresent();
        assertThat(optionalGameSession.get().getChatId()).isEqualTo("2");
    }

    @Test
    void updateShouldUpdateGameSessionIntoDB() {
        GameSessionDao.getInstance().update(new GameSession("test", "1"));
        var optionalGameSession = GameSessionDao.getInstance().getByChatId("1");
        assertThat(optionalGameSession).isPresent();
        assertThat(optionalGameSession.get().getWord()).isEqualTo("test");
    }

    @Test
    void getByChatIdShouldReturnGameSession() {
        var optionalGameSession = GameSessionDao.getInstance().getByChatId("1");
        assertThat(optionalGameSession).isPresent();
    }

    @Test
    void deleteShouldDeleteGameSessionFromDB() {
        GameSessionDao.getInstance().delete("3");
        var optionalGameSession = GameSessionDao.getInstance().getByChatId("3");
        assertThat(optionalGameSession).isNotPresent();
    }

    @AfterAll
    static void cleanDB() {
        GameSessionDao.getInstance().delete("1");
        GameSessionDao.getInstance().delete("2");
        GameSessionDao.getInstance().delete("3");
        String sql = """
                DELETE FROM user_statistic
                WHERE chat_id IN ('1','2','3')
                """;
        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
