package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.MaxRoundsReachedException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoValidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.PreviousRoundNotFinishedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @DisplayName("Multiple active rounds are not allowed.")
    @Test
    void multipleActiveRounds() {
        Game game = new Game();
        try {
            game.newRound("ZEVEN");
        } catch (PreviousRoundNotFinishedException | MaxRoundsReachedException e) {
            fail("Arranging the game with a round in progress failed.");
        }

        assertThrows(PreviousRoundNotFinishedException.class, () -> game.newRound("meter"));
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
                fail("Excpection thrown while arranging test.");
            }

            try { //Correct Guess to End round
                game.guessWord("WOORD");
            } catch (InvalidGuessException | NoValidRoundException e) {
                fail("Excpection thrown while arranging test.");
            }
        }
        return game;
    }

}