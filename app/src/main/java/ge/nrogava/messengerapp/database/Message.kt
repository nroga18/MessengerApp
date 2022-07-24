package ge.nrogava.messengerapp.database

data class Message (val sentOrReceived:Boolean
                    ,var key : String
                    ,var fromId : String
                    ,var toId : String
                    ,val message: String = "",
                    val time: Long = System.currentTimeMillis() / 1000) {
}