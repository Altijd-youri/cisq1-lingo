package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;

public class Feedback {
    private final List<Mark> marks;
    private final String guess;

    Feedback(String guess, List<Mark> marks) {
        this.guess = guess;
        this.marks = marks;
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
