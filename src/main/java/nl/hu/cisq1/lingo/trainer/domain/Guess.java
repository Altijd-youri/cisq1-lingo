package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;

import java.util.List;
import java.util.stream.Collectors;

public class Guess {
    private final Feedback feedback;
    private final Round round;
    private final StringBuilder hint;
    private final String correctWord;

    public Guess(Round round, String guessedWord) {
        this.round = round;
        this.correctWord = this.round.getWord();
        this.hint = new StringBuilder(".".repeat(round.wordLength()));

        this.feedback = this.generateFeedback(guessedWord);
    }

    private List<Feedback> getPreviousFeedback() {
        List<Guess> guessHistory = this.round.getGuesses();
        return guessHistory.stream().map(Guess::getFeedback).collect(Collectors.toList());
    }

    private Feedback generateFeedback(String guess) {
        return new Feedback(correctWord,guess);
    }

    public Feedback getFeedback() {
        return this.feedback;
    }

    private String createHint(List<Feedback> feedbackHistory) {
        for (Feedback feedback : feedbackHistory) {
            List<Mark> marks = feedback.getMarks();
            for (int position = 0; position < marks.size(); position++) {
                if (hint.charAt(position) != '.') continue;
                Mark mark = marks.get(position);
                if (mark.equals(Mark.CORRECT)) {
                    String character = String.valueOf(correctWord.charAt(position));
                    hint.replace(position,position+1,character);
                }
            }
        }
        return hint.toString();
    }

    public String takeAGuess() throws InvalidGuessException {
        if(!feedback.isWordValid()) throw new InvalidGuessException();
        List<Feedback> feedbackHistory = this.getPreviousFeedback();
        return createHint(feedbackHistory);
    }

    public boolean isWordGuessed() {
        return this.feedback.isWordGuessed();
    }
}
