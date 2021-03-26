package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoValidRoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GuessTest {
    private Game game;

    @BeforeEach
    @DisplayName("Create a game once.")
    void createGameTest() {
        this.game = new Game();
        this.game.newRound("BAARD");
    }

    @Test
    @DisplayName("Hint is correct when it matches the expected hint.")
    void orderedHintTest() {
        doPartOfOrderedGuessTest("B....", "BONJE");
        doPartOfOrderedGuessTest("BA...", "BARST");
        doPartOfOrderedGuessTest("BAA.D", "DRAAD");
        doPartOfOrderedGuessTest("BAARD", "BAARD");
    }

    private void doPartOfOrderedGuessTest(String correct, String guess) {
        try {
            assertEquals(correct, game.guessWord(guess));
        } catch (NoValidRoundException | InvalidGuessException e) {
            fail("Should not throw "+e.getClass().getName());
        }
    }

    @Test
    @DisplayName("Check if Word is guessed.")
    void wordIsGuessedTest() {
        try {
            assertEquals("BAARD", game.guessWord("BAARD"));
        } catch (NoValidRoundException | InvalidGuessException e) {
            fail("Should not throw "+e.getClass().getName());
        }
    }

    @Test
    @DisplayName("Invalid word throw Exception")
    void wordIsInvalidTest() {
        assertThrows(InvalidGuessException.class, () -> game.guessWord("LANGERDANBAARD"));
        assertThrows(InvalidGuessException.class, () ->  game.guessWord("LBAARD"));
    }

    @Test
    @DisplayName("Invalid round throw Exception")
    void roundIsInvalidTest() {
        this.game = new Game(); //create a game without a round;

        assertThrows(NoValidRoundException.class, () -> game.guessWord("BONJE"));
        assertThrows(NoValidRoundException.class, () ->  game.guessWord("BAARD"));
    }
}