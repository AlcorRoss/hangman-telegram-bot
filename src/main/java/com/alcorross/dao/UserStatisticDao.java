package com.alcorross.dao;

import com.alcorross.model.UserStatistic;
import com.alcorross.util.DataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Optional;

@Slf4j
public class UserStatisticDao {

    private static UserStatisticDao instance;
    private static final String SAVE_SQL = """
            INSERT INTO user_statistic (chat_id, number_of_wins, number_of_loses)
            VALUES (?,?,?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE user_statistic
            SET number_of_wins=number_of_wins+?,
            number_of_loses=number_of_loses+?
            WHERE chat_id=?
            """;

    private static final String GET_BY_CHAT_ID = """
            SELECT chat_id,
            number_of_wins,
            number_of_loses
            FROM user_statistic
            WHERE chat_id=?
            """;

    private UserStatisticDao() {
    }

    public static UserStatisticDao getInstance() {
        if (instance == null) instance = new UserStatisticDao();
        return instance;
    }

    public void save(UserStatistic userStatistic) {
        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, userStatistic.getChatId());
            preparedStatement.setInt(2, userStatistic.getNumberOfWins());
            preparedStatement.setInt(3, userStatistic.getNumberOfLoses());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Couldn't insert UserStatistic into the database", e);
        }
    }

    public void update(String chatId, int lose, int win) {
        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, win);
            preparedStatement.setInt(2, lose);
            preparedStatement.setString(3, chatId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Couldn't update user_statistic into the database", e);
        }
    }

    public Optional<UserStatistic> getByChatId(String chatId) {
        UserStatistic userStatistic = null;
        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(GET_BY_CHAT_ID)) {
            preparedStatement.setString(1, chatId);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userStatistic = UserStatistic.builder()
                        .chatId(resultSet.getString("chat_id"))
                        .numberOfLoses(resultSet.getInt("number_of_loses"))
                        .numberOfWins(resultSet.getInt("number_of_wins"))
                        .build();
            }
        } catch (SQLException e) {
            log.error("Couldn't get data from user_statistic", e);
        }
        return Optional.ofNullable(userStatistic);
    }
}
