package prashanth.bookmate.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import prashanth.bookmate.R
import prashanth.bookmate.R.id.bookpricetv
import prashanth.bookmate.activities.DisplayBooks
import prashanth.bookmate.models.BookDetails
import prashanth.bookmate.models.Books

class TopBooksListAdapter(val bookList : ArrayList<Books>, val mContext : Context) : RecyclerView.Adapter<TopBooksListAdapter.MyViewHolder>()
{
    var index = 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val view : View = LayoutInflater.from(parent?.context).inflate(R.layout.top_bookslist,parent,false)
        val myViewHolder : MyViewHolder = MyViewHolder(view)
        myViewHolder.holderIndex = index
        index++
        return myViewHolder
    }

    override fun getItemCount(): Int {
       return bookList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val book : Books = bookList[position]
        Picasso.with(mContext)
                .load(book.photo)
                .fit()
                .error(R.drawable.no_image)
                .into(holder!!.book_image)
        holder.book_name.text = book.bookName
        holder.book_price.text = "$"+book.price

        holder.itemView.setOnClickListener({
            val bundleBook : BookDetails = BookDetails(book.userID,book.bookName,book.author,book.bookDescription,book.mobileNumber,book.price,book.photo,book.genre,book.zipcode)
            val newPage : Intent = Intent(mContext,DisplayBooks ::class.java)
            newPage.putExtra("book details",bundleBook)
            mContext.startActivity(newPage)
        })

    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var book_image : ImageView = itemView?.findViewById(R.id.book_image) as ImageView
        var book_name : TextView = itemView?.findViewById(R.id.booknametv) as TextView
        var book_price : TextView = itemView?.findViewById(bookpricetv) as TextView
        var  holderIndex: Int = 0
    }
}


