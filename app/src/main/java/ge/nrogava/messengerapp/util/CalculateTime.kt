package ge.nrogava.messengerapp.util

import java.text.SimpleDateFormat

fun calculateTime(messageTime : Long) : String {
    val difference=(System.currentTimeMillis()-messageTime)/60000
    val diffInHr=difference/60

    var time = ""
    if(difference<60) {
        time= "$difference min"
    } else if(diffInHr<24) {
        if(diffInHr>1) {
            time = (diffInHr).toString() + " hours"
        } else {
            time = (diffInHr).toString() + " hour"
        }
    } else {
           time=SimpleDateFormat("dd MMM").format(messageTime)

    }
    return time

}