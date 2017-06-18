package prashanth.bookmate.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_display_books.*
import prashanth.bookmate.R
import prashanth.bookmate.models.BookDetails

class DisplayBooks : AppCompatActivity() {

    var dBooks : BookDetails? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_books)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        dBooks = intent.getParcelableExtra("book details")
        if (dBooks == null)
            run {
                Toast.makeText(this, "invalid data", Toast.LENGTH_LONG).show()
                finish()
            }
        else {
            supportActionBar!!.title = dBooks!!.bookName
            Picasso.with(this)
                    .load(dBooks!!.photo)
                    .fit()
                    .into(summaryPhoto)
            summaryAuthor.text = "AUTHOR: "+dBooks!!.author
            summaryName.text = "NAME: "+dBooks!!.bookName
            summaryDescription.text = "DESCRIPTION: "+dBooks!!.bookDescription
            summaryGenre.text = "GENRE: "+dBooks!!.genre
            summaryPrice.text = "PRICE: "+dBooks!!.price
            summaryContact.text = "CONTACT: "+dBooks!!.mobileNumber
            summaryZipCode.text = "ZIPCODE: "+dBooks!!.zipcode
        }
    }
}
