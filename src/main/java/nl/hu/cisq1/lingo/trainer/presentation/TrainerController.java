package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.*;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameResponseDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GuessDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.management.InstanceNotFoundException;

@RestController
@RequestMapping("game")
public class TrainerController {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDTO startAGame() {

        Game game = trainerService.startNewGame();

        return new GameResponseDTO(game.getId(), game.getNumberOfRounds(), game.getScore(), game.getStatus().toString());
    }

    @GetMapping(value = "/{uuid}")
    public GameResponseDTO getStatus(@PathVariable String uuid) {
        try {
            Game game = trainerService.getStatus(uuid);

            return new GameResponseDTO(game.getId(), game.getNumberOfRounds(), game.getScore(), game.getStatus().toString());
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/{uuid}/round")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDTO startARound(@PathVariable String uuid) {

        try {
            Game game = trainerService.startNewRound(uuid);

            return new GameResponseDTO(game.getId(), game.getNumberOfRounds(), game.getScore(), game.getStatus().toString());
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (PreviousRoundNotFinishedException e) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (NoActiveGameException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @PatchMapping(value = "/{uuid}/guess")
    public GameResponseDTO guessWord(@PathVariable String uuid, @RequestBody GuessDTO guessDTO) {
        try {
            Game game = trainerService.guessWord(uuid, guessDTO.getWord());

            return new GameResponseDTO(game.getId(), game.getNumberOfRounds(), game.getScore(), game.getStatus().toString());
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (NoActiveRoundException e) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (InvalidGuessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
