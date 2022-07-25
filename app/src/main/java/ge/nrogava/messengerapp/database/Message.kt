package ge.nrogava.messengerapp.database

data class Message (var sentOrReceived:Boolean
                    ,var key : String
                    ,var fromId : String
                    ,var toId : String
                    ,var toIdNickname : String
                    ,var fromIdNickname : String
                    ,var message: String = "",
                    var time: Long = System.currentTimeMillis() / 1000) {
    constructor() : this(true, "", "","","","","")
}
