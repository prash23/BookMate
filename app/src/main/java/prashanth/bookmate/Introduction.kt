package prashanth.bookmate

import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import prashanth.bookmate.fragments.ForgotPassword
import prashanth.bookmate.fragments.LoginPage
import prashanth.bookmate.fragments.RegisterFragment
import prashanth.bookmate.interfaces.PageRedirect

class Introduction : AppCompatActivity(), View.OnClickListener, PageRedirect {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        val login = findViewById(R.id.loginbtn) as Button
        val register = findViewById(R.id.registerBtn) as Button
        login.setOnClickListener(this)
        register.setOnClickListener(this)
    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.loginbtn -> fragmentsTransaction("loginPage")
            R.id.registerBtn -> fragmentsTransaction("register")
        }

    }

    private fun fragmentsTransaction(page: String) {
        if (page.equals("loginPage", ignoreCase = true)) {
            val loginFragment = LoginPage()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.activity_introduction, loginFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } else if (page.equals("register", ignoreCase = true)) {
            val registerFragment = RegisterFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.activity_introduction, registerFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } else if (page.equals("forgotPassword", ignoreCase = true)) {
            val forgotFragment = ForgotPassword()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.activity_introduction, forgotFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun redirectPage(pageName: String) {
        fragmentsTransaction(pageName)
    }
}
