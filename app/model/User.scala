package model

import org.joda.time.DateTime


object User {

  type UserId = String

  case class User(image: String, name: String, pass: String) extends NameID {
    override def toString: String = s"name: $name  pass: $pass  image: $image"
  }

  case class UserChallenge(userId: String, challengeId: String, status: Status.Value, startDate: Option[DateTime] = None)


  object Status extends Enumeration {
    val Done = Value("Done")
    val Pending = Value("Pending")
    val Aborted = Value("Aborted")
  }


}

