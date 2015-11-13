package controllers

import java.util.Date
import scala.concurrent.Future
import models.Blog
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import slick.driver.JdbcProfile
import tables.BlogTable
import java.sql.Timestamp
import java.util.Calendar
import models.Blog.timestampFormat
import play.api.cache._
import javax.inject.Inject
import models.User

class Api @Inject() (cache: CacheApi) extends Controller with BlogTable with HasDatabaseConfig[JdbcProfile] {
  val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)
  import driver.api._

  val blogs = TableQuery[Blogs]

  def getBlogs() = SecuredAction.async { implicit request =>

    val myblogs = for {
      c <- blogs.sortBy { x => x.when.desc } if c.user === request.user.user
    } yield (c)

    db.run(myblogs.result).map { res =>
      {
        Ok(Json.toJson(res))
      }
    }
  }

  def postBlog = SecuredAction.async(parse.json) { implicit request =>

    val blog = request.body.as[Blog]

    val b = Blog("", request.user.user, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()), blog.what)

    db.run(blogs += b).map(_ => Ok(Json.toJson(b)))

    Future.successful(Ok("added"))
  }


}