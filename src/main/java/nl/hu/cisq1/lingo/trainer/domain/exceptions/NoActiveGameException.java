package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class NoActiveGameException extends Exception {
    public NoActiveGameException() {
        super("This game is inactive and can't be interacted with.");
    }
}
