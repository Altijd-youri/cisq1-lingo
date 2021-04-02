package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class MaxRoundsReachedException extends Exception {
    public MaxRoundsReachedException() {
        super("Game already reached the maximum amount of playable rounds.");
    }
}
