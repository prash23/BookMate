package prashanth.bookmate.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*
import prashanth.bookmate.R
import java.net.InetAddress

/**
 * Created by Prashanth on 7/8/2017.
 */
class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)
        val auth = FirebaseAuth.getInstance()
        val emailID = findViewById(R.id.registerEmail) as EditText
        val password = findViewById(R.id.registerPassword) as EditText
        val registerBtn = findViewById(R.id.sign_up_button) as Button
        val forgotPassword = findViewById(R.id.btn_reset_password) as Button
        val linkToLogin = findViewById(R.id.linkToLoginPage) as Button
        registerBtn.setOnClickListener(View.OnClickListener {
            val email = emailID.text.toString().trim { it <= ' ' }
            val pwd = password.text.toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (pwd.length < 6) {
                Toast.makeText(this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (!isInternetAvailable) {
                register_progress.visibility = View.VISIBLE
                //create user
                auth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(this) { task ->
                            Toast.makeText(this, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                            register_progress.visibility = View.GONE
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful) {
                                Toast.makeText(this, "Authentication failed." + task.exception!!,
                                        Toast.LENGTH_SHORT).show()
                            } else {
                                val loginPage = Intent(this,LoginActivity::class.java)
                                startActivity(loginPage)
                            }
                        }
            } else {
                register_progress.visibility = View.GONE
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show()
            }
        })


        linkToLogin.setOnClickListener {
            val loginPage = Intent(this,LoginActivity::class.java)
            startActivity(loginPage)
            finish()
        }

        forgotPassword.setOnClickListener {
            val forgotPassword = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(forgotPassword)
            finish()
        }
    }

    //You can replace it with your name
    val isInternetAvailable: Boolean
        get() {
            try {
                val ipAddr = InetAddress.getByName("google.com")
                return ipAddr != null

            } catch (e: Exception) {
                return false
            }

        }

    override fun onResume() {
        super.onResume()
        register_progress.visibility = View.GONE
    }
}