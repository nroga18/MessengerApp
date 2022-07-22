package ge.nrogava.messengerapp.database

data class Message (val sentOrReceived:Boolean = true,val time: String = "", val message: String = "") {
}