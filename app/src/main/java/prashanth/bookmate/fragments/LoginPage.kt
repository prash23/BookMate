package prashanth.bookmate.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


import java.net.InetAddress

import prashanth.bookmate.HomePage
import prashanth.bookmate.R
import prashanth.bookmate.interfaces.PageRedirect

class LoginPage : Fragment() {


    var mPageRedirect: PageRedirect? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_login, container, false)
        val auth = FirebaseAuth.getInstance()
        val email = view.findViewById(R.id.loginEmail) as EditText
        val password = view.findViewById(R.id.loginPassword) as EditText
        val loginBtn = view.findViewById(R.id.loginButton) as Button
        val forgotPassword = view.findViewById(R.id.loginPage_reset_password) as Button
        val linkToRegister = view.findViewById(R.id.linkToRegisterPage) as Button
        val progressBar = view.findViewById(R.id.login_progress) as ProgressBar

        forgotPassword.setOnClickListener { redirectToPage("forgotPassword") }

        linkToRegister.setOnClickListener { redirectToPage("register") }

        loginBtn.setOnClickListener(View.OnClickListener {
            val emailID = email.text.toString()
            val pwd = password.text.toString()

            if (TextUtils.isEmpty(emailID)) {
                Toast.makeText(activity, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(activity, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            progressBar.visibility = View.VISIBLE

            if (!isInternetAvailable) {
                //authenticate user
                auth.signInWithEmailAndPassword(emailID, pwd)
                        .addOnCompleteListener(activity) { task ->
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            progressBar.visibility = View.GONE
                            if (!task.isSuccessful) {
                                // there was an error
                                if (pwd.length < 6) {
                                    password.error = getString(R.string.minimum_password)
                                } else {
                                    Toast.makeText(activity, getString(R.string.auth_failed), Toast.LENGTH_LONG).show()
                                }
                            } else {
                                Toast.makeText(activity, "Login Success", Toast.LENGTH_LONG).show()
                                val it = Intent(activity, HomePage::class.java)
                                startActivity(it)
                            }
                        }
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_LONG).show()
            }
        })
        return view
    }

    private fun redirectToPage(pageName: String) {
        try {
            mPageRedirect = activity as PageRedirect
        } catch (ex: Exception) {
            Log.e("exception raised: ", ex.toString())
        }

        mPageRedirect?.redirectPage(pageName)
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


