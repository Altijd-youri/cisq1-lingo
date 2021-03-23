package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;

public class Round {
    private final int roundNumber;
    private String word = "";
    private Status status;
    private ArrayList<Guess> guesses;

    public Round(int roundNumber, String word) {
        this.roundNumber = roundNumber;
        this.status = Status.ACTIVE;
        this.word = word;
        this.guesses = new ArrayList<>();
    }

    public int wordLength() {
        return word.length();
    }

    public String getWord() {
        return this.word;
    }

    public ArrayList<Guess> getGuesses() {
        return guesses;
    }

    public boolean addGuess(Guess guess) {
        return this.guesses.add(guess);
    }
}
