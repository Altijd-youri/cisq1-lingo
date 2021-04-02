package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.MaxRoundsReachedException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoValidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.PreviousRoundNotFinishedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game {
    private List<Round> rounds = new ArrayList<>();
    private int score;

    public Game() {
        this.score = 0;
    }

    public void newRound(String word) throws PreviousRoundNotFinishedException, MaxRoundsReachedException {
        if (maxRoundsReached()) throw new MaxRoundsReachedException();
        if (aRoundisActive()) throw new PreviousRoundNotFinishedException();

        Round newRound = new Round(word);
        this.rounds.add(newRound);
    }

    public String guessWord(String guessedWord) throws NoValidRoundException, InvalidGuessException {
        Optional<Round> lastRoundOptional = this.getLastRound();
        Round lastRound = lastRoundOptional.orElseThrow(NoValidRoundException::new);
        return lastRound.guessWord(guessedWord);
    }

    private Optional<Round> getLastRound() {
        int size = this.rounds.size();
        if (size > 0) {
            int index = size - 1;
            return Optional.ofNullable(this.rounds.get(index));
        }
        return Optional.empty();
    }

    private boolean maxRoundsReached() {
        return this.rounds.size() >= 3;
    }

    private boolean aRoundisActive() {
        Optional<Round> lastRound = getLastRound();
        return lastRound.isPresent() && lastRound.get().isActive();
    }
}
