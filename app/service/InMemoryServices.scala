package service

import model.User._
import model.ChallengeAction._
import model.ChallengeAction.Action
import model.User._
import model.User.UserChallenge
import model.ChallengeAction.Challenge
import scala.Some
import controllers.routes
import java.util.Date
import org.joda.time.{Period, DateTime}


object InMemoryServices {


  def imagePath(name: String) = routes.Assets.at("images/" + name).url

  var challenges: List[Challenge] = Challenge("Zagadaj", "Zagadaj do obcej osoby na mieście 3 razy", imagePath("badges/zagadaj.png")) ::
    """badge-komplement.png
      |badge-usmiech.png
      |rozmowa-badge.png
      |zagadaj.png""".stripMargin.split("\n").map(img => Challenge(img, img + img, imagePath("badges/" + img))).toList


  var actions: List[Action] = Action("Zagadaj", "", imagePath("actions/person.png")) :: Nil
  var users: List[User] = User("https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash4/195316_100000522472208_416088632_q.jpg", "a@a.pl", "ala123") :: Nil
  var userChallenges: List[UserChallenge] =
    UserChallenge(users.head.id, challenges.head.id, Status.Pending, Some(new DateTime().minus(Period.days(1)))) ::
      challenges.tail.map(c => UserChallenge(users.head.id, c.id, Status.Done))

  var challengeActions: List[ActionChallenge] =
    ActionChallenge(challenges.head.id, actions.head.id, 1) ::
      ActionChallenge(challenges.head.id, actions.head.id, 2) :: Nil
  var userDoneAction: List[ActionDone] = ActionDone(users.head.id, new DateTime(), actions.head.id, 5) :: Nil


  implicit def ActionChallengeService: ActionChallengeService = new ActionChallengeService {
    def actionForID(actionId: ActionId) = actions.filter(_.id == actionId).headOption


    def challengeForID(challengeId: ChallengeId) = challenges.filter(_.id == challengeId).headOption


    def allChallengesIDs: List[String] = challenges.map(_.id)

    def actionsForChallenge(challengeId: ChallengeId): List[ActionChallenge] =
      challengeActions.filter(_.challengeID == challengeId)

    def mainActions: List[Action] = actions

  }

  implicit def UserService = new UserService {
    def actionChallengeService: ActionChallengeService = InMemoryServices.ActionChallengeService

    def challengesForUser(name: UserId): List[UserChallenge] = userChallenges.filter(_.userId == name)

    def authorize(name: UserId, pass: String): Option[User] = userForID(name).filter(_.pass == pass)

    def userForID(id: UserId): Option[User] = users.filter(_.id == id).headOption

    def saveUser(user: User): Option[User] = {
      userForID(user.id).flatMap(_ => None).orElse {
        users = users :+ user
        Some(user)
      }
    }

    def challengeProgress(cu: UserChallenge): List[(Action, Boolean)] = {
      cu.startDate.toList.flatMap {
        start => {
          var done = userDoneAction.filter(_.who == cu.userId).sortBy(_.when.getMillis).filter(_.when.isAfter(start))

          actionChallengeService.actionsForChallenge(cu.challengeId).map {
            ca =>
              val hasIt = done.dropWhile(_.what != ca.actionID) match {
                case h :: t => {
                  done = t
                  true
                }
                case _ =>
                  done = Nil
                  false

              }
              (actionChallengeService.actionForID(ca.actionID).get, hasIt)
          }
        }
      }
    }

    def doAction(userID: UserId, actionId: ActionId, amount: Int) {
      userDoneAction = userDoneAction :+ ActionDone(userID, new DateTime(), actionId, amount);
    }
  }


}
