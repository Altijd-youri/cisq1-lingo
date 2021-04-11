package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.*;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

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

    public Game getStatus(String uuid) throws GameNotFoundException {
        return getGame(uuid);
    }

    public Game startNewGame() {
        Game game = new Game();

        gameRepository.save(game);
        return game;
    }

    public Game startNewRound(String uuid) throws GameNotFoundException, PreviousRoundNotFinishedException, NoActiveGameException {
        Game game = getGame(uuid);

        int length = determineWordLength(game.getNumberOfRounds());
        String word = wordService.provideRandomWord(length);

        game.newRound(word);

        gameRepository.save(game);
        return game;
    }

    public Game guessWord(String uuid, String guess) throws GameNotFoundException, NoActiveRoundException, InvalidGuessException {
        Game game = getGame(uuid);

        game.guessWord(guess);

        gameRepository.save(game);
        return game;
    }

    private Game getGame(String uuid) throws GameNotFoundException {
        try {
            Optional<Game> optionalGame = gameRepository.findById(UUID.fromString(uuid));
            if (optionalGame.isEmpty()) throw new GameNotFoundException();
            return optionalGame.get();
        } catch (IllegalArgumentException e) {
            throw new GameNotFoundException();
        }
    }

    private int determineWordLength(int roundsBefore) {
        int num = roundsBefore + 1;
        int wordLength = 5;
        switch(calculateModules(num)) {
            case 0:
                wordLength = 7;
            case 1: break; // 5;
            case 2:
                wordLength =  6;
        }
        return wordLength;
    }

    private int calculateModules(int number) {
        final int modules = 3;
        while(number >= modules) {
            number = number - modules;
        }
        return number;
    }
}
