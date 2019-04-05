package ir.rainyday.android.common.supers


import android.content.Intent
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity


/**
 * Created by taghipour on 09/10/2017.
 */


abstract class SuperActivity : AppCompatActivity() {

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // handle arrow click here
        @Suppress("DEPRECATED_IDENTITY_EQUALS")
        if (item.itemId === android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }
}
