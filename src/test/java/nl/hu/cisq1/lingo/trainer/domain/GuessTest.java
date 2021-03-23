package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GuessTest {

    private static Stream<Arguments> hintTestcases() {
        Round round = new Round(1,"BAARD");
        return Stream.of(
                Arguments.of(round, "B....", "BONJE"),
                Arguments.of(round, "BA...", "BARST"),
                Arguments.of(round, "BAA.D", "DRAAD"),
                Arguments.of(round, "BAARD", "BAARD")
        );
    }

    @ParameterizedTest
    @MethodSource("hintTestcases")
    @DisplayName("Hint is correct when it match the expected hint.")
    void hint(Round round, String correctHint, String guessedWord) {
        Guess guess = new Guess(round, guessedWord);
        assertEquals(correctHint, guess.takeAGuess());
    }


}