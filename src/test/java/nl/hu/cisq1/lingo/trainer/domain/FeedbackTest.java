package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {
    private final String CORRECTWORD = "BAARD";
    private final List CORRECTMARKS = List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT);

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

    @Test
    @DisplayName("Feedback is correct when it mataches the expected feedback.")
    void feedbackIsCorrect() {
        //I added one test at the time, then improved/refactored and added the next one.
        Feedback feedback = new Feedback(CORRECTWORD, "BONJE");
        assertEquals( List.of(Mark.CORRECT, Mark.INCORRECT, Mark.INCORRECT, Mark.INCORRECT, Mark.INCORRECT), feedback.getMarks());

        //This one failed, so I added a better check for Present.
        feedback = new Feedback(CORRECTWORD, "BARST");
            assertEquals(List.of(Mark.CORRECT, Mark.CORRECT, Mark.PRESENT, Mark.INCORRECT, Mark.INCORRECT), feedback.getMarks());

        //This one failed, so I added a mechanic to determine if a letters exists more than one time and if the second time the letter should be marked correct.
        feedback = new Feedback(CORRECTWORD, "DRAAD");
        assertEquals(List.of(Mark.INCORRECT, Mark.PRESENT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT), feedback.getMarks());

        //This one failed, so I added a mechanic to save Present marks across feedback items.
        feedback = new Feedback(CORRECTWORD, CORRECTWORD);
        assertEquals(CORRECTMARKS, feedback.getMarks());
    }
}