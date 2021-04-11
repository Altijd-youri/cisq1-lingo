package nl.hu.cisq1.lingo.trainer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GameResponseDTO;
import nl.hu.cisq1.lingo.trainer.presentation.dto.GuessDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
@Tag("integration")
class TrainerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String uuid;

    @BeforeEach
    void startGame() throws Exception {
        RequestBuilder requestGame = MockMvcRequestBuilders
                .post("/game");
        this.uuid = getResponseUuid(mockMvc.perform(requestGame).andReturn());
    }

    @Test
    @DisplayName("Start a new game.")
    void startAnewGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game");

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.score", is(0)))
                .andExpect(jsonPath("$.rounds", is(0)));
    }

    @Test
    @DisplayName("Start a new round")
    void startANewRound() throws Exception {
        startGame();

        RequestBuilder requestRound = MockMvcRequestBuilders
                .post("/game/"+uuid+"/round");

        mockMvc.perform(requestRound)
                .andExpect(jsonPath("$.id", is(uuid)))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.score", is(0)))
                .andExpect(jsonPath("$.rounds", is(1)));

        // TODO - Make assertion more thorough. e.g.: hint
    }

    @Test
    @DisplayName("Multiple active rounds are not allowed")
    void multipleActiveRoundsNotAllowed() throws Exception {
        startGame();
        startRound();

        RequestBuilder requestRound = MockMvcRequestBuilders
                .post("/game/"+uuid+"/round");

        mockMvc.perform(requestRound)
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Can't start a round on a invalid game.")
    void roundWithoutGameNotAllowed() throws Exception {
        RequestBuilder requestRound = MockMvcRequestBuilders
                .post("/game/00000000-0000-0000-0000-000000000000/round");

        mockMvc.perform(requestRound)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Can't start a round on a finished game.")
    void roundOnEndedGameNotAllowed() throws Exception {
        startGame();
        startRound();
        final int MAXATTEMPTS = 5;
        for (int index = 0; index < MAXATTEMPTS; index++) {
            doGuess("wrong");
        }

        RequestBuilder requestRound = MockMvcRequestBuilders
                .post("/game/"+uuid+"/round");

        mockMvc.perform(requestRound)
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Guess a word.")
    void guessCorrect() throws Exception {
        startGame();
        startRound();

        GuessDTO guessDTO = new GuessDTO("beard");

        RequestBuilder requestGuess = MockMvcRequestBuilders
                .patch("/game/"+uuid+"/guess")
                .content(objectMapper.writeValueAsString(guessDTO))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestGuess)
                .andExpect(status().isOk());

        // TODO - Make assertion more thorough. e.g.: hint and feedback:
//                .andExpect(jsonPath("$.round.hint", is(hasLength(5))))
//                .andExpect(jsonPath("$.round.feedback", is(hasLength(5))));
    }

    @Test
    @DisplayName("Can't guess on a non-existent game.")
    void guessWithoutGameNotAllowed() throws Exception {
        GuessDTO guessDTO = new GuessDTO("beard");

        RequestBuilder requestGuess = MockMvcRequestBuilders
                .patch("/game/00000000-0000-0000-0000-000000000000/guess")
                .content(objectMapper.writeValueAsString(guessDTO))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestGuess)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Can't guess without an active round.")
    void guessWhileNoRoundIsActiveIsNotAllowed() throws Exception {
        startGame();

        GuessDTO guessDTO = new GuessDTO("beard");

        RequestBuilder requestGuess = MockMvcRequestBuilders
                .patch("/game/"+uuid+"/guess")
                .content(objectMapper.writeValueAsString(guessDTO))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestGuess)
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Guess is rejecten when is the wrong length")
    void invalidGuessNotAllowed() throws Exception {
        startGame();
        startRound();

        GuessDTO guessDTO = new GuessDTO("beardToLong");

        RequestBuilder requestGuess = MockMvcRequestBuilders
                .patch("/game/"+uuid+"/guess")
                .content(objectMapper.writeValueAsString(guessDTO))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestGuess)
                .andExpect(status().isBadRequest());
    }

    private String getResponseUuid(MvcResult mvcResult) throws Exception {
        String content = mvcResult.getResponse().getContentAsString();
        GameResponseDTO dto = objectMapper.readValue(content, GameResponseDTO.class);

        return dto.getId();
    }

    private void startRound() throws Exception {
        RequestBuilder requestRound = MockMvcRequestBuilders
                .post("/game/"+uuid+"/round");
        mockMvc.perform(requestRound);
    }

    private MvcResult doGuess(String word) throws Exception {
        GuessDTO guessDTO = new GuessDTO(word);

        RequestBuilder requestGuess = MockMvcRequestBuilders
                .patch("/game/"+uuid+"/guess")
                .content(objectMapper.writeValueAsString(guessDTO))
                .contentType(MediaType.APPLICATION_JSON);

        return mockMvc.perform(requestGuess).andReturn();
    }


}
