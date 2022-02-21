package microservices.book.gamification.game;

import microservices.book.gamification.game.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

/**
 * Handles data operations with BadgeCards
 */
public interface BadgeRepository extends CrudRepository<BadgeCard, Long> {

  /**
   * Retrieves all BadgeCards for a given user.
   *
   * @param userId the id of the user to look for BadgeCards
   * @return a Set of badge types collected by the user ordered by most recent first.
   */
  Set<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(Long userId);
}
