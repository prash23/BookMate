package prashanth.bookmate.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Prashanth on 6/17/2017.
 */

// Inline function to create Parcel Creator
inline fun <reified T : Parcelable> createParcel(crossinline createFromParcel: (Parcel) -> T?): Parcelable.Creator<T> =
        object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel): T? = createFromParcel(source)
            override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
        }

// custom readParcelable to avoid reflection
fun <T : Parcelable> Parcel.readParcelable(creator: Parcelable.Creator<T>): T? {
    if (readString() != null) return creator.createFromParcel(this) else return null
}

data class BookDetails(
        var userID: String,
        var bookName: String,
        var author: String,
        var bookDescription: String,
        var mobileNumber: String,
        var price: String,
        var photo: String,
        var genre: String,
        var zipcode: String
) : Parcelable {

    companion object {
        @JvmField @Suppress("unused")
        val CREATOR = createParcel { BookDetails(it) }
    }


    constructor(parcel: Parcel) : this (
       userID = parcel.readString(),
       bookName = parcel.readString(),
       author = parcel.readString(),
       bookDescription = parcel.readString(),
       mobileNumber = parcel.readString(),
       price = parcel.readString(),
       photo = parcel.readString(),
       genre = parcel.readString(),
       zipcode = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel!!.writeString(userID)
        parcel.writeString(bookName)
        parcel.writeString(author)
        parcel.writeString(bookDescription)
        parcel.writeString(mobileNumber)
        parcel.writeString(price)
        parcel.writeString(photo)
        parcel.writeString(genre)
        parcel.writeString(zipcode)
    }

    override fun describeContents(): Int {
        return 0
    }

}