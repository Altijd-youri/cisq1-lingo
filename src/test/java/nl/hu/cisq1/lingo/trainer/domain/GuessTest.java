package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GuessTest {
    private Game game;

    @BeforeEach
    @DisplayName("Create a game once for each test")
    void createGameTest() throws GameRuleException {
        this.game = new Game();
        this.game.newRound("BAARD");
    }

    @Test
    @DisplayName("Hint is correct when it matches the expected hint.")
    void orderedHintTest() throws GameRuleException {
        doPartOfOrderedGuessTest("B....", "BONJE");
        doPartOfOrderedGuessTest("BA...", "BARST");
        doPartOfOrderedGuessTest("BAA.D", "DRAAD");
        doPartOfOrderedGuessTest("BAARD", "BAARD");
    }

    private void doPartOfOrderedGuessTest(String correct, String guess) throws GameRuleException {
        assertEquals(correct, game.guessWord(guess));
    }

    @Test
    @DisplayName("Check if Word is guessed.")
    void wordIsGuessedTest() throws GameRuleException {
        assertEquals("BAARD", game.guessWord("BAARD"));
    }

    @Test
    @DisplayName("Invalid word throws Exception")
    void wordIsInvalidTest() {
        assertThrows(InvalidGuessException.class, () -> game.guessWord("LANGERDANBAARD"));
        assertThrows(InvalidGuessException.class, () ->  game.guessWord("LBAARD"));
    }

    @Test
    @DisplayName("Invalid round throws Exception")
    void roundIsInvalidTest() {
        this.game = new Game(); //create a game without a round;

        assertThrows(NoActiveRoundException.class, () -> game.guessWord("BONJE"));
        assertThrows(NoActiveRoundException.class, () ->  game.guessWord("BAARD"));
    }
}