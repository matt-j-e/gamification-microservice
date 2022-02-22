package microservices.book.gamification.game;

import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.GameService.GameResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(GameController.class)
public class GameControllerTest {

  @MockBean
  private GameService gameService;

  @Autowired
  private MockMvc mvc;
  @Autowired
  private JacksonTester<ChallengeSolvedDTO> jsonRequestAttempt;
  @Autowired
  private JacksonTester<GameResult> jsonResultAttempt;

  @Test
  void validPostResultTest() throws Exception {
    // given
    ChallengeSolvedDTO dto = new ChallengeSolvedDTO(1L, true, 20, 30, 1L, "matt");
    // when
    MockHttpServletResponse response = mvc.perform(
            post("/attempts").contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequestAttempt.write(dto).getJson()))
                    .andReturn().getResponse();
    // then
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    verify(gameService, times(1)).newAttemptForUser(dto);
  }

  @Test
  void wrongUrlPostResultTest() throws Exception {
    ChallengeSolvedDTO dto = new ChallengeSolvedDTO(1L, true, 20, 30, 1L, "matt");
    MockHttpServletResponse response = mvc.perform(
                    post("/attempt").contentType(MediaType.APPLICATION_JSON)
                            .content(jsonRequestAttempt.write(dto).getJson())).andReturn().getResponse();
    then(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    verify(gameService, times(0)).newAttemptForUser(dto);
  }

  @Test
  void wrongPayloadTypePostResultTest() throws Exception {
    ChallengeSolvedDTO dto = new ChallengeSolvedDTO(1L, true, 20, 30, 1L, "matt");
    MockHttpServletResponse response = mvc.perform(
            post("/attempts").contentType(MediaType.TEXT_HTML)
                    .content("wrong content")).andReturn().getResponse();
    then(response.getStatus()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    verify(gameService, times(0)).newAttemptForUser(dto);
  }
}
