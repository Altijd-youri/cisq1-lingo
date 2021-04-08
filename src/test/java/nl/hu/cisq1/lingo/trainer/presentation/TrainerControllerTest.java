package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class TrainerControllerTest {
    private static TrainerService service;
    private TrainerController controller;

    @BeforeAll
    static void beforeAll() {
        service = mock(TrainerService.class);
    }

    @BeforeEach
    void beforeEach() {
        this.controller = new TrainerController(service);
    }

    @Test
    @DisplayName("Game creation is triggered from lower layers is triggered.")
    void startAGame() {
        Game game = new Game();
        when(service.startNewGame()).thenReturn(game);

        assertNotNull(controller.startAGame());
    }

    @Test
    @DisplayName("Game status action trigger lower layers.")
    void getStatus() throws GameNotFoundException {
        Game game = new Game();

        when(service.getStatus(anyString())).thenReturn(game);

        assertNotNull(controller.getStatus("anUuidYouSeeHere"));

        verify(service, times(1)).getStatus(anyString());
    }

    @Test
    @DisplayName("Start round action triggers lower layers.")
    void startARound() throws GameRuleException {
        Game game = new Game();

        when(service.startNewRound(anyString())).thenReturn(game);

        assertNotNull(controller.startARound("anUuidYouSeeHere"));

        verify(service, times(1)).startNewRound(anyString());
    }

    @Test
    @DisplayName("Guess word action triggers lower layers.")
    void guessWord() throws GameRuleException {
        Game game = new Game();

        game.newRound("SEVEN");

        when(service.guessWord(anyString(), anyString())).thenReturn(game);

        assertNotNull(controller.guessWord("anUuidYouSeeHere","GUESS"));

        verify(service, times(1)).guessWord(anyString(), anyString());
    }
}