package prashanth.bookmate.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import prashanth.bookmate.R
import java.net.InetAddress

/**
 * Created by Prashanth on 7/8/2017.
 */
class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_forgot_password)
        val auth = FirebaseAuth.getInstance()
        val emailID = findViewById(R.id.forgotEmail) as EditText
        val resetBtn = findViewById(R.id.resetPassword) as Button
        val backBtn = findViewById(R.id.back) as Button
        val mProgressBar = findViewById(R.id.progressBar) as ProgressBar
        backBtn.setOnClickListener { finish()}

        resetBtn.setOnClickListener(View.OnClickListener {
            val email = emailID.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter your registered email id", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (!isInternetAvailable) {
                mProgressBar.visibility = View.VISIBLE
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this, "Failed to send reset email!", Toast.LENGTH_SHORT).show()
                            }

                            mProgressBar.visibility = View.GONE
                        }
            } else {
                mProgressBar.visibility = View.GONE
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