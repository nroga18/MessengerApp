package ge.nrogava.messengerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.database.FirebaseRepository

class ProfilePage : AppCompatActivity() {
    private val rep= FirebaseRepository()
    lateinit var bottomNav : BottomNavigationView
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        viewInitializations()

    }

    fun signOut(view: View){
        rep.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    private fun viewInitializations() {
        bottomNavInit()
        searchFabInit()
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