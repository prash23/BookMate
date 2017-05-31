package prashanth.bookmate.fragments

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
import kotlinx.android.synthetic.main.fragment_register.*

import java.net.InetAddress

import prashanth.bookmate.R
import prashanth.bookmate.interfaces.PageRedirect

/**
 * A login screen that offers login via email/password.
 */
class RegisterFragment : Fragment() {

    var pageRedirect: PageRedirect? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_register, container, false)
        val auth = FirebaseAuth.getInstance()
        val emailID = view.findViewById(R.id.registerEmail) as EditText
        val password = view.findViewById(R.id.registerPassword) as EditText
        val registerBtn = view.findViewById(R.id.sign_up_button) as Button
        val forgotPassword = view.findViewById(R.id.btn_reset_password) as Button
        val linkToLogin = view.findViewById(R.id.linkToLoginPage) as Button
        registerBtn.setOnClickListener(View.OnClickListener {
            val email = emailID.text.toString().trim { it <= ' ' }
            val pwd = password.text.toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(activity, "Enter email address!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(activity, "Enter password!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (pwd.length < 6) {
                Toast.makeText(activity, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (!isInternetAvailable) {
                register_progress.visibility = View.VISIBLE
                //create user
                auth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(activity) { task ->
                            Toast.makeText(activity, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                            register_progress.visibility = View.GONE
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful) {
                                Toast.makeText(activity, "Authentication failed." + task.exception!!,
                                        Toast.LENGTH_SHORT).show()
                            } else {
                                redirectToPage("loginPage")
                            }
                        }
            } else {
                register_progress.visibility = View.GONE
                Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_LONG).show()
            }
        })


        linkToLogin.setOnClickListener { redirectToPage("loginPage") }

        forgotPassword.setOnClickListener { redirectToPage("forgotPassword") }

        return view
    }

    private fun redirectToPage(pageName: String) {
        try {
            pageRedirect = activity as PageRedirect
        } catch (ex: Exception) {
            Log.e("exception raised: ", ex.toString())
        }

        pageRedirect!!.redirectPage(pageName)
    }


    override fun onResume() {
        super.onResume()
        register_progress.visibility = View.GONE
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

