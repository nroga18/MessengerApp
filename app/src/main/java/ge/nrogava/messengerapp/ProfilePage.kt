package ge.nrogava.messengerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfilePage : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
        viewInitializations()

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
                startActivity(Intent(this,HomePage::class.java))
                finish()
            }
        }
    }

    private fun searchFabInit() {
        fab=findViewById(R.id.search_fab_profile)
        fab.setOnClickListener {
            startActivity(Intent(this,SearchPage::class.java))
            finish()
        }
    }
}