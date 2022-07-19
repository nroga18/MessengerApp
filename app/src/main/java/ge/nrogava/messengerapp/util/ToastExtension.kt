package ge.nrogava.messengerapp.util

import android.content.Context
import android.widget.Toast

fun Context.toast(text : String) {
    Toast.makeText(this,text, Toast.LENGTH_LONG).show()
    //toast duration speed up would be better.
}