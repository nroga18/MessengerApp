package ge.nrogava.messengerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfilePage : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        viewInitializations()

    }


    private fun viewInitializations() {
        bottomNavInit()
    }


    private fun bottomNavInit() {
        bottomNav=findViewById(R.id.bottomAppNav)
        bottomNav.background=null
        bottomNav.setOnItemReselectedListener {
            if(it.itemId != R.id.settings) {
                startActivity(Intent(this,HomePage::class.java))
                finish()
            }
        }

    }
}