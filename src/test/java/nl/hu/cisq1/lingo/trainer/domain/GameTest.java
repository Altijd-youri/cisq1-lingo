package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @DisplayName("Game is created.")
    @Test
    void game() {
        Game game = new Game();
        assertTrue(game != null);
    }

}