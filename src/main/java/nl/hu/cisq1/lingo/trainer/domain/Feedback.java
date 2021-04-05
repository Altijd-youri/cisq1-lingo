package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
public class Feedback {
    @Id
    @GeneratedValue
    private Long id;
    @Lob
    private List<Mark> marks;
    @Column
    private String guess;
    @Column
    private String answer;

    Feedback(String answer, String guess) {
        this.guess = guess;
        this.answer = answer;
        this.marks = this.compare();
    }

    public Feedback() {}

    private List<Character> stringToCharArray(String str) {
        List<Character> charList = new ArrayList<>();
        for (char ch : str.toCharArray()) {
            charList.add(ch);
        }
        return charList;
    }

    private List<Mark> markAll(Mark mark) {
        return IntStream.range(0, this.answer.length()).mapToObj(i -> mark).collect(Collectors.toList());
    }

    private List<Mark> compare() {
        if (guess.equals(answer)) {
            return markAll(Mark.CORRECT);
        }

        if (guess.length() != answer.length()) {
            return markAll(Mark.INVALID);
        }

        List<Character> answerCharacters = stringToCharArray(answer);
        List<Mark> markList = new ArrayList<>();
        for (int position = 0; position < guess.length(); position++) {
            if (guess.charAt(position) == answer.charAt(position)) {
                markList.add(Mark.CORRECT);
                answerCharacters.set(position, null);
            } else {
                markList.add(null);
            }
        }

        for (int position = 0; position < guess.length(); position++) {
            if (markList.get(position) == Mark.CORRECT) continue;

            char letter = guess.charAt(position);
            int next = answerCharacters.indexOf(letter);
            if (next > -1) {
                markList.set(position, Mark.PRESENT);
                answerCharacters.set(next, null);
            } else {
                markList.set(position, Mark.INCORRECT);
            }
        }
        return markList;
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
