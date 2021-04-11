package nl.hu.cisq1.lingo.trainer.presentation.dto;

import java.util.List;

public class RoundResponseDTO {
    private String id;
    private String status;
    private String hint;
    private List<GuessResponseDTO> guesses;

    public RoundResponseDTO(String id, String status, String hint, List<GuessResponseDTO> guesses) {
        this.id = id;
        this.status = status;
        this.hint = hint;
        this.guesses = guesses;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getHint() {
        return hint;
    }

    public List<GuessResponseDTO> getGuesses() {
        return guesses;
    }
}
