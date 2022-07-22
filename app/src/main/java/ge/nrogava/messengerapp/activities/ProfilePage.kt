package ge.nrogava.messengerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.database.FirebaseRepository
import ge.nrogava.messengerapp.database.Person
import ge.nrogava.messengerapp.util.toast

class ProfilePage : AppCompatActivity() {
    private val rep = FirebaseRepository
    lateinit var bottomNav : BottomNavigationView
    lateinit var fab: FloatingActionButton
    lateinit var nicknameText: EditText
    lateinit var occupationText: EditText
    var person = rep.person!!
    lateinit var updateButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        viewInitializations()

    }

    fun signOut(view:View){
        rep.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun viewInitializations() {
        bottomNavInit()
        searchFabInit()
        editTextInit()
        updateButtonInit()
    }

     fun editTextInit() {

        Log.d("CurrentUserLog3", rep.person.toString())

        nicknameText=findViewById(R.id.edit_text_nickname)
        nicknameText.hint=person.nickname

        occupationText=findViewById(R.id.edit_text_occupation)
        occupationText.hint=person.occupation

    }


    private fun updateButtonInit() {
        updateButton=findViewById(R.id.update_button)
        updateButton.setOnClickListener {
            val newNickname = nicknameText.text.toString()
            val newOccupation = occupationText.text.toString()
            val currentEmail = rep.getNameFromMail(rep.fireBaseUser!!.email.toString())
            Log.d("CurrentEmail",currentEmail)
            if(newNickname.isNotEmpty() && newOccupation.isNotEmpty() && newNickname!=currentEmail) {
                rep.updateCurrentUser(newNickname, newOccupation)
                editTextInit()
            } else {
                toast("Please Fill in Both, Make Sure Email is Unique and then Update.")
            }
        }
    }


    private fun bottomNavInit() {
        bottomNav=findViewById(R.id.bottomAppNav)
        bottomNav.background=null
        bottomNav.setOnItemReselectedListener {
            if(it.itemId != R.id.settings) {
                startActivity(Intent(this, HomePage::class.java))
                finish()
            }
        }
    }

    private fun searchFabInit() {
        fab=findViewById(R.id.search_fab_profile)
        fab.setOnClickListener {
            startActivity(Intent(this, SearchPage::class.java))
            finish()
        }
    }
}