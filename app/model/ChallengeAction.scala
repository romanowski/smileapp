package model

import org.joda.time.DateTime


object ChallengeAction {

  type ChallengeId = String
  type ActionId = String

  case class Action(name: String, desc: String, image: String) extends NameID {

  }


  case class ActionChallenge(challengeID: ChallengeId, actionID: ActionId, order: Int) {

  }

  case class ActionDone(who: String, when: DateTime, what: ActionId, amount: Int) {

  }

  case class Challenge(name: String, desc: String, badge: String) extends NameID {
  }


}
