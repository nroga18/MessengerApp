package ge.nrogava.messengerapp.database

import android.util.Log


data class Person(var nickname: String = "", var occupation: String = "", val uid : String, var url: String= "") {
    constructor() : this("", "", "")
   init {
     Log.d("CurrentUserLogInit",nickname)
     Log.d("CurrentUserLogInit",occupation)
   }


}