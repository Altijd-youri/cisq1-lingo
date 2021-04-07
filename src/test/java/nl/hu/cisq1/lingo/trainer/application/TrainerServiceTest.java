package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveGameException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.PreviousRoundNotFinishedException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    TrainerService service;
    WordService wordService;
    SpringGameRepository gameRepository;

    @BeforeEach
    void mockDependancies() {
        wordService = mock(WordService.class);
        gameRepository = mock(SpringGameRepository.class);

        service = new TrainerService(wordService, gameRepository);
    }

    @Test
    @DisplayName("Can start a new game.")
    void startNewGame() {

        service.startNewGame();

        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    @DisplayName("Can start a new round on a game.")
    void startNewRound() {
        Game game = new Game();

        when(gameRepository.findById(any())).thenReturn(Optional.of(game));

        try {
            service.startNewRound(game.getId());
        } catch (InstanceNotFoundException | NoActiveRoundException | PreviousRoundNotFinishedException e) {
           fail();
        } catch (Exception e) {
            fail("Failed with a non-game exception.");
        }

        verify(wordService, times(1)).provideRandomWord(anyInt());
    }

    @Test
    @DisplayName("Can place a guess on a active round.")
    void guessWord() {
        Game game = new Game();
        try {
            game.newRound("HALLO");
        } catch (PreviousRoundNotFinishedException | NoActiveGameException e) {
            fail("Exception thrown while preparing test.");
        }

        when(gameRepository.findById(any())).thenReturn(Optional.of(game));

        try {
            service.guessWord(game.getId(), "WRONG");
        } catch (InstanceNotFoundException | NoActiveRoundException | InvalidGuessException | NoActiveGameException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Get game status.")
    void getGameStatus() {
        Game game = new Game();
        String uuid = game.getId();

        when(gameRepository.findById(any())).thenReturn(Optional.of(game));

        try {
            assertEquals(game, service.getStatus(uuid));
        } catch (InstanceNotFoundException e) {
            fail("Exception thrown in getStatus(String uuid).");
        }
    }

    @Test
    @DisplayName("Exception when a non-existent game is requested.")
    void getGameStatusException() {
        String uuid = "0a0a0a0a-1111-0000-1111-0a0a0a0a0a0a";

        when(gameRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class, () -> service.getStatus(uuid));
    }
}