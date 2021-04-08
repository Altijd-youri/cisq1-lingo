package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class GameNotFoundException extends GameRuleException {
    public GameNotFoundException() {
        super("Game with given Id doesn't exists.");
    }
}
