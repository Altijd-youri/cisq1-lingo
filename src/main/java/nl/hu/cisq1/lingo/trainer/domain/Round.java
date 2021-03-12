package nl.hu.cisq1.lingo.trainer.domain;

public class Round {
    private final int roundNumber;
    private String word = "";
    private Status status;

    public Round(int roundNumber, String word) {
        this.roundNumber = roundNumber;
        this.status = Status.ACTIVE;
        this.word = word;
    }

    public int wordLength() {
        return word.length();
    }
}
