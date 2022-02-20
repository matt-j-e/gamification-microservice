package microservices.book.gamification.game.badgeprocessors;

import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FirstWonBadgeProcessorTest {

  private FirstWonBadgeProcessor badgeProcessor;
  private ScoreCard scoreCard1;
  private ScoreCard scoreCard2;

  @BeforeEach
  public void setUp() {
    badgeProcessor = new FirstWonBadgeProcessor();
    scoreCard1 = new ScoreCard(1L, 1L);
    scoreCard2 = new ScoreCard(1L, 2L);
  }

  @Test
  public void shouldGiveBadgeIfFirstScore() {
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(10, List.of(scoreCard1), null);
    assertThat(badgeType).contains(BadgeType.FIRST_WON);
  }

  @Test
  public void shouldNotGiveBadgeIfNotFirstScore() {
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(20, List.of(scoreCard1, scoreCard2), null);
    assertThat(badgeType).isEmpty();
  }
}
