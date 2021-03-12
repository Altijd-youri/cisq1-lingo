package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuessTest {
    public Round round;

    @BeforeEach
    void createNewRound() {
        this.round = new Round(1,"BAARD");
    }

    @DisplayName("Hint is correct when it match the expected hint.")
    @Test
    void hint() {
        Guess guess = new Guess(this.round);
        assertEquals("B....", guess.takeAGuess("BONJE"));

        // TODO - Test all cases from Gherkin trainer.feature. (Green (now) > red > refactor > green)
    }


}