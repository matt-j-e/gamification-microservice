package microservices.book.gamification.game;

import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

  private GameService gameService;
  @MockBean
  private ChallengeSolvedDTO challengeSolvedDTO;

  @BeforeEach
  public void setUp() {
    gameService = new GameServiceImpl();
  }

  @Test
  public void checkCorrectAttemptTest() {
    //given
    challengeSolvedDTO = new ChallengeSolvedDTO(1L, true, 20, 30, 1L, "matt");
    //when
    GameService.GameResult gameResult = gameService.newAttemptForUser(challengeSolvedDTO);
    //then
    then(gameResult.getScore()).isEqualTo(10);
  }

  @Test
  public void checkWrongAttemptTest() {
    //given
    challengeSolvedDTO = new ChallengeSolvedDTO(1L, false, 20, 30, 1L, "matt");
    //when
    GameService.GameResult gameResult = gameService.newAttemptForUser(challengeSolvedDTO);
    //then
    then(gameResult.getScore()).isEqualTo(0);
  }

}
