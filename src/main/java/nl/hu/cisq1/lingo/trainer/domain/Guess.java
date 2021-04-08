package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Guess {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    private Feedback feedback;
    @Transient
    private Round round;
    @Column
    private String correctWord;

    public Guess(Round round, String guessedWord) {
        this.round = round;
        this.correctWord = this.round.getWord();

        this.feedback = this.generateFeedback(guessedWord);
    }

    public Guess() {}

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
        StringBuilder hint = new StringBuilder(".".repeat(round.wordLength()));
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
