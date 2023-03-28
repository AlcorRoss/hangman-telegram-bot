package com.alcorross.listeners;

import com.alcorross.exceptions.DictionaryLoadException;
import com.alcorross.model.Dictionary;
import com.alcorross.model.GameSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeoutCleanerTest {

    @Test
    void getTimeCleanInstanceShouldReturnTimeoutCleaner() {
        assertThat(TimeoutCleaner.getTimeCleanInstance())
                .withFailMessage("getTimeCleanInstance() should return TimeoutCleaner")
                .isInstanceOf(TimeoutCleaner.class);
    }

    @Test
    void runShouldClearCurrentSessionsToTheSizeOfTwoElements() throws DictionaryLoadException {
        Dictionary dictionary = Dictionary.getDictionaryInstance();
        dictionary.readDictionary();
        Listener listener = Listener.getListenerInstance();
        TimeoutCleaner timeoutCleaner = TimeoutCleaner.getTimeCleanInstance();

        for (int i = 0; i < 5; i++)
            listener.getCurrentSessions()
                    .put(String.valueOf(i), new GameSession(dictionary.wordChoice(), String.valueOf(i)));
        for (int i = 0; i < 3; i++)
            listener.getCurrentSessions().get(String.valueOf(i))
                    .setTimeOfLastChange(System.currentTimeMillis() - 70000);
        timeoutCleaner.run();
        assertThat(listener.getCurrentSessions().size())
                .withFailMessage("The size of CURRENT_SESSIONS should be equal to two")
                .isEqualTo(2);
    }

    @AfterAll
    static void clearCurrentSessions() {
        Listener.getListenerInstance().getCurrentSessions().clear();
    }
}
