package service

import model.ChallengeAction._
import model.User

/**
 * Created with IntelliJ IDEA.
 * User: krzysiek
 * Date: 08.06.13
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
trait ActionChallengeService {

  def allChallengesIDs: List[ChallengeId]

  def actionForID(actionId: ActionId): Option[Action]

  def challengeForID(challengeId: ChallengeId): Option[Challenge]

  def actionsForChallenge(challengeId: ChallengeId): List[ActionChallenge]

  def mainActions: List[Action]


}
