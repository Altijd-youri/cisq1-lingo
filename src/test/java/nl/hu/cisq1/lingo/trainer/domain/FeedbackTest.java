package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {
    private final String WORD = "BAARD";

    @Test
    @DisplayName("Word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback(WORD, WORD);
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word is not guessed if all not letters are correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback(WORD, WORD.substring(0,3)+"A");
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word is valid if all letters are valid")
    void wordIsValid() {
        Feedback feedback = new Feedback(WORD, WORD);
        assertTrue(feedback.isWordValid());
    }

    @Test
    @DisplayName("Word is invalid if all letters are invalid")
    void wordIsInvalid() {
        Feedback feedback = new Feedback(WORD, WORD+"IN");
        assertFalse(feedback.isWordValid());
    }
}