package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveRoundException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Round {
    @Id
    private UUID id = UUID.randomUUID();
    @Column
    private String word;
    @Column
    private Status status;
    @OneToMany(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private List<Guess> guesses;
    @Column
    private String hint;

    public Round(String word) {
        this.status = Status.ACTIVE;
        this.word = word;
        this.guesses = new ArrayList<>();
        this.hint = createFirstHint();
    }

    private String createFirstHint() {
        StringBuilder builder = new StringBuilder();
        builder.append(word.charAt(0));
        builder.append(".".repeat(wordLength()-1));

        return builder.toString();
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
    public String guessWord(String guessedWord) throws InvalidGuessException, NoActiveRoundException {
        if(!isActive()) throw new NoActiveRoundException();

        Guess newGuess = new Guess(this, guessedWord);
        this.guesses.add(newGuess);

        hint = newGuess.takeAGuess();

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

    public String getId() {
        return id.toString();
    }

    public String getHint() {
        return hint;
    }

    private void markAsEnded(Status status) {
        this.status = status;
    }

    private int getAmountOfGuesses() {
        return this.guesses.size();
    }
}
