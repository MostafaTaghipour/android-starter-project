package ir.rainyday.android.starter.modules.shared.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import ir.rainyday.android.starter.R
import ir.rainyday.android.starter.helpers.*
import ir.rainyday.android.starter.modules.shared.events.*
import io.reactivex.disposables.CompositeDisposable
import ir.rainyday.android.common.helpers.dismissKeyboard
import ir.rainyday.android.common.helpers.launchInternetSetting
import ir.rainyday.android.common.helpers.toast
import ir.rainyday.fontmanager.FontManager
import ir.rainyday.localemanager.LocaleManager
import ir.rainyday.thememanager.ThemeManager


/**
 * Created by taghipour on 09/10/2017.
 */


abstract class BaseMvvmActivity<VM : BaseViewModel> : AppCompatActivity() {

    protected var viewModel: VM? = null
    protected val disposable by lazy { CompositeDisposable() }


    override fun attachBaseContext(newBase: Context) {
        val localeBase = LocaleManager.getInstance().wrapContext(newBase)
        val wrapContext = FontManager.wrapContext(localeBase)
        super.attachBaseContext(wrapContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.getInstance().applyTheme(this)
        setContentView(getContentView())
        viewModel = generateViewModel()
        setupView()

        if (viewModel != null)
            onReady(savedInstanceState, viewModel!!)
        else
            onReady(savedInstanceState)
    }


    private fun setupView() {
        findViewById<Toolbar>(R.id.toolbar).let {
            setSupportActionBar(it)
        }

        viewModel?.observeEvent(this, NoInternetAccessEvent::class.java, Observer { event -> onLostInternetConnection(event) })
        viewModel?.observeEvent(this, HttpErrorEvent::class.java, Observer { event -> onHttpError(event) })
        viewModel?.observeEvent(this, MessageEvent::class.java, Observer { event -> onMessageEvent(event) })
        viewModel?.observeEvent(this, NavigationEvent::class.java, Observer { event -> onNavigationRequest(event) })
        viewModel?.observeEvent(this, FinishEvent::class.java, Observer { event -> onFinishEvent(event) })

    }


    override fun onPause() {
        dismissKeyboard()
        super.onPause()
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    private fun onLostInternetConnection(event: NoInternetAccessEvent?) {
        event?.let {
            snack(R.string.error_internet_connection) {}
        }
    }

    private fun onFinishEvent(event: FinishEvent?) {
        event?.let {
            event.handled = true
            finish()
        }
    }

    protected open fun onHttpError(event: HttpErrorEvent?) {
        event?.error?.localizedDescription?.let {
            errorToast(it, duration = Toast.LENGTH_LONG)
            event.handled = true
        }
    }

    protected open fun onMessageEvent(event: MessageEvent?) {
        event?.let {
            when (event.type) {
                MessageType.SUCCESS -> successToast(event.message, event.duration)
                MessageType.ERROR -> errorToast(event.message, event.duration)
                MessageType.INFO -> infoToast(event.message, event.duration)
                MessageType.WARNING -> warningToast(event.message, event.duration)
                else -> toast(event.message, event.duration, Gravity.CENTER)
            }
            event.handled = true
        }
    }

    protected open fun onNavigationRequest(event: NavigationEvent?) {
        //todo unimplemented
//        if (event?.destination == AuthActivity::class.java) {
//            Navigator.logout(googleSignInHelper)
//        }
    }

    abstract fun getContentView(): Int
    open fun generateViewModel(): VM? = null
    open fun onReady(savedInstanceState: Bundle?) {}
    open fun onReady(savedInstanceState: Bundle?, viewModel: VM) {}
}
