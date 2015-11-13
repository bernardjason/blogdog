package models

case class User(id:Int,user:String, password:String) 


object User extends  ((Int,String ,String) => User) {

  def apply(user:String,password:String):User = User(0,user,password)
  def unpick(u: User): Option[ (String,String)]  = Some(u.user,u.password)
  
}
