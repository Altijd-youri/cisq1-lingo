package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.PreviousRoundNotFinishedException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.*;

@Entity
public class Game {
    @Id
    private UUID id = UUID.randomUUID();
    @OneToMany
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

    /**
     *
     * @param word The word to be guessed.
     * @throws PreviousRoundNotFinishedException Thrown when there already is another active round for this game.
     * @throws NoActiveRoundException Thrown when the game has ended. (Start a new Game)
     */
    public void newRound(String word) throws PreviousRoundNotFinishedException, NoActiveRoundException {
        if (!gameIsActive()) throw new NoActiveRoundException();
        if (roundIsActive()) throw new PreviousRoundNotFinishedException();

        Round newRound = new Round(word);
        this.rounds.add(newRound);
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
        int size = this.rounds.size();
        if (size > 0) {
            int index = size - 1;
            return Optional.ofNullable(this.rounds.get(index));
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

    public String getId() {
        return id.toString();
    }
}
