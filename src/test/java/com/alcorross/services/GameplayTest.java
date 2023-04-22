package com.alcorross.services;

import com.alcorross.model.GameSession;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GameplayTest {

    private static final Gameplay GAMEPLAY = Gameplay.getGameplayInstance();

    @BeforeAll
    static void createGameSession() {
        GAMEPLAY.getCurrentSessions().put("1", new GameSession("ABC", "1"));
        GAMEPLAY.getCurrentSessions().put("2", new GameSession("ABC", "2"));
        GAMEPLAY.getCurrentSessions().put("3", new GameSession("ABC", "3"));
    }

    @Test
    void getGameplayInstanceInstanceShouldReturnGameplay() {
        assertThat(Gameplay.getGameplayInstance())
                .withFailMessage("getGameplayInstance() should return Gameplay")
                .isInstanceOf(Gameplay.class);
    }

    @Test
    void makeAMoveShouldMakeLoseCounterAndWinCounterEqualToOne() {
        GAMEPLAY.makeAMove("1", GAMEPLAY.getCurrentSessions().get("1"));
        GAMEPLAY.makeAMove("A", GAMEPLAY.getCurrentSessions().get("1"));
        assertAll(
                () -> assertThat(GAMEPLAY.getCurrentSessions().get("1").getLoseCounter())
                        .withFailMessage("The loseCounter should be equal to 1").isEqualTo(1),
                () -> assertThat(GAMEPLAY.getCurrentSessions().get("1").getWinCounter())
                        .withFailMessage("The winCounter should be equal to 1").isEqualTo(1)
        );
    }

    @Test
    void sendAnswerShouldRemoveGameSessionFromTheCurrentSessionWhenDefeated() {
        int size = GAMEPLAY.getCurrentSessions().size() - 1;
        for (int i = 0; i < 6; i++) GAMEPLAY.makeAMove(String.valueOf(i), GAMEPLAY.getCurrentSessions().get("2"));
        assertThat(GAMEPLAY.getCurrentSessions().size())
                .withFailMessage("The size of the CurrentSessions should be equal to 2")
                .isEqualTo(size);
    }

    @Test
    void sendAnswerShouldRemoveGameSessionFromTheCurrentSessionWhenWin() {
        int size = GAMEPLAY.getCurrentSessions().size() - 1;
        GAMEPLAY.makeAMove("A", GAMEPLAY.getCurrentSessions().get("3"));
        GAMEPLAY.makeAMove("B", GAMEPLAY.getCurrentSessions().get("3"));
        GAMEPLAY.makeAMove("C", GAMEPLAY.getCurrentSessions().get("3"));
        assertThat(GAMEPLAY.getCurrentSessions().size())
                .withFailMessage("The size of the CurrentSessions should be equal to 1")
                .isEqualTo(size);
    }

    @Test
    void makeAMoveShouldNotIncreaseLoseCounterAndWinCounter() {
        GameSession gameSession = new GameSession("abc", "1");
        gameSession.getUsedCharacter().add("t");
        for (int i = 0; i < 50; i++) GAMEPLAY.makeAMove("t", gameSession);
        assertAll(
                () -> assertThat(gameSession.getWinCounter()).isEqualTo(0),
                () -> assertThat(gameSession.getLoseCounter()).isEqualTo(0)
        );
    }

    @AfterAll
    static void clearCurrentSessions() {
        GAMEPLAY.getCurrentSessions().clear();
    }
}
