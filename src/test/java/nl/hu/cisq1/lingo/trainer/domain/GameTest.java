package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveGameException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.PreviousRoundNotFinishedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @DisplayName("Multiple active rounds are not allowed.")
    @Test
    void multipleActiveRounds() {
        Game game = prepareGameWithSpecifiedAmountOfWonGames(0);
        try {
            game.newRound("ZEVEN");
        } catch (PreviousRoundNotFinishedException | NoActiveGameException e) {
            fail("Arranging the game with a round in progress failed.");
        }

        assertThrows(PreviousRoundNotFinishedException.class, () -> game.newRound("meter"));
    }

    @DisplayName("Score is as expected after winning the first round on the first try.")
    @Test
    void scoreIsCorrectAfterOneRound() {
        Game game = prepareGameWithSpecifiedAmountOfWonGames(0);
        try {
            game.newRound("GUESS");
        } catch (PreviousRoundNotFinishedException | NoActiveGameException e) {
            fail("Arranging the game with a round in progress failed.");
        }
        try {
            game.guessWord("GUESS");
        } catch (InvalidGuessException | NoActiveRoundException e) {
            fail("Exception thrown while arranging test.");
        }

        assertEquals(25, game.getScore());
    }

    @DisplayName("Score is as expected after winning the first round on the first try.")
    @Test
    void scoreIsCorrectAfterThreeRounds() {
        Game game = prepareGameWithSpecifiedAmountOfWonGames(3);

        assertEquals(75, game.getScore());
    }

    @DisplayName("A game is only playable while the game is active.")
    @Test
    void whileGameIsNotLost() {
        Game game = new Game();
        try {
            game.newRound("WOORD");
        } catch (PreviousRoundNotFinishedException | NoActiveGameException e) {
            fail("Exception thrown while arranging test.");
        }

        int allowedAttempts = 5;
        for (int index = 0; index < allowedAttempts; index++) { //Five incorect guesses to LOSE the round and thus the game.
            try {
                game.guessWord("BAARD");
            } catch (InvalidGuessException | NoActiveRoundException e) {
                fail("Exception thrown while arranging test.");
            }
        }

        assertThrows(NoActiveGameException.class, () -> game.newRound("WOORD"));
    }

    @DisplayName("Prepare a game with specified amount of won games")
    private Game prepareGameWithSpecifiedAmountOfWonGames(int amountOfWonGames) {
        Game game = new Game();

        for (int index = 0; index < amountOfWonGames; index++) {
            try { //Start new Round
                game.newRound("WOORD");
            } catch (PreviousRoundNotFinishedException | NoActiveGameException e) {
                fail("Exception thrown while arranging test.");
            }

            try { //Correct Guess to End round
                game.guessWord("WOORD");
            } catch (InvalidGuessException | NoActiveRoundException e) {
                fail("Exception thrown while arranging test.");
            }
        }
        return game;
    }

}