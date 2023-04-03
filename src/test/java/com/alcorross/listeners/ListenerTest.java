package com.alcorross.listeners;

import com.alcorross.exceptions.DictionaryLoadException;
import com.alcorross.model.Bot;
import com.alcorross.model.Dictionary;
import com.alcorross.model.GameSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ListenerTest {
    private final Bot bot = Bot.getBotInstance();
    private final Listener listener = Listener.getListenerInstance();
    private static final Dictionary DICT = Dictionary.getDictionaryInstance();

    @BeforeAll
    static void loadDictionary() throws DictionaryLoadException {
        DICT.readDictionary();
    }

    @Test
    void getListenerInstanceShouldReturnListener() {
        assertThat(Listener.getListenerInstance())
                .withFailMessage("getListenerInstance() should return Listener")
                .isInstanceOf(Listener.class);
    }

    @Test
    void runShouldCreateNewGameSession() {
        Message message = Mockito.mock(Message.class);
        Mockito.doReturn(1L).when(message).getChatId();
        Mockito.doReturn("/start").when(message).getText();
        bot.getMessages().offer(message);
        Thread threadListener = new Thread(listener);
        threadListener.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(listener.getCurrentSessions().get("1")).isInstanceOf(GameSession.class);
        threadListener.interrupt();
    }

    @Test
    void runShouldIncreaseWinCounter() {
        Message message = Mockito.mock(Message.class);
        Mockito.doReturn(2L).when(message).getChatId();
        Mockito.doReturn("а").when(message).getText();
        bot.getMessages().offer(message);
        listener.getCurrentSessions().put("2", new GameSession("абв", "2"));
        Thread threadListener = new Thread(listener);
        threadListener.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(listener.getCurrentSessions().get("2").getWinCounter()).isEqualTo(1);
        threadListener.interrupt();
    }

    @Test
    void runDoNotShouldIncreaseCurrentSessionsSize() {
        Message message = Mockito.mock(Message.class);
        Message message1 = Mockito.mock(Message.class);
        Message message2 = Mockito.mock(Message.class);
        Mockito.doReturn(1L).when(message).getChatId();
        Mockito.doReturn("/start").when(message).getText();
        Mockito.doReturn(1L).when(message1).getChatId();
        Mockito.doReturn("Новая игра").when(message1).getText();
        Mockito.doReturn(2L).when(message2).getChatId();
        Mockito.doReturn("123").when(message2).getText();
        bot.getMessages().offer(message);
        bot.getMessages().offer(message1);
        bot.getMessages().offer(message2);
        listener.getCurrentSessions().put("1", new GameSession("abc", "1"));
        Thread threadListener = new Thread(listener);
        threadListener.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertThat(listener.getCurrentSessions().size()).isEqualTo(1);
        threadListener.interrupt();
    }

    @Test
    void runDoNotShouldIncreaseWinCounterAndLoseCounterIfCharacterIsNotCorrect() {
        Message message = Mockito.mock(Message.class);
        Mockito.doReturn(3L).when(message).getChatId();
        Mockito.doReturn("1").when(message).getText();
        bot.getMessages().offer(message);
        listener.getCurrentSessions().put("3", new GameSession("abc", "3"));
        Thread threadListener = new Thread(listener);
        threadListener.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertAll(
                () -> assertThat(listener.getCurrentSessions().get("3").getWinCounter()).isEqualTo(0),
                () -> assertThat(listener.getCurrentSessions().get("3").getLoseCounter()).isEqualTo(0)
        );
        threadListener.interrupt();
    }

    @AfterEach
    void clearCurrentSessions() {
        Listener.getListenerInstance().getCurrentSessions().clear();
    }
}
