package microservices.book.gamification.game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.challenge.ChallengeSolvedDTO;
import microservices.book.gamification.game.badgeprocessors.BadgeProcessor;
import microservices.book.gamification.game.domain.BadgeCard;
import microservices.book.gamification.game.domain.BadgeType;
import microservices.book.gamification.game.domain.ScoreCard;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {

  private final ScoreRepository scoreRepository;
  private final BadgeRepository badgeRepository;
  // Spring injects all the @Component beans in the badgeProcessors list.
  private final List<BadgeProcessor> badgeProcessors;

  @Override
  public GameResult newAttemptForUser(ChallengeSolvedDTO challenge) {
    if (challenge.isCorrect()) {
      ScoreCard scoreCard = new ScoreCard(challenge.getUserId(), challenge.getAttemptId());
      scoreRepository.save(scoreCard);
      log.info("User {} scored {} points for attempt id {}",
              challenge.getUserAlias(),
              scoreCard.getScore(),
              challenge.getAttemptId());
      List<BadgeCard> badgeCards = processForBadges(challenge);
      return new GameResult(scoreCard.getScore(),
              badgeCards.stream().map(BadgeCard::getBadgeType)
                      .collect(Collectors.toList()));
    } else {
      log.info("Attempt id {} is not correct. " +
              "User {} does not get a score.",
              challenge.getAttemptId(),
              challenge.getUserAlias());
      return new GameResult(0, List.of())
    }
  }

  /**
   * Checks the total score and the different score cards obtained
   * to give new badges in cases where their conditions are met.
   */
  private List<BadgeCard> processForBadges(final ChallengeSolvedDTO solvedChallenge) {

    Optional<Integer> optTotalScore = scoreRepository.getTotalScoreForUser(solvedChallenge.getUserId());
    if (optTotalScore.isEmpty()) return Collections.emptyList();
    int totalScore = optTotalScore.get();
    // Gets the total score and existing badges for that user
    List<ScoreCard> scoreCardList = scoreRepository
            .findByUserIdOrderByScoreTimestampDesc(solvedChallenge.getUserId());
    Set<BadgeType> alreadyGotBadges = badgeRepository
            .findByUserIdOrderByBadgeTimestampDesc(solvedChallenge.getUserId())
            .stream()
            .map(BadgeCard::getBadgeType)
            .collect(Collectors.toSet());

    // Calls the badge processors for badges that the user doesn't yet have.
    List<BadgeCard> newBadgeCards = badgeProcessors.stream()
            .filter(bp -> bp.processForOptionalBadge(totalScore, scoreCardList, solvedChallenge))
            .flatMap(Optional::stream)
                    .map(badgeType -> new BadgeCard(solvedChallenge.getUserId(), badgeType)
                    )
                    .collect(Collectors.toList());

    badgeRepository.saveAll(newBadgeCards);

    return newBadgeCards;
  }
}