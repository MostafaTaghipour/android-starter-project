package ir.rainyday.android.starter.modules.shared.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import ir.rainyday.android.starter.R
import ir.rainyday.android.common.helpers.dismissKeyboard
import ir.rainyday.android.common.supers.SuperActivity
import ir.rainyday.fontmanager.FontManager
import ir.rainyday.localemanager.LocaleManager
import ir.rainyday.thememanager.ThemeManager


abstract class BaseActivity : SuperActivity() {

    override fun attachBaseContext(newBase: Context) {
        val localeBase = LocaleManager.getInstance().wrapContext(newBase)
        val wrapContext = FontManager.wrapContext(localeBase)
        super.attachBaseContext(wrapContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.getInstance().applyTheme(this)
        setContentView(getContentView())

        findViewById<Toolbar>(R.id.toolbar).let {
            setSupportActionBar(it)
        }

        onReady(savedInstanceState)
    }

    override fun onPause() {
        dismissKeyboard()
        super.onPause()
    }


    abstract fun getContentView(): Int
    open fun onReady(savedInstanceState: Bundle?) {}
}
