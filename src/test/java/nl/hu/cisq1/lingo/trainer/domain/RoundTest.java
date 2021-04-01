package nl.hu.cisq1.lingo.trainer.domain;

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
}