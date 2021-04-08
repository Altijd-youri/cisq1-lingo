package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @DisplayName("Multiple active rounds are not allowed.")
    @Test
    void multipleActiveRounds() throws GameRuleException {
        Game game = prepareGameWithSpecifiedAmountOfWonGames(0);

        game.newRound("ZEVEN");

        assertThrows(PreviousRoundNotFinishedException.class, () -> game.newRound("meter"));
    }

    @DisplayName("Guessing is not allowed when there is no  active round.")
    @Test
    void guessWhileNoActiveRound() throws GameRuleException {
        Game game = prepareGameWithSpecifiedAmountOfWonGames(0);

        assertThrows(NoActiveRoundException.class, () -> game.guessWord("ZEVEN"));
    }

    @DisplayName("Score is as expected after winning the first round on the first try.")
    @Test
    void scoreIsCorrectAfterOneRound() throws GameRuleException {
        Game game = prepareGameWithSpecifiedAmountOfWonGames(0);

        game.newRound("GUESS");
        game.guessWord("GUESS");

        assertEquals(25, game.getScore());
    }

    @DisplayName("Score is as expected after winning the first round on the first try.")
    @Test
    void scoreIsCorrectAfterThreeRounds() throws GameRuleException {
        Game game = prepareGameWithSpecifiedAmountOfWonGames(3);

        assertEquals(75, game.getScore());
    }

    @DisplayName("A game is only playable while the game is active.")
    @Test
    void whileGameIsNotLost() throws GameRuleException {
        Game game = new Game();

        game.newRound("WOORD");
        final int ALLOWEDATTEPTS = 5;
        for (int index = 0; index < ALLOWEDATTEPTS; index++) { //Five incorect guesses to LOSE the round and thus the game.
            game.guessWord("BAARD");
        }

        assertThrows(NoActiveGameException.class, () -> game.newRound("WOORD"));
    }

    @DisplayName("Prepare a game with specified amount of won games")
    private Game prepareGameWithSpecifiedAmountOfWonGames(int amountOfWonGames) throws GameRuleException {
        Game game = new Game();

        for (int index = 0; index < amountOfWonGames; index++) {
                game.newRound("WOORD");
                game.guessWord("WOORD");
        }
        return game;
    }

}