package com.alcorross.services.commands;

import com.alcorross.dao.UserStatisticDao;
import com.alcorross.services.Gameplay;
import com.alcorross.util.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class NewGameTest {
    private static final Gameplay GAMEPLAY = Gameplay.getInstance();

    @Test
    void getInstanceShouldReturnNewGameInstance() {
        assertThat(NewGame.getInstance()).isInstanceOf(NewGame.class);
    }

    @Test
    void executeShouldCreateNewGameSession() {
        NewGame.getInstance().execute("1");
        assertAll(
                () -> assertThat(UserStatisticDao.getInstance().getByChatId("1")).isPresent(),
                () -> assertThat(GAMEPLAY.getCurrentSessions().size()).isEqualTo(1)
        );
    }

    @AfterAll
    static void cleanCurrentSessionAndDB() {
        GAMEPLAY.getCurrentSessions().clear();
        String sql = """
                DELETE FROM user_statistic
                WHERE chat_id='1';
                """;
        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
