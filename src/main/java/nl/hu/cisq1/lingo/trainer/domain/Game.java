package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveGameException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.PreviousRoundNotFinishedException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.*;

@Entity
public class Game {
    @Id
    private UUID id = UUID.randomUUID();
    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<Round> rounds = new ArrayList<>();
    @Column
    private int score;
    @Column
    private Status status;

    public Game() {
        this.score = 0;
        this.status = Status.ACTIVE;
    }

    public int getScore() {
        return this.score;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public int getNumberOfRounds() {
        return getRounds().size();
    }

    public Status getStatus() {
        return status;
    }

    public String getId() {
        return id.toString();
    }

    /**
     *
     * @param word The word to be guessed.
     * @throws PreviousRoundNotFinishedException Thrown when there already is another active round for this game.
     * @throws NoActiveGameException Thrown when the game has ended. (Start a new Game)
     */
    public void newRound(String word) throws PreviousRoundNotFinishedException, NoActiveGameException {
        if (!gameIsActive()) throw new NoActiveGameException();
        if (roundIsActive()) throw new PreviousRoundNotFinishedException();

        Round newRound = new Round(word);
        getRounds().add(newRound);
    }

    /**
     *
     * @param guessedWord The word to guess in the active round.
     * @return String - Hint based on the guess.
     * @throws NoActiveRoundException Thrown when there is no active round.
     * @throws InvalidGuessException Thrown when the length of the guessed word is incorrect.
     */
    public String guessWord(String guessedWord) throws NoActiveRoundException, InvalidGuessException {
        Optional<Round> lastRoundOptional = this.getLastRound();
        Round lastRound = lastRoundOptional.orElseThrow(NoActiveRoundException::new);
        if (!lastRound.isActive()) throw new NoActiveRoundException();
        if (!gameIsActive()) throw new NoActiveRoundException();

        String hint = lastRound.guessWord(guessedWord);
        addToScore(lastRound.getScore());

        switch (lastRound.getStatus()) {
            case LOST:
                markAsEnded(Status.LOST);
                break;
            case WON:
            case ACTIVE:
            default:
                break;
        }

        return hint;
    }

    private Optional<Round> getLastRound() {
        int size = getNumberOfRounds();
        if (size > 0) {
            int index = size - 1;
            return Optional.ofNullable(getRounds().get(index));
        }
        return Optional.empty();
    }

    private void addToScore(int points) {
        this.score += points;
    }

    private boolean gameIsActive() {
        return Status.ACTIVE.equals(this.status);
    }

    private boolean roundIsActive() {
        Optional<Round> lastRound = getLastRound();
        return lastRound.isPresent() && lastRound.get().isActive();
    }

    private void markAsEnded(Status status) {
        this.status = status;
    }
}
