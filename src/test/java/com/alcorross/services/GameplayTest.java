package com.alcorross.services;

import com.alcorross.listeners.Listener;
import com.alcorross.model.GameSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GameplayTest {

    private final Gameplay gameplay = Gameplay.getGameplayInstance();
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
        gameplay.makeAMove("1", LISTENER.getCurrentSessions().get("1"));
        gameplay.makeAMove("A", LISTENER.getCurrentSessions().get("1"));
        assertAll(
                () -> assertThat(LISTENER.getCurrentSessions().get("1").getLoseCounter())
                        .withFailMessage("The loseCounter should be equal to 1").isEqualTo(1),
                () -> assertThat(LISTENER.getCurrentSessions().get("1").getWinCounter())
                        .withFailMessage("The winCounter should be equal to 1").isEqualTo(1)
        );
    }

    @Test
    void sendAnswerShouldRemoveGameSessionFromTheCurrentSessionWhenDefeated() {
        int size = LISTENER.getCurrentSessions().size() - 1;
        for (int i = 0; i < 6; i++) gameplay.makeAMove(String.valueOf(i), LISTENER.getCurrentSessions().get("2"));
        assertThat(LISTENER.getCurrentSessions().size())
                .withFailMessage("The size of the CurrentSessions should be equal to 2")
                .isEqualTo(size);
    }

    @Test
    void sendAnswerShouldRemoveGameSessionFromTheCurrentSessionWhenWin() {
        int size = LISTENER.getCurrentSessions().size() - 1;
        gameplay.makeAMove("A", LISTENER.getCurrentSessions().get("3"));
        gameplay.makeAMove("B", LISTENER.getCurrentSessions().get("3"));
        gameplay.makeAMove("C", LISTENER.getCurrentSessions().get("3"));
        assertThat(LISTENER.getCurrentSessions().size())
                .withFailMessage("The size of the CurrentSessions should be equal to 1")
                .isEqualTo(size);
    }

    @Test
    void makeAMoveShouldNotIncreaseLoseCounterAndWinCounter() {
        GameSession gameSession = new GameSession("abc", "1");
        gameSession.getUsedCharacter().add("t");
        for (int i = 0; i < 50; i++) gameplay.makeAMove("t", gameSession);
        assertAll(
                () -> assertThat(gameSession.getWinCounter()).isEqualTo(0),
                () -> assertThat(gameSession.getLoseCounter()).isEqualTo(0)
        );
    }

    @AfterAll
    static void clearCurrentSessions() {
        LISTENER.getCurrentSessions().clear();
    }
}
