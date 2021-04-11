package nl.hu.cisq1.lingo.trainer.presentation.dto;

public class GuessDTO {
    private String word;

    public GuessDTO(String guess) {
        this.word = guess;
    }

    public GuessDTO() {}

    public String getWord() {
        return word;
    }
}
