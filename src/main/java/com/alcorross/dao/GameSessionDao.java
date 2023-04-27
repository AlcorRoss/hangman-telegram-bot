package com.alcorross.dao;

import com.alcorross.model.GameSession;
import com.alcorross.util.DataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
public class GameSessionDao {

    private static GameSessionDao instance;
    private static final String SAVE_SQL = """
            INSERT INTO game_session(chat_id, lose_counter, win_counter, word, used_character, st)
            VALUES (?,?,?,?,?,?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE game_session
            SET lose_counter=?,
            win_counter=?,
            word=?,
            used_character=?,
            st=?
            WHERE chat_id=?
            """;

    private static final String GET_BY_CHAT_ID = """
            SELECT lose_counter,
            win_counter,
            word,
            used_character,
            st
            FROM game_session
            WHERE chat_id=?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM game_session
            WHERE chat_id=?
            """;

    private GameSessionDao() {
    }

    public static GameSessionDao getInstance() {
        if (instance == null) instance = new GameSessionDao();
        return instance;
    }

    public void save(GameSession gameSession) {
        StringBuilder usedCharacter = convertSetToStringBuilder(gameSession);

        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, gameSession.getChatId());
            preparedStatement.setInt(2, gameSession.getLoseCounter());
            preparedStatement.setInt(3, gameSession.getWinCounter());
            preparedStatement.setString(4, gameSession.getWord());
            preparedStatement.setString(5, usedCharacter.toString());
            preparedStatement.setString(6, gameSession.getSt().toString());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Couldn't insert GameSession into the database", e);
        }
    }

    public void update(GameSession gameSession) {
        StringBuilder usedCharacter = convertSetToStringBuilder(gameSession);

        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1, gameSession.getLoseCounter());
            preparedStatement.setInt(2, gameSession.getWinCounter());
            preparedStatement.setString(3, gameSession.getWord());
            preparedStatement.setString(4, usedCharacter.toString());
            preparedStatement.setString(5, gameSession.getSt().toString());
            preparedStatement.setString(6, gameSession.getChatId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Couldn't update game_session", e);
        }
    }

    public Optional<GameSession> getByChatId(String chatId) {
        GameSession gameSession = null;

        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(GET_BY_CHAT_ID)) {
            preparedStatement.setString(1, chatId);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                gameSession = GameSession.builder()
                        .chatId(chatId)
                        .loseCounter(resultSet.getInt("lose_counter"))
                        .winCounter(resultSet.getInt("win_counter"))
                        .word(resultSet.getString("word"))
                        .usedCharacter(convertStringToSet(resultSet.getString("used_character")))
                        .st(new StringBuilder(resultSet.getString("st")))
                        .timeOfLastChange(System.currentTimeMillis())
                        .build();
            }
        } catch (SQLException e) {
            log.error("Couldn't get data from game_session", e);
        }
        return Optional.ofNullable(gameSession);
    }

    public void delete(String chatId) {
        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setString(1, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Couldn't delete data from game_session", e);
        }
    }

    private Set<String> convertStringToSet(String usedCharacter) {
        Set<String> charSet = new TreeSet<>();
        for (int i = 0; i < usedCharacter.length(); i++) charSet.add(String.valueOf(usedCharacter.charAt(i)));
        return charSet;
    }

    private static StringBuilder convertSetToStringBuilder(GameSession gameSession) {
        StringBuilder usedCharacter = new StringBuilder();
        for (String s : gameSession.getUsedCharacter()) usedCharacter.append(s);
        return usedCharacter;
    }
}
