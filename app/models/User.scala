package models

case class User(id:Int,user:String, password:String,nickname:String) 


object User extends  ((Int,String ,String,String) => User) {

  def apply(user:String,password:String,nickname:String):User = User(0,user,password,nickname)
  def unpick(u: User): Option[ (String,String,String)]  = Some(u.user,u.password,u.nickname)
  
}
