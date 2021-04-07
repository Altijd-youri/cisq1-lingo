package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.TrainerService;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;

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
    void getStatus() {
        Game game = new Game();
        try {
            when(service.getStatus(anyString())).thenReturn(game);
        } catch (InstanceNotFoundException e) {}

        assertNotNull(controller.getStatus("anUuidYouSeeHere"));
        try {
            verify(service, times(1)).getStatus(anyString());
        } catch (Exception e) {
            fail("Exception thrown while verifying the correct methods were called.");
        }
    }

    @Test
    @DisplayName("Start round action triggers lower layers.")
    void startARound() {
        Game game = new Game();
        try {
            when(service.startNewRound(anyString())).thenReturn(game);
        } catch (Exception e) {
            fail("Exception thrown while preparing test.");
        }

        assertNotNull(controller.startARound("anUuidYouSeeHere"));
        try {
            verify(service, times(1)).startNewRound(anyString());
        } catch (Exception e) {
            fail("Exception thrown while verifying the correct methods were called.");
        }
    }

    @Test
    @DisplayName("Guess word action triggers lower layers.")
    void guessWord() {
        Game game = new Game();
        try {
            game.newRound("SEVEN");

            when(service.guessWord(anyString(), anyString())).thenReturn(game);
        } catch (Exception e) {
            fail("Exception thrown while preparing test.");
        }

        assertNotNull(controller.guessWord("anUuidYouSeeHere","GUESS"));
        try {
            verify(service, times(1)).guessWord(anyString(), anyString());
        } catch (Exception e) {
            fail("Exception thrown while verifying the correct methods were called.");
        }
    }
}