package ge.nrogava.messengerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import ge.nrogava.messengerapp.R
import ge.nrogava.messengerapp.database.FirebaseRepository

class SignUpActivity : AppCompatActivity() {

    private val rep= FirebaseRepository()
    lateinit var emailTxt : TextView
    lateinit var passwordTxt : TextView
    lateinit var occupationTxt: TextView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


    }
    fun actvitySignUp(view: View){
        emailTxt = findViewById<EditText>(R.id.txt_email)
        passwordTxt = findViewById<EditText>(R.id.txt_password)
        occupationTxt = findViewById<EditText>(R.id.txt_occupation)
        progressBar = findViewById<ProgressBar>(R.id.sign_up_progress_bar)

        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()
        var occupation = occupationTxt.text.toString()

        progressBar.visibility = View.VISIBLE
        rep.fireBaseSignUp(email, password, occupation,
            this, this::startHomePageActivity)
    }

    private fun startHomePageActivity(success: Boolean){
        if (success) {
            startActivity(Intent(this, HomePage::class.java))
            finish()
        }
        findViewById<ProgressBar>(R.id.sign_up_progress_bar).visibility = View.INVISIBLE
    }
}