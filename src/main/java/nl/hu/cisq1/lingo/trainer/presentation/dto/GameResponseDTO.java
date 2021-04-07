package nl.hu.cisq1.lingo.trainer.presentation.dto;

public class GameResponseDTO {
    private String id;
    private int rounds;
    private int score;
    private String status;

    public GameResponseDTO(String id, int rounds, int score, String status) {
        this.id = id;
        this.rounds = rounds;
        this.score = score;
        this.status = status;
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
}
