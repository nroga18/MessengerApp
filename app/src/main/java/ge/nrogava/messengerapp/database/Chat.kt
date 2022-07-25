package ge.nrogava.messengerapp.database

data class Chat (val user:String = "",val time: Long = System.currentTimeMillis() / 1000 , val recentMessage: String = "") {
  constructor() : this("",System.currentTimeMillis() / 1000 , "")
}