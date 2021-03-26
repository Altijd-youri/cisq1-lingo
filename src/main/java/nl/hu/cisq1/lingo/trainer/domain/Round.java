package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private final int roundNumber;
    private String word = "";
    private Status status;
    private List<Guess> guesses;

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

    public List<Guess> getGuesses() {
        return guesses;
    }

    public String guessWord(String guessedWord) throws InvalidGuessException {
        Guess newGuess = new Guess(this, guessedWord);
        this.guesses.add(newGuess);

        String hint = newGuess.takeAGuess();

        if(newGuess.isWordGuessed()) {
            System.out.println("ROUND ENDED");
        }

        return hint;
    }
}
