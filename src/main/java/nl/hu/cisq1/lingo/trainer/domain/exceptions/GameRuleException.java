package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public abstract class GameRuleException extends Exception {
    public GameRuleException(String message) {
        super(message);
    }
}
