package microservices.book.gamification.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {

  @Override
  public GameResult newAttemptForUser(ChallengeSolvedDTO challengeSolvedDTO) {
    int score;
    if (challengeSolvedDTO.isCorrect()) {
      score = 10;
    } else {
      score = 0;
    }
    return new GameResult(score, List.of());
  }
}
