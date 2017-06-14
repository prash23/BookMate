package prashanth.bookmate.models

/**
 * Created by Prashanth on 4/19/2017.
 */

class Books(var userID: String, val bookName: String, val author: String, val bookDescription: String, var mobileNumber: String, var price: String, var photo: String, var genre: String, var zipcode: String)
{
 constructor() : this("","","","","","","","","")

 override fun toString(): String {
  return "Books(userID='$userID', bookName='$bookName', author='$author', bookDescription='$bookDescription', mobileNumber='$mobileNumber', price='$price', photo='$photo', genre='$genre', zipcode='$zipcode')"
 }


}