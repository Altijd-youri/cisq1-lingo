package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Round {
    @Id
    @GeneratedValue
    private UUID id;
    @Column
    private String word;
    @Column
    private Status status;
    @OneToMany
    private List<Guess> guesses;

    public Round(String word) {
        this.status = Status.ACTIVE;
        this.word = word;
        this.guesses = new ArrayList<>();
    }

    public Round() {}

    public int wordLength() {
        return word.length();
    }

    public String getWord() {
        return this.word;
    }

    public List<Guess> getGuesses() {
        return this.guesses;
    }

    /**
     * Calculate and returns the round score
     * @return 0, while the round isn't yet finished, the actual score after the round has finished.
     */
    public int getScore() {
        if (this.isActive()) return 0;
        final int BASE = 5;
        return BASE * (BASE - getAmountOfGuesses()) + BASE;
    }

    /**
     *
     * @param guessedWord The word to guess.
     * @return String - Hint based on the guess.
     * @throws InvalidGuessException Thrown when the length of the guessed word is incorrect.
     */
    public String guessWord(String guessedWord) throws InvalidGuessException {
        Guess newGuess = new Guess(this, guessedWord);
        this.guesses.add(newGuess);

        String hint = newGuess.takeAGuess();

        if(newGuess.isWordGuessed()) {
            this.markAsEnded(Status.WON);
            return hint;
        }
        if(this.getGuesses().size() >= 5) {
            this.markAsEnded(Status.LOST);
        }

        return hint;
    }

    public Status getStatus() {
        return this.status;
    }

    public boolean isActive() {
        return getStatus().equals(Status.ACTIVE);
    }

    private void markAsEnded(Status status) {
        this.status = status;
    }

    private int getAmountOfGuesses() {
        return this.guesses.size();
    }
}
