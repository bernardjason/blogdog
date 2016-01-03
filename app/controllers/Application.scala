package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import models.User
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.db.slick.HasDatabaseConfig
import javax.inject.Inject
import play.api.cache._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class Application @Inject() (cache: CacheApi) extends Controller with tables.UserTable with HasDatabaseConfig[JdbcProfile] {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  val users = TableQuery[Users]

  val loginForm = Form(
    mapping(
      "user" -> text,
      "password" -> text,
      "nickname" -> text)(User.apply)(User.unpick))

  def index = Action {
    Redirect(routes.Application.list())
  }

  def list = Action { implicit request =>

    request.session.get("user").map { u =>

      val auth = cache.get[User](u)
      
      if ( ! auth.isEmpty ) 
         Ok(views.html.list(null, loginForm, auth.get.user))
      else
         Ok(views.html.list(null, loginForm, null))
    }.getOrElse{
      Ok(views.html.list(null, loginForm, null))
    }
  }

  val login = Action(parse.form(loginForm)) {
    implicit request =>

      val loginData = request.body

      val q = users.filter { u => u.user === loginData.user && u.password === loginData.password }

      var id:String = ""
      Await.result(db.run(q.result), Duration.Inf).map { u =>

        id = java.util.UUID.randomUUID().toString

        cache.set(id, u)
      }

      Redirect(routes.Application.list()).withSession(
          "user" -> id)

  }

  val logout = Action {
    Redirect(routes.Application.list()).withNewSession
  }
}
