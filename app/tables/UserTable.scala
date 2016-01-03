package tables

import java.sql.Timestamp

import models.User
import slick.driver.JdbcProfile
import slick.lifted.ProvenShape.proveShapeOf



trait UserTable {
  protected val driver: JdbcProfile
  import driver.api._
  class Users(tag: Tag) extends Table[User](tag, "USERS") {
    
    def id = column[Int]("ID",O.PrimaryKey,O.AutoInc)
    def user = column[String]("USER")
    def password = column[String]("PASSWORD")
    def nickname = column[String]("NICKNAME")

    def * = (id,user,password,nickname) <> (User.tupled, User.unapply _)
  }
}