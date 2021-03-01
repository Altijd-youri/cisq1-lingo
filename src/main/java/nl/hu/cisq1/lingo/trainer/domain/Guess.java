package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Guess {
    private Feedback feedback;
    private Round round;
    private final String WORD = "Baard"; //Temporary
    private final int LENGTH = 5; //Temporary

    public Guess(Round round) {
        this.round = round;
    }

    private Feedback getFeedback(String guess) {
        this.feedback = (this.feedback==null) ? new Feedback(WORD,guess) : this.feedback;
        return this.feedback;
    }

    private String createHint(List<Feedback> feedbackHistory) {
        StringBuilder hintBuilder = new StringBuilder(".".repeat(LENGTH));
        for(Feedback feedback : feedbackHistory) {
            if(feedback.isWordValid()) {
                List<Mark> marks = feedback.getMarks();
                for(int markCount = 0; markCount < marks.size(); markCount++) {
                    if(Mark.CORRECT.equals(marks.get(markCount))) {
                        Character letter = WORD.charAt(markCount);
                        hintBuilder.replace(markCount, markCount+1,letter.toString());
                    }

                }
            }
        }
        return hintBuilder.toString();
    }

    public String takeAGuess(String guess) {
        List<Feedback> feedbackHistory = new ArrayList<>();
        feedbackHistory.add(this.getFeedback(guess));
        return createHint(feedbackHistory);
    }
}
