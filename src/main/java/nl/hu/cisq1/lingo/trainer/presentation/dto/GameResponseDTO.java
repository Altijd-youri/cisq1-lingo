package nl.hu.cisq1.lingo.trainer.presentation.dto;

public class GameResponseDTO {
    private String id;
    private int rounds;
    private int score;
    private String status;
    private RoundResponseDTO round;

    public GameResponseDTO() {}

    public GameResponseDTO(String id, int rounds, int score, String status) {
        this.id = id;
        this.rounds = rounds;
        this.score = score;
        this.status = status;
    }

    public GameResponseDTO(String id, int rounds, int score, String status, RoundResponseDTO round) {
        this.id = id;
        this.rounds = rounds;
        this.score = score;
        this.status = status;
        this.round = round;
    }

    public String getId() {
        return id;
    }

    public int getRounds() {
        return rounds;
    }

    public int getScore() {
        return score;
    }

    public String getStatus() {
        return status;
    }

    public RoundResponseDTO getRound() {
        return round;
    }
}
