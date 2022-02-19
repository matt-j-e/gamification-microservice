package microservices.book.gamification.game;

import microservices.book.gamification.game.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface BadgeRepository extends CrudRepository<BadgeCard, Long> {

  /**
   *
   * @param userId
   * @return a Set of badge types collected by the user.
   */
  Set<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(Long userId);
}
