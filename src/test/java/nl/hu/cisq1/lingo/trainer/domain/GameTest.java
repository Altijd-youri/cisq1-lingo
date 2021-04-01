package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundCreationNotAllowedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @DisplayName("Game is created.")
    @Test
    void game() {
        Game game = new Game();
        assertNotNull(game);
    }

    @DisplayName("Multiple active rounds are not allowed.")
    @Test
    void multipleActiveRounds() {
        Game game = new Game();
        try {
            game.newRound("zeven");
        } catch (RoundCreationNotAllowedException e) {
            fail("Arranging the game with a round in progress failed.");
        }

        assertThrows(RoundCreationNotAllowedException.class, () -> game.newRound("meter"));
    }

}