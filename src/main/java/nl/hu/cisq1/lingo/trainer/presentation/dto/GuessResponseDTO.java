package nl.hu.cisq1.lingo.trainer.presentation.dto;

import java.util.List;

public class GuessResponseDTO {
    private List<String> marks;
    private String guess;

    public GuessResponseDTO(String guess, List<String> marks) {
        this.guess = guess;
        this.marks = marks;
    }

    public String getGuess() {
        return guess;
    }

    public List<String> getMarks() {
        return marks;
    }
}
