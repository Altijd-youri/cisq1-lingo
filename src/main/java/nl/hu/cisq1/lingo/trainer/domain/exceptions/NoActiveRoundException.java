package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class NoActiveRoundException extends GameRuleException {
    public NoActiveRoundException() {
        super("There is no active round for this game. First start a new round.");
    }
}
