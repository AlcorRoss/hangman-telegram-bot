package com.alcorross.listeners;

import com.alcorross.model.Bot;
import com.alcorross.model.GameSession;
import com.alcorross.services.Gameplay;
import com.alcorross.util.DataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class ListenerTest {
    private static final Bot BOT = Bot.getInstance();


    @Test
    void getListenerInstanceShouldReturnListener() {
        assertThat(Listener.getInstance())
                .withFailMessage("getListenerInstance() should return Listener")
                .isInstanceOf(Listener.class);
    }

    @Test
    void listenerShouldTakeMessageFromMessages() {
        addMessageIntoMessages("test", 3L);
        executeListenerRun();
        assertThat(BOT.getMessages()).isEmpty();
    }

    @Test
    void listenerShouldExecuteNewGameCommand() {
        addMessageIntoMessages("Новая игра", 1L);
        executeListenerRun();
        assertThat(Gameplay.getInstance().getCurrentSessions().size()).isEqualTo(1);
    }

    @Test
    void listenerShouldExecuteGameplayMakeAMove() {
        Gameplay.getInstance().getCurrentSessions().put("2", new GameSession("а", "2"));
        addMessageIntoMessages("а", 2L);
        executeListenerRun();
        assertThat(Gameplay.getInstance().getCurrentSessions().size()).isEqualTo(0);
    }

    private void addMessageIntoMessages(String line, Long chatId) {
        var mock1 = Mockito.mock(Message.class);
        Mockito.doReturn(chatId).when(mock1).getChatId();
        Mockito.doReturn(line).when(mock1).getText();
        try {
            BOT.getMessages().put(mock1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeListenerRun() {
        Thread thread = new Thread(Listener.getInstance());
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread.interrupt();
    }

    @AfterEach
    void cleanCurrentSession() {
        Gameplay.getInstance().getCurrentSessions().clear();
    }

    @AfterAll
    static void cleanDB() {
        String sql = """
                DELETE FROM user_statistic
                WHERE chat_id='1'
                """;
        try (var connection = DataSource.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
