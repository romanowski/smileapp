package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import service.InMemoryServices
import InMemoryServices._
import model.User.User
import model.Mock

object Application extends Controller {


  def index = IsAuthenticated {
    login => implicit req =>
      UserService.userForID(login)
        .map(u => Ok(views.html.dashboard(Mock, UserService.challengeFeed(login), u)))
        .getOrElse(Redirect(routes.Application.lendingPage()))
  }

  def lendingPage = Action {
    Ok(views.html.lending("Your new application is ready."))
  }


  def doAction(actionID: String, amount: Int = 1) = IsAuthenticated {
    login => implicit req =>
      UserService.doAction(login, actionID, amount)
      Redirect(routes.Application.index())
  }

  def debug = Action {

    Ok( s"""
    ${InMemoryServices.users}
    ${InMemoryServices.userChallenges}
    ${InMemoryServices.challenges}
    ${InMemoryServices.actions}
       """)
  }

  val userLoginParam = "login"

  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get(userLoginParam).flatMap(UserService.userForID).map(_.id)

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.lendingPage())

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) {
    user =>
      Action(request => f(user)(request))
  }

  // -- Authentication

  val registerForm = Form(

    mapping(
      "image" -> text,
      "login" -> text,
      "password" -> text
    )(User.apply)(User.unapply)
      .verifying("Choose other login", result => result match {
      case (user) =>
        !UserService.saveUser(user).isEmpty
    })
  )

  /*  /**
     * Login page.
     */
    def login = Action {
      implicit request =>
        Ok(html.login(loginForm))
    }*/

  /**
   * Handle login form submission.
   */
  def register = Action {
    implicit request =>
      registerForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.register(formWithErrors)),
        user => Redirect(routes.Application.index()).withSession(userLoginParam -> user.id)
      )
  }

}