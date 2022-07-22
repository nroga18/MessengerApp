package ge.nrogava.messengerapp.views

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ge.nrogava.messengerapp.database.FirebaseRepository
import ge.nrogava.messengerapp.database.Person

class PeopleViewModel : ViewModel() {

    private val rep= FirebaseRepository

    private val _peopleLiveData= MutableLiveData<List<Person>>()
    val peopleLiveData: LiveData<List<Person>> = _peopleLiveData

    fun getPeople(prefix : String) {
        rep.getPeople(_peopleLiveData,prefix)
        Log.d("RV","In View Model")
    }

    fun getAllPeople() {
        rep.getAllPeople(_peopleLiveData)
    }

}