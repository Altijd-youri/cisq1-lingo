package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameRuleException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveRoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @DisplayName("Word length is expected.")
    @Test
    void wordLength() {
        Round round = new Round("BAARD");

        assertEquals(5, round.wordLength());
    }

    @DisplayName("The score is zero while the round is active.")
    @Test
    void ScoreIsZeroWhileRoundIsActive() throws GameRuleException {
        Round round = new Round("BAARD");
        assertEquals(0, round.getScore());

        round.guessWord("GUESS");
        assertEquals(0, round.getScore());
    }

    @DisplayName("Score is as expected when the word is not guessed before the round ends.")
    @Test
    void scoreIsCorrectAfterFiveGuesses() throws GameRuleException {
        Round round = prepareRoundWithSpecifiedWrongAttempts(5, "BAARD");

        assertEquals(5, round.getScore());
    }

    @DisplayName("Round is won when the word is guessed after four attempts.")
    @Test
    void RoundWonWhenGuessedAfterFourAttempts() throws GameRuleException {
        Round round = prepareRoundWithSpecifiedWrongAttempts(4, "BAARD");

        round.guessWord("BAARD");

        assertEquals(Status.WON, round.getStatus());
    }

    @DisplayName("Round is lost after the maximum of five attemts is reached before the word is guessed.")
    @Test
    void RoundLostAfterMaxAttempts() throws GameRuleException {
        Round round = prepareRoundWithSpecifiedWrongAttempts(5, "BAARD");

        assertEquals(Status.LOST, round.getStatus());
    }

    @DisplayName("Prepare a round with the specified amount of wrong attempts.")
    private Round prepareRoundWithSpecifiedWrongAttempts(int allowedAttempts, String correctWord) throws GameRuleException {
        Round round = new Round(correctWord);
        for (int index = 0; index < allowedAttempts; index++) {
            round.guessWord("NOTIT");
        }

        return round;
    }
}