package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
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

    @DisplayName("Round is won when the word is guessed after four attempts.")
    @Test
    void RoundWonWhenGuessedAfterFourAttempts() {
        Round round = prepareRoundWithSpecifiedWrongAttempts(4, "BAARD");
        try {
            round.guessWord("BAARD");
        } catch (InvalidGuessException e) {
            fail("Excpection thrown while arranging test.");
        }

        assertEquals(Status.WON, round.getStatus());
    }

    @DisplayName("Round is lost after the maximum of five attemts is reached before the word is guessed.")
    @Test
    void RoundLostAfterMaxAttempts() {
        Round round = prepareRoundWithSpecifiedWrongAttempts(5, "BAARD");

        assertEquals(Status.LOST, round.getStatus());
    }


    private Round prepareRoundWithSpecifiedWrongAttempts(int allowedAttempts, String correctWord) {
        Round round = new Round(correctWord);
        try {
            for (int index = 0; index <= allowedAttempts; index++) {
                round.guessWord("NOTIT");
            }
        } catch (InvalidGuessException e) {
            fail("Excpection thrown while arranging test.");
        }
        return round;
    }
}