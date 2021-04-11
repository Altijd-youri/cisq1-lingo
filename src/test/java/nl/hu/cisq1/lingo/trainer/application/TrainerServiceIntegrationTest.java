package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Game;
import nl.hu.cisq1.lingo.trainer.domain.Round;
import nl.hu.cisq1.lingo.trainer.domain.enums.Status;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.*;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
class TrainerServiceIntegrationTest {

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
    void correctGuessIntergation() throws GameRuleException {
        //Arrange
        Game game = service.startNewGame();

        final String UUID = game.getId(); // Extract for direct access to object.

        game = service.startNewRound(UUID);

        Round activeRound = game.getRounds().get(0); // Extract for direct access to object.
        final String CORRECTWORD = activeRound.getWord(); // Extract for direct access to object.

        //Act
        game = service.guessWord(UUID, CORRECTWORD);


        activeRound = game.getRounds().get(0); // Extract for direct access to object.

        //Assert
        assertEquals(Status.WON, activeRound.getStatus());
    }


}
