package controllers

import play.api.mvc._
import play.Logger
import scala.concurrent.Future
import javax.inject.Inject
import play.api.cache.CacheApi
import play.mvc.Http.Status
import models.User
import play.api.cache.Cache
import play.api.mvc.{ Request, WrappedRequest }
import models.User

class AuthenticatedRequest[A](val user: User, val request: Request[A])
  extends WrappedRequest[A](request)

object SecuredAction extends ActionBuilder[AuthenticatedRequest] {


  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]) = {

    request.session.get("user").map { u =>

      import play.api.Play.current
      val user = Cache.getAs[User](u)

      if (user.isEmpty || user.get.id < 0) {
        Logger.info(s"not logged in, action ${request.method} ${request.uri}")

        Future.successful(Results.Status(Status.UNAUTHORIZED))
      } else {
        Logger.info(s"Calling action ${request.method} ${request.uri}")
        block(new AuthenticatedRequest(user.get, request))
      }
    }.getOrElse {
      Logger.info(s"not logged on, action ${request.method} ${request.uri}")

      Future.successful(Results.Status(Status.UNAUTHORIZED))
    }

  }

}



