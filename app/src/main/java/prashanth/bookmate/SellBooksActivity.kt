package prashanth.bookmate

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sell_books.*
import prashanth.bookmate.models.Books
import java.io.ByteArrayOutputStream

class SellBooksActivity : AppCompatActivity() {
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val storage: FirebaseStorage = FirebaseStorage.getInstance()
    val adb = FirebaseDatabase.getInstance()
    val RESULT_LOAD_IMAGE = 1
    val databaseBooks = adb.getReference("books").push()
    var bookImagePath: String? = null
    var bookBitmap: Bitmap? = null
    var mProgressBar: ProgressBar? = null
    internal var taskValue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_books)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val postTheAd = findViewById(R.id.post_the_ad) as Button
        mProgressBar = findViewById(R.id.progressBar2) as ProgressBar
        postTheAd.setOnClickListener { addBooks() }

        book_image.setOnClickListener {
            val i = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

            val cursor = contentResolver.query(selectedImage,
                    filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            cursor.close()
            book_image.setImageURI(selectedImage)
            val drawable = book_image.drawable
            bookBitmap = (drawable as BitmapDrawable).bitmap
        }
    }


    private fun addBooks() {
        val book_Name = book_name.text.toString()
        val author_Name = author.text.toString()
        val book_Description = book_description.text.toString()
        val mobile_Number = mobileNumber.text.toString()
        val price = book_price.text.toString()
        val book_genre = genre.selectedItem.toString()
        val zipcode = pinCode.text.toString()

        if (TextUtils.isEmpty(book_Name))
        {
            Toast.makeText(this, "Missing book name", Toast.LENGTH_LONG).show()
        }
        else if (TextUtils.isEmpty(author_Name))
        {
            Toast.makeText(this, "Missing author name", Toast.LENGTH_LONG).show()
        }
        else if (TextUtils.isEmpty(book_Description))
        {
            Toast.makeText(this, "Missing book description", Toast.LENGTH_LONG).show()
        }
        else if (TextUtils.isEmpty(mobile_Number))
        {
            Toast.makeText(this, "Missing mobile number", Toast.LENGTH_LONG).show()
        }
        else if (TextUtils.isEmpty(price))
        {
            Toast.makeText(this, "Missing book price", Toast.LENGTH_LONG).show()
        }
        else if (TextUtils.isEmpty(book_genre))
        {
            Toast.makeText(this, "Missing book genre", Toast.LENGTH_LONG).show()
        }
        else if (TextUtils.isEmpty(zipcode))
        {
            Toast.makeText(this, "Missing Zip code", Toast.LENGTH_LONG).show()
        }
        else
        {
            mProgressBar!!.visibility = View.VISIBLE
            post_the_ad.isEnabled = false
            val newBook = Books("", book_Name, author_Name, book_Description, mobile_Number, price,"", book_genre, zipcode)

            uploadImage(newBook)
        }
    }

    @SuppressLint("VisibleForTests")
    fun uploadImage(book: Books) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bookBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        book_image.isDrawingCacheEnabled = false
        val data = byteArrayOutputStream.toByteArray()
        val bookImageRef = storage.reference
                           .child(mAuth.currentUser!!.uid)
                           .child(book.bookName)
        bookImageRef.putBytes(data)
                .addOnSuccessListener { snapshot ->
                    try {
                        book.photo = snapshot.downloadUrl.toString()
                        book.userID = mAuth.currentUser!!.uid
                        databaseBooks.setValue(book)
                        Toast.makeText(this, "Your ad is posted successfully", Toast.LENGTH_SHORT).show()
                        mProgressBar!!.visibility = View.GONE
                        post_the_ad.isEnabled = true
                    } catch (ex: Exception) {
                        Log.println(Log.ERROR,"exception is",ex.toString())
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to upload your ad", Toast.LENGTH_LONG).show()
                    mProgressBar!!.visibility = View.GONE
                    post_the_ad.isEnabled = true }
    }
}
