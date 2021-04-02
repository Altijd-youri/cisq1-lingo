package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.MaxRoundsReachedException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.PreviousRoundNotFinishedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @DisplayName("Multiple active rounds are not allowed.")
    @Test
    void multipleActiveRounds() {
        Game game = prepareGameWithSpecifiedAmountOfWonGames(0);
        try {
            game.newRound("ZEVEN");
        } catch (PreviousRoundNotFinishedException | MaxRoundsReachedException e) {
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
        } catch (PreviousRoundNotFinishedException | MaxRoundsReachedException e) {
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

    @DisplayName("A game may not have more than three rounds.")
    @Test
    void noMoreThanThreeRounds() {
        Game game = prepareGameWithSpecifiedAmountOfWonGames(3);

        assertThrows(MaxRoundsReachedException.class, () -> game.newRound("WOORD"));
    }

    @DisplayName("Prepare a game with specified amount of won games")
    private Game prepareGameWithSpecifiedAmountOfWonGames(int amountOfWonGames) {
        Game game = new Game();

        for (int index = 0; index < amountOfWonGames; index++) {
            try { //Start new Round
                game.newRound("WOORD");
            } catch (PreviousRoundNotFinishedException | MaxRoundsReachedException e) {
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