package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveGameException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.PreviousRoundNotFinishedException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TrainerService {
    private final WordService wordService;

    private final SpringGameRepository gameRepository;


    public TrainerService(WordService wordService, SpringGameRepository gameRepository) {
        this.wordService = wordService;
        this.gameRepository = gameRepository;
    }

    public Game getStatus(String uuid) throws InstanceNotFoundException {
        return getGame(uuid);
    }

    public Game startNewGame() {
        Game game = new Game();

        gameRepository.save(game);
        return game;
    }

    public Game startNewRound(String uuid) throws InstanceNotFoundException, NoActiveRoundException, PreviousRoundNotFinishedException, NoActiveGameException {
        Game game = getGame(uuid);

        int length = 5; //TODO - Implement length grow and shrink cycle.
        String word = wordService.provideRandomWord(length);

        game.newRound(word);

        gameRepository.save(game);
        return game;
    }

    public Game guessWord(String uuid, String guess) throws InstanceNotFoundException, NoActiveRoundException, InvalidGuessException, NoActiveGameException {
        Game game = getGame(uuid);

        game.guessWord(guess);

        gameRepository.save(game);
        return game;
    }

    private Game getGame(String uuid) throws InstanceNotFoundException {
        Optional<Game> optionalGame = gameRepository.findById(UUID.fromString(uuid));
        if (optionalGame.isEmpty()) throw new InstanceNotFoundException("Game with given Id doesn't exists.");

        return optionalGame.get();
    }
}
