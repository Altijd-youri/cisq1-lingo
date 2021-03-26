package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class NoValidRoundException extends Exception {
    public NoValidRoundException() {
        super("There is no active round for this game. First start a new round.");
    }
}
