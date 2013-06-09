package service

import model.ChallengeAction._
import model.User._
import model.{User, TileRenderer, TaskFeed}
import renerers.ChallengeRenderer
import model.User.User


trait UserService {
  implicit def actionChallengeService: ActionChallengeService

  implicit val t = this

  def proposeForUser(name: UserId): List[ChallengeId] =
    actionChallengeService.allChallengesIDs.filterNot(done(name).contains).filterNot(current(name).contains)


  def authorize(name: UserId, pass: String): Option[User]


  def userForID(name: UserId): Option[User]

  def done(name: UserId): List[Challenge] = {
    challengesForUser(name).filter(_.status == Status.Done)
      .flatMap(uc => actionChallengeService.challengeForID(uc.challengeId))
  }

  def current(name: UserId): List[Challenge] = challengesForUser(name).filter(_.status == Status.Pending)
    .flatMap(uc => actionChallengeService.challengeForID(uc.challengeId))

  def challengesForUser(name: UserId): List[UserChallenge]

  def saveUser(user: User): Option[User]

  def challengeFeed(userId: UserId): TaskFeed = new TaskFeed {
    def taskFeed: List[TileRenderer] = {
      challengesForUser(userId).filter(_.status == Status.Pending).map(ChallengeRenderer.apply)
    }
  }

  def challengeProgress(challenge: UserChallenge): List[(Action, Boolean)]

  def doAction(userID: UserId, actionId: ActionId, amount: Int)

}
