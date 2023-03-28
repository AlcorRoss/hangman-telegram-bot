package com.alcorross.services;

import com.alcorross.listeners.Listener;
import com.alcorross.model.GameSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GameplayTest {

    private static final Gameplay GAMEPLAY = Gameplay.getGameplayInstance();
    private static final Listener LISTENER = Listener.getListenerInstance();


    @BeforeAll
    static void createGameSession() {
        LISTENER.getCurrentSessions().put("1", new GameSession("ABC", "1"));
        LISTENER.getCurrentSessions().put("2", new GameSession("ABC", "2"));
        LISTENER.getCurrentSessions().put("3", new GameSession("ABC", "3"));
    }

    @Test
    void getGameplayInstanceInstanceShouldReturnGameplay() {
        assertThat(Gameplay.getGameplayInstance())
                .withFailMessage("getGameplayInstance() should return Gameplay")
                .isInstanceOf(Gameplay.class);
    }

    @Test
    void makeAMoveShouldMakeLoseCounterAndWinCounterEqualToOne() {
        GAMEPLAY.makeAMove("1", LISTENER.getCurrentSessions().get("1"));
        GAMEPLAY.makeAMove("A", LISTENER.getCurrentSessions().get("1"));
        assertAll(
                () -> assertThat(LISTENER.getCurrentSessions().get("1").getLoseCounter())
                        .withFailMessage("The loseCounter should be equal to 1").isEqualTo(1),
                () -> assertThat(LISTENER.getCurrentSessions().get("1").getWinCounter())
                        .withFailMessage("The winCounter should be equal to 1").isEqualTo(1)
        );
    }

    @Test
    void sendAnswerShouldRemoveGameSessionFromTheCurrentSessionWhenDefeated() {
        for (int i = 0; i < 6; i++) GAMEPLAY.makeAMove(String.valueOf(i), LISTENER.getCurrentSessions().get("2"));
        assertThat(LISTENER.getCurrentSessions().size())
                .withFailMessage("The size of the CurrentSessions should be equal to 2")
                .isEqualTo(2);
    }

    @Test
    void sendAnswerShouldRemoveGameSessionFromTheCurrentSessionWhenWin() {
        GAMEPLAY.makeAMove("B", LISTENER.getCurrentSessions().get("1"));
        GAMEPLAY.makeAMove("C", LISTENER.getCurrentSessions().get("1"));
        assertThat(LISTENER.getCurrentSessions().size())
                .withFailMessage("The size of the CurrentSessions should be equal to 1")
                .isEqualTo(1);
    }

    @AfterAll
    static void clearCurrentSessions() {
        LISTENER.getCurrentSessions().clear();
    }
}
