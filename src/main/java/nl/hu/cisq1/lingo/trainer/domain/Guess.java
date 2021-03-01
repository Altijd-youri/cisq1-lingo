package nl.hu.cisq1.lingo.trainer.domain;

public class Guess {
    private Feedback feedback;
    private Round round;

    public Guess(Round round) {
        this.round = round;
    }

    private Feedback takeAGuess(String guess) {
        return new Feedback("","");
    }

    public String hint(String guess) {
        return "";
    }
}
