package nl.hu.cisq1.lingo.trainer.domain;

import java.util.List;

public class Feedback {
    private final List<Mark> marks;

    Feedback(List<Mark> marks) {
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
