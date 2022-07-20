package ge.nrogava.messengerapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import ge.nrogava.messengerapp.database.FirebaseRepository

class LoginActivity : AppCompatActivity() {

    private val rep= FirebaseRepository()
    lateinit var emailTxt : TextView
    lateinit var passwordTxt : TextView
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login1)

//        rep.fireBaseUser?.let{
//            riderctToHomePage(true)
//        }
    }
    private fun riderctToHomePage(success: Boolean){
        if (success) {
            startActivity(Intent(this, HomePage::class.java))
            finish()
        }
        findViewById<ProgressBar>(R.id.login_progress_bar).visibility = View.INVISIBLE
    }
    fun logIn(view: View){
        emailTxt = findViewById<EditText>(R.id.edit_text_email)
        passwordTxt = findViewById<EditText>(R.id.edit_text_password)
        progressBar = findViewById<ProgressBar>(R.id.login_progress_bar)
        progressBar.visibility = View.VISIBLE

        rep.login(emailTxt.text.toString(), passwordTxt.text.toString(),this, this::riderctToHomePage)
    }

    fun signUp(view: View){
        startActivity(Intent(this, SignUpActivity::class.java))
    }

}