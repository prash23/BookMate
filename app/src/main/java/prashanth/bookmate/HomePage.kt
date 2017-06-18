package prashanth.bookmate

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.home_page.*
import prashanth.bookmate.adapters.TopBooksListAdapter
import prashanth.bookmate.models.Books

class HomePage : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mRecyclerView : RecyclerView? = null
    private var mLayoutManager : RecyclerView.LayoutManager? = GridLayoutManager(this,2)
    private var mBooksAdapter : TopBooksListAdapter? = null
    var bookList : ArrayList<Books>? = ArrayList()
    val mDatabase : FirebaseDatabase? = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)
        val gso : GoogleSignInOptions? = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso!!)
                .build()
        mRecyclerView = mainRecyclerView as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = mLayoutManager
        fetchBooks()
    }

    private fun fetchBooks() {
        val database = mDatabase!!.getReference("books")
        database.addChildEventListener(object : ChildEventListener {
                    override fun onChildChanged(snapshot: DataSnapshot?, s: String?) {
                        displayBooks(snapshot!!.getValue(Books::class.java), snapshot.key)
                    }

                    override fun onCancelled(p0: DatabaseError?) {}

                    override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

                    override fun onChildRemoved(p0: DataSnapshot?) {
                    }

                    override fun onChildAdded(snapshot: DataSnapshot?, s: String?) {
                        Log.e("it's working", snapshot!!.getValue(Books::class.java).bookName)
                            displayBooks(snapshot.getValue(Books::class.java), snapshot.key)
                    }

                })
    }

    private fun displayBooks(books: Books?, key: String?) {
        if (books != null) {
            Log.e("display books method", books.price)
            bookList!!.add(books)
            mBooksAdapter = TopBooksListAdapter(bookList!!,applicationContext)
            mRecyclerView!!.adapter = mBooksAdapter
            mBooksAdapter!!.notifyDataSetChanged()
        }
        else
        {
            Toast.makeText(applicationContext,"No Data Found", Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSellYourBook -> {
                val it = Intent(this@HomePage, SellBooksActivity::class.java)
                startActivity(it)
            }
            R.id.menuLogout -> {
                signOut()
            }
        }
        return true
    }

    private fun signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback {
//            finish()
//            Toast.makeText(this,"Logged out successfully",Toast.LENGTH_LONG).show() }
        FirebaseAuth.getInstance().signOut()
        finish()
        fragmentManager.popBackStack()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e("No Internet Connection", p0.toString())
    }
}

