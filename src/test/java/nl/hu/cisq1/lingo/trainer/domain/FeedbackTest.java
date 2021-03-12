package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {
    private static final String CORRECTWORD = "BAARD";
    private static final List CORRECTMARKS = List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT);

    @Test
    @DisplayName("Word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback(CORRECTWORD, CORRECTWORD);
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word is not guessed if all not letters are correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback(CORRECTWORD, CORRECTWORD.substring(0,3)+"A");
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word is valid if all letters are valid")
    void wordIsValid() {
        Feedback feedback = new Feedback(CORRECTWORD, CORRECTWORD);
        assertTrue(feedback.isWordValid());
    }

    @Test
    @DisplayName("Word is invalid if all letters are invalid")
    void wordIsInvalid() {
        Feedback feedback = new Feedback(CORRECTWORD, CORRECTWORD +"IN");
        assertFalse(feedback.isWordValid());
    }

    private static Stream<Arguments> feedbackTestcases() {
        return Stream.of(
                Arguments.of(List.of(Mark.CORRECT, Mark.INCORRECT, Mark.INCORRECT, Mark.INCORRECT, Mark.INCORRECT), "BAARD", "BONJE"),
                Arguments.of(List.of(Mark.CORRECT, Mark.CORRECT, Mark.PRESENT, Mark.INCORRECT, Mark.INCORRECT), "BAARD", "BARST"),
                Arguments.of(List.of(Mark.INCORRECT, Mark.PRESENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT), "BAARD", "DRAAD"),
                Arguments.of(CORRECTMARKS, CORRECTWORD, CORRECTWORD),
                Arguments.of(List.of(Mark.PRESENT, Mark.PRESENT, Mark.CORRECT, Mark.PRESENT, Mark.INCORRECT), "BAADR", "DRAAD")
        );
    }

    @ParameterizedTest
    @MethodSource("feedbackTestcases")
    @DisplayName("Feedback is correct when it matches the expected feedback.")
    void feedbackIsCorrect(List<Mark> correctMarks, String correctWord, String guessedWord) {
        Feedback feedback = new Feedback(correctWord, guessedWord);
        assertEquals(correctMarks, feedback.getMarks());
    }
}