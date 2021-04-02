package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class PreviousRoundNotFinishedException extends Exception {
    public PreviousRoundNotFinishedException() {
        super("Game already has an active round.");
    }
}
