package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class InvalidGuessException extends Exception {
    public InvalidGuessException() {
        super("The guessed word is invalid.");
    }
}
