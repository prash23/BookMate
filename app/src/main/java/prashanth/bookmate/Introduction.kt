package prashanth.bookmate

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_introduction.*
import prashanth.bookmate.activities.ForgotPasswordActivity
import prashanth.bookmate.activities.LoginActivity
import prashanth.bookmate.activities.RegisterActivity
import prashanth.bookmate.interfaces.PageRedirect


class Introduction : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    var mAuth: FirebaseAuth? = null
    var mGoogleApiClient: GoogleApiClient? = null
    val RC_SIGN_IN = 2
    var mAuthListener : FirebaseAuth.AuthStateListener? = null

    override fun onResume() {
        super.onResume()
        mAuth!!.addAuthStateListener { mAuthListener }
    }

    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener { mAuthListener }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        val signInButton = findViewById(R.id.google_sign) as SignInButton
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                val it = Intent(applicationContext, HomePage::class.java)
                startActivity(it)
            }
            else
            {
                Toast.makeText(this," Login failed ", Toast.LENGTH_LONG).show()
            }
        }
        val login = findViewById(R.id.loginbtn) as Button
        val register = findViewById(R.id.registerBtn) as Button
        mAuth = FirebaseAuth.getInstance()
        val gso : GoogleSignInOptions? = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso!!)
                .build()
        signInButton.setOnClickListener{ signIn() }
        login.setOnClickListener({
            val loginPage = Intent(this, LoginActivity::class.java)
            startActivity(loginPage)
        })
        register.setOnClickListener({
            val registerPage = Intent(this, RegisterActivity::class.java)
            startActivity(registerPage)
        })
        forgotbtn.setOnClickListener({
            val forgotPassword = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(forgotPassword)
        })
    }

    private fun signIn() {
        val signInIntent : Intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN)
        {
            val result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess)
        {
            val account: GoogleSignInAccount = result.signInAccount as GoogleSignInAccount
            firebaseAuthWithGoogle(account)
        }
        else{
            Toast.makeText(this," Login failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val it = Intent(this, HomePage::class.java)
                        startActivity(it)
                        Toast.makeText(this,account.displayName+" Login successful", Toast.LENGTH_LONG).show()
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.exception)
                        Toast.makeText(this, "Authentication failed",Toast.LENGTH_SHORT).show()
                    }
                }
    }

//    private fun fragmentsTransaction(page: String) {
//        if (page.equals("loginPage", ignoreCase = true)) {
//            val loginFragment = LoginPage()
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.intro_activity, loginFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//        } else if (page.equals("register", ignoreCase = true)) {
//            val registerFragment = RegisterFragment()
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.intro_activity, registerFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//        } else if (page.equals("forgotPassword", ignoreCase = true)) {
//            val forgotFragment = ForgotPassword()
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.intro_activity, forgotFragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
//        }
//    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e("Connection failed", p0.toString())
    }

//    override fun redirectPage(pageName: String) {
//        fragmentsTransaction(pageName)
//    }
}
