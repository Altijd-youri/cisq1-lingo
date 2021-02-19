package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.List;

public class Feedback {
    private final List<Mark> marks;
    private final String guess;
    private final String answer;

    Feedback(String guess, String answer) {
        this.guess = guess;
        this.answer = answer;
        this.marks = this.compare();
    }

    private List<Mark> compare() {
        List<Mark> marks = new ArrayList<>();
        String guessCharaters = guess;
        for (int answerPosition = 0; answerPosition < answer.length(); answerPosition++) {
            Character answerletter = answer.charAt(answerPosition);
            for (int guessPosition = 0; guessPosition < guess.length(); guessPosition++) {
                Character guessLetter = guess.charAt(answerPosition);

                if (answerletter.equals(guessLetter)) {
                    marks.add(Mark.CORRECT);
                    continue;
                }
                if (answer.contains(guessCharaters)) {
                    marks.add(Mark.PRESENT);
                    continue;
                }
                marks.add(Mark.INCORRECT);
            }
        }
        return marks;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public boolean isWordGuessed() {
        return this.marks.stream()
                .allMatch(Mark.CORRECT::equals);
    }

    public boolean isWordValid() {
        return this.marks.stream()
                .noneMatch(Mark.INVALID::equals);
    }

}
