package prashanth.bookmate.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

import java.net.InetAddress

import prashanth.bookmate.R


class ForgotPassword : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_forgot_password, container, false)
        val auth = FirebaseAuth.getInstance()
        val emailID = view.findViewById(R.id.forgotEmail) as EditText
        val resetBtn = view.findViewById(R.id.resetPassword) as Button
        val backBtn = view.findViewById(R.id.back) as Button
        val mProgressBar = view.findViewById(R.id.progressBar) as ProgressBar
        backBtn.setOnClickListener { fragmentManager.popBackStackImmediate() }

        resetBtn.setOnClickListener(View.OnClickListener {
            val email = emailID.text.toString().trim { it <= ' ' }

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(activity, "Enter your registered email id", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }

            if (isInternetAvailable) {
                mProgressBar.visibility = View.VISIBLE
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(activity, "Failed to send reset email!", Toast.LENGTH_SHORT).show()
                            }

                            mProgressBar.visibility = View.GONE
                        }
            } else {
                mProgressBar.visibility = View.GONE
                Toast.makeText(activity, "Please check your internet connection", Toast.LENGTH_LONG).show()
            }
        })
        return view
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

}// Required empty public constructor
