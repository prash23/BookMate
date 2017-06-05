package prashanth.bookmate

import android.content.Intent
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import com.google.android.gms.common.api.GoogleApiClient

class HomePage : AppCompatActivity() {
    private val googleApiClient: GoogleApiClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)
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
        }
        return true
    }

    companion object {

        private val PLACE_PICKER_REQUEST = 1000
    }
}
