package ge.nrogava.messengerapp.views

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ge.nrogava.messengerapp.database.Chat
import ge.nrogava.messengerapp.database.FirebaseRepository

class ChatsViewModel : ViewModel() {
    private val rep=FirebaseRepository()

    private val _chatsLiveData=MutableLiveData<List<Chat>>()
    val chatsLiveData: LiveData<List<Chat>> = _chatsLiveData

    fun getAllChats() {
        rep.getAllChats(_chatsLiveData)
        Log.d("RV","In View Model")
    }


}