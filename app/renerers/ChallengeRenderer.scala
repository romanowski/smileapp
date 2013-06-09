package renerers

import model.ChallengeAction._
import model.{MockTile, TileRenderer}
import service.{UserService, ActionChallengeService}
import views.html
import play.api.templates.Html
import collection.mutable.StringBuilder
import model.User.UserChallenge

class ChallengeRenderer(uc: UserChallenge, implicit val UserService: UserService) extends TileRenderer {
  val challenge = UserService.actionChallengeService.challengeForID(uc.challengeId).get

  def headerString: String = challenge.name

  def image = views.html.image(challenge.badge)

  def content = html.modelviews.challengeBody(uc, challenge)
}

object ChallengeRenderer {
  def apply(challenge: UserChallenge)(implicit UserService: UserService) =
    new ChallengeRenderer(challenge, UserService)
}
