package com.alcorross.listeners;

import com.alcorross.exceptions.DictionaryLoadException;
import com.alcorross.model.Bot;
import com.alcorross.model.Dictionary;
import com.alcorross.model.GameSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.assertj.core.api.Assertions.assertThat;

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
}
