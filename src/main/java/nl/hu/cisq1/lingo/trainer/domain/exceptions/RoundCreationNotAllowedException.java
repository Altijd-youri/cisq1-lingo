package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class RoundCreationNotAllowedException extends Exception {
    public RoundCreationNotAllowedException() {
        super("Starting a new round for the game is now allowed.");
    }
}
