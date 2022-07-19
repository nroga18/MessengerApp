package ge.nrogava.messengerapp

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SearchPage : AppCompatActivity() {

    lateinit var backButton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)
        viewInitializations()
    }


    private fun viewInitializations() {
        backButtonInit()

    }

    private fun backButtonInit() {
        backButton=findViewById(R.id.back_button)
        backButton.setOnClickListener {
            startActivity(Intent(this,HomePage::class.java))
            finish()
        }
    }
}