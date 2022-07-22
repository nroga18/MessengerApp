package ge.nrogava.messengerapp.database

import android.util.Log


data class Person(var nickname: String = "", var occupation: String = "", var url: String= "") {

   init {
     Log.d("CurrentUserLogInit",nickname)
     Log.d("CurrentUserLogInit",occupation)
   }


}