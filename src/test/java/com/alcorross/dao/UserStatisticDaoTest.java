package com.alcorross.dao;

import com.alcorross.model.UserStatistic;
import com.alcorross.util.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserStatisticDaoTest {

    @BeforeAll
    static void insertIntoDB() {
        UserStatisticDao.getInstance().save(UserStatistic
                .builder().chatId("1")
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
    }

    @Test
    void getInstanceShouldReturnInstance() {
        assertThat(UserStatisticDao.getInstance())
                .withFailMessage("getInstance should return GameSessionDao")
                .isInstanceOf(UserStatisticDao.class);
    }

    @Test
    void saveShouldInsertUserStatisticIntoDB() {
        UserStatisticDao.getInstance().save(UserStatistic
                .builder().chatId("2")
                .numberOfLoses(1)
                .numberOfWins(1)
                .build()
        );
        var optionalUserStatistic = UserStatisticDao.getInstance().getByChatId("2");
        assertThat(optionalUserStatistic).isPresent();
    }

    @Test
    void updateShouldUpdateUserStatisticIntoDB() {
        UserStatisticDao.getInstance().update("1", 9, 4);
        var optionalUserStatistic = UserStatisticDao.getInstance().getByChatId("1");
        assertThat(optionalUserStatistic).isPresent();
        assertThat(optionalUserStatistic.get().getNumberOfWins()).isEqualTo(5);
        assertThat(optionalUserStatistic.get().getNumberOfLoses()).isEqualTo(10);
    }

    @Test
    void getByChatIdShouldReturnUserStatistic() {
        var optionalUserStatistic = UserStatisticDao.getInstance().getByChatId("3");
        assertThat(optionalUserStatistic).isPresent();
        assertThat(optionalUserStatistic.get()).isInstanceOf(UserStatistic.class);
    }


    @AfterAll
    static void cleanDB() {
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
