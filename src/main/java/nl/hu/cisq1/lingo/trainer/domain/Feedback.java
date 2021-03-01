package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Feedback {
    private final List<Mark> marks;
    private final String guess;
    private final String answer;

    Feedback(String answer, String guess) {
        this.guess = guess;
        this.answer = answer;
        this.marks = this.compare();
    }

    private List<Mark> compare() {
        List<Mark> marks = new ArrayList<>();
        String guessCharaters = guess;
        String answerCharaters = answer;

        if (answer.length() != guess.length()) {
            marks = IntStream.range(0, answer.length()).mapToObj(i -> Mark.INVALID).collect(Collectors.toList());
            return marks;
        }

        int answerPosition = 0;
        while(answerPosition < answerCharaters.length()) {
            Character answerletter = answerCharaters.charAt(answerPosition);
            Character guessLetter = guessCharaters.charAt(answerPosition);

            Mark mark = Mark.INCORRECT;
            int addition = 1;
            if (answerletter.equals(guessLetter)) {
                mark = Mark.CORRECT;

            } else if (answerCharaters.contains(guessLetter.toString())) {
                int index = answerCharaters.indexOf(guessLetter);
                if (index > 0) { //Letter is contained in answer
                    int indexSecond = guessCharaters.indexOf(guessLetter,answerPosition+1);
                    if(indexSecond!=-1) { //Letter is contained a second time in the guess
                        Character nextOccurrenceInAnswer = answerCharaters.charAt(indexSecond);
                        Character nextOccurrenceInGuess = guessCharaters.charAt(indexSecond);
                        if (!nextOccurrenceInAnswer.equals(nextOccurrenceInGuess)) { //Next occurance in answer and guess do not match.
                            mark = Mark.PRESENT;
                        }
                    } else {
                        mark = Mark.PRESENT;
                    }
                }
            }
            marks.add(mark);
            answerPosition = answerPosition + addition;
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
