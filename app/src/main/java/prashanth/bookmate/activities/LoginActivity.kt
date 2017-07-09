package prashanth.bookmate.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import prashanth.bookmate.HomePage
import prashanth.bookmate.R
import java.net.InetAddress

/**
 * Created by Prashanth on 7/8/2017.
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)
        val auth = FirebaseAuth.getInstance()
        val email = findViewById(R.id.loginEmail) as EditText
        val password = findViewById(R.id.loginPassword) as EditText
        val loginBtn = findViewById(R.id.loginButton) as Button
        val forgotPassword = findViewById(R.id.loginPage_reset_password) as Button
        val linkToRegister = findViewById(R.id.linkToRegisterPage) as Button
        val progressBar = findViewById(R.id.login_progress) as ProgressBar

        forgotPassword.setOnClickListener {
            val forgotPassword = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(forgotPassword)
            finish()
        }

        linkToRegister.setOnClickListener {
            val registerPage = Intent(this,RegisterActivity::class.java)
            startActivity(registerPage)
            finish()
        }

        loginBtn.setOnClickListener(View.OnClickListener {
            val emailID = email.text.toString()
            val pwd = password.text.toString()

            if (TextUtils.isEmpty(emailID)) {
                Toast.makeText(this, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar.visibility = View.VISIBLE

            if (!isInternetAvailable) {
                //authenticate user
                auth.signInWithEmailAndPassword(emailID, pwd)
                        .addOnCompleteListener(this) { task ->
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            progressBar.visibility = View.GONE
                            if (!task.isSuccessful)
                            {
                                // there was an error
                                if (pwd.length < 6)
                                {
                                    password.error = getString(R.string.minimum_password)
                                }
                                else
                                {
                                    Toast.makeText(this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show()
                                }
                            }
                            else
                            {
                                Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show()
                                val it = Intent(this, HomePage::class.java)
                                startActivity(it)
                            }
                        }
            }
            else
            {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_LONG).show()
            }
        })
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
}