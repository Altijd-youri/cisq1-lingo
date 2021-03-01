package nl.hu.cisq1.lingo.trainer.domain;

public class Round {
    private int roundNumber;
    private String word = "";
    private Status status;

    public Round(int roundNumber, int wordLength) {
        this.roundNumber = roundNumber;
        this.status = Status.ACTIVE;
        this.word = "";
    }

    public int wordLength() {
        return word.length();
    }
}
