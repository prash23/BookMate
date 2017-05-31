package prashanth.bookmate

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.android.synthetic.main.activity_sell_books.*

import java.io.ByteArrayOutputStream
import java.util.UUID

import prashanth.bookmate.models.Books

class SellBooksActivity : AppCompatActivity() {
    private val storage = FirebaseStorage.getInstance()
    val adb = FirebaseDatabase.getInstance()
    private var mAdPostClickListener = AdPostClickListener()
    val databaseBooks = adb.getReference("books")
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

//            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            //            bookBitmap = BitmapFactory.decodeFile(picturePath);
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

        if (TextUtils.isEmpty(book_Name)) {
            Toast.makeText(this, "Missing book name", Toast.LENGTH_LONG).show()
        } else if (TextUtils.isEmpty(author_Name)) {
            Toast.makeText(this, "Missing author name", Toast.LENGTH_LONG).show()
        } else if (TextUtils.isEmpty(book_Description)) {
            Toast.makeText(this, "Missing book description", Toast.LENGTH_LONG).show()
        } else if (TextUtils.isEmpty(mobile_Number)) {
            Toast.makeText(this, "Missing mobile number", Toast.LENGTH_LONG).show()
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Missing book price", Toast.LENGTH_LONG).show()
        } else if (TextUtils.isEmpty(book_genre)) {
            Toast.makeText(this, "Missing book genre", Toast.LENGTH_LONG).show()
        } else if (TextUtils.isEmpty(zipcode)) {
            Toast.makeText(this, "Missing Zip code", Toast.LENGTH_LONG).show()
        } else {
            mProgressBar!!.visibility = View.VISIBLE
            post_the_ad.isEnabled = false

            mAdPostClickListener.uploadImage(book_Name)
            mAdPostClickListener.adPost(book_Name, author_Name, book_Description, mobile_Number, price, book_genre, zipcode)


        }
    }


    private inner class AdPostClickListener {

        fun adPost(book_Name: String, author_Name: String, book_Description: String, mobile_Number: String, price: String, book_genre: String, zipcode: String) {
            if (bookImagePath != null) {
                try {
                    val bookID = databaseBooks.push().key

                    val newBook = Books(bookID, book_Name, author_Name, book_Description, mobile_Number, price, bookImagePath!!, book_genre, zipcode)

                    databaseBooks.child(bookID).setValue(newBook)
                    Toast.makeText(this@SellBooksActivity, "Your ad is posted successfully", Toast.LENGTH_SHORT).show()
                    mProgressBar!!.visibility = View.GONE
                    post_the_ad.isEnabled = true
                } catch (ex: Exception) {
                    Log.println(Log.ERROR,"exception is",ex.toString())
                }

            } else {
                Toast.makeText(this@SellBooksActivity, "Error uploading image", Toast.LENGTH_LONG).show()
                mProgressBar!!.visibility = View.GONE
                post_the_ad.isEnabled = true
            }
        }

        fun uploadImage(book_Name: String) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bookBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

            book_image.isDrawingCacheEnabled = false
            val data = byteArrayOutputStream.toByteArray()

            val path = "bookImages/" + UUID.randomUUID() + ".png"
            val bookImageRef = storage.reference.child(path)
            val metadata = StorageMetadata.Builder()
                    .setCustomMetadata("name", book_Name)
                    .build()

            val uploadTask = bookImageRef.putBytes(data, metadata)
            uploadTask.addOnSuccessListener(this@SellBooksActivity) { taskSnapshot -> bookImagePath = taskSnapshot.downloadUrl!!.toString() }
        }
    }

    companion object {
        private val RESULT_LOAD_IMAGE = 1
    }
}
