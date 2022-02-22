package microservices.book.gamification.game;

import microservices.book.gamification.game.domain.LeaderBoardRow;
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

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(LeaderBoardController.class)
public class LeaderBoardControllerTest {

  @MockBean
  LeaderBoardService leaderBoardService;
  @Autowired
  private MockMvc mvc;
  @Autowired
  private JacksonTester<List<LeaderBoardRow>> json;

  @Test
  void retrieveLeaderBoardTest() throws Exception {
    // given
    List<LeaderBoardRow> leaders = List.of(new LeaderBoardRow(1L, 200L, List.of()));
    given(leaderBoardService.getCurrentLeaderBoard()).willReturn(leaders);
    // when
    MockHttpServletResponse response =  mvc.perform(get("/leaders")
            .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();
    // then
    then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    then(response.getContentAsString()).isEqualTo(json.write(leaders).getJson());
  }
}
