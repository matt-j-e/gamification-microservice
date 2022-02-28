package microservices.book.gamification.game.badgeprocessors;

import microservices.book.gamification.challenge.ChallengeSolvedEvent;
import microservices.book.gamification.game.domain.BadgeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LuckyNumberBadgeProcessorTest {

  private LuckyNumberBadgeProcessor badgeProcessor;

  @BeforeEach
  public void setUp() {
    badgeProcessor = new LuckyNumberBadgeProcessor();
  }

  @Test
  public void shouldGiveBadgeIfEitherFactorIs42() {
    var attempt = new ChallengeSolvedEvent(1L, true, 42, 30, 1L, "matt");
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(10, List.of(), attempt);
    assertThat(badgeType).contains(BadgeType.LUCKY_NUMBER);
  }

  @Test
  public void shouldNotGiveBadgeIfNeitherFactorIs42() {
    var attempt = new ChallengeSolvedEvent(1L, true, 40, 30, 1L, "matt");
    Optional<BadgeType> badgeType = badgeProcessor.processForOptionalBadge(20, List.of(), attempt);
    assertThat(badgeType).isEmpty();
  }
}
