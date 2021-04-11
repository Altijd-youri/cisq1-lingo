package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Guess;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.*;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameResponseDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GuessDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GuessResponseDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.RoundResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("game")
public class TrainerController {

    private final TrainerService trainerService;

    private static GameResponseDTO createResponseDTO(Game game) {
        if (game.getNumberOfRounds() > 0) {
            Round round = game.getRound().get();

            List<Guess> guesses = round.getGuesses();
            List<GuessResponseDTO> guessDTOs = new ArrayList<>();

            for (int index=0; index < guesses.size(); index++) {
                Guess guess = guesses.get(index);
                List<Mark> marks = guess.getFeedback().getMarks();
                List<String> stringifyMarks = marks.stream().map(mark -> mark.toString()).collect(Collectors.toList());
                guessDTOs.add(new GuessResponseDTO(guess.getGuessed(), stringifyMarks));
            }

            RoundResponseDTO roundDTO = new RoundResponseDTO(round.getId(), round.getStatus().toString(), round.getHint(), guessDTOs);
            return new GameResponseDTO(game.getId(), game.getNumberOfRounds(), game.getScore(), game.getStatus().toString(), roundDTO);
        }
        return new GameResponseDTO(game.getId(), game.getNumberOfRounds(), game.getScore(), game.getStatus().toString());
    }

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDTO startAGame() {

        Game game = trainerService.startNewGame();

        return TrainerController.createResponseDTO(game);
    }

    @GetMapping(value = "/{uuid}")
    public GameResponseDTO getStatus(@PathVariable String uuid) {
        try {
            Game game = trainerService.getStatus(uuid);

            return TrainerController.createResponseDTO(game);
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/{uuid}/round")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResponseDTO startARound(@PathVariable String uuid) {

        try {
            Game game = trainerService.startNewRound(uuid);

            return TrainerController.createResponseDTO(game);
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

            return TrainerController.createResponseDTO(game);
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (NoActiveRoundException e) {
            throw  new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (InvalidGuessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
