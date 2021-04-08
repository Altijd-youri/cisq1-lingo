package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.InvalidGuessException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveGameException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.NoActiveRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.PreviousRoundNotFinishedException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.management.InstanceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
public class TrainerServiceIntegrationTest {

    @Autowired
    private WordService wordService;
    @Autowired
    private SpringGameRepository gameRepository;
    private TrainerService service;


    @BeforeEach
    void prepareTrainerService() {
        service = new TrainerService(wordService, gameRepository);
    }

    @Test
    @DisplayName("Game starts, round starts and correct guess ends the current round with status WON.")
    void correctGuessIntergation() {
        //Arrange
        Game game = service.startNewGame();

        final String UUID = game.getId(); // Extract for direct access to object.
        try {
            game = service.startNewRound(UUID);
        } catch (PreviousRoundNotFinishedException | NoActiveGameException | InstanceNotFoundException | NoActiveRoundException e) {
            fail("Exception thrown while starting a new round.", e);
        }
        Round activeRound = game.getRounds().get(0); // Extract for direct access to object.
        final String CORRECTWORD = activeRound.getWord(); // Extract for direct access to object.

        //Act
        try {
            game = service.guessWord(UUID, CORRECTWORD);
        } catch (InstanceNotFoundException | NoActiveRoundException | InvalidGuessException | NoActiveGameException e) {}

        activeRound = game.getRounds().get(0); // Extract for direct access to object.

        //Assert
        assertEquals(Status.WON, activeRound.getStatus());
    }


}
