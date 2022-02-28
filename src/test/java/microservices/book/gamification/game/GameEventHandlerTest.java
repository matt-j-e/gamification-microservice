package microservices.book.gamification.game;

import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameEventHandlerTest {

  private GameEventHandler gameEventHandler;

  @Mock
  private GameService gameService;

  @BeforeEach
  public void setUp() {
    gameEventHandler = new GameEventHandler(gameService);
  }

  @Test
  public void testGameServiceIsCalled() throws Exception {
    // given
    ChallengeSolvedEvent event = new ChallengeSolvedEvent(
            1L, false, 40, 50, 10L, "dave");

    // when
    gameEventHandler.handleMultiplicationSolved(event);

    // then
    verify(gameService, times(1)).newAttemptForUser(event);
  }

  // SIMPLY COULDN'T GET MY HEAD AROUND HOW TO TEST THE EXCEPTION
//  @Test
//  public void testExceptionThrownWithMissingParameter() throws AmqpRejectAndDontRequeueException {
//    // given
//    ChallengeSolvedEvent event = new ChallengeSolvedEvent(
//            1L, false, 40, 50, 10L, "dave");
//    given(gameService.newAttemptForUser(event).willNothing());
//    //when
//    gameEventHandler.handleMultiplicationSolved(event);
//    //then
//    verify(gameEventHandler, shouldHaveThrown(AmqpRejectAndDontRequeueException.class));
//  }

}
