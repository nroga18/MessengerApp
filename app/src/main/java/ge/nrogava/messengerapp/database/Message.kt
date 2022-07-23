package ge.nrogava.messengerapp.database

data class Message (val sentOrReceived:Boolean = true, val message: String = "",val time: Long = System.currentTimeMillis()) {
}