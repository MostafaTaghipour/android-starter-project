package ir.rainyday.android.starter.modules.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ir.rainyday.android.starter.R
import ir.rainyday.android.starter.modules.shared.Navigator
import ir.rainyday.thememanager.ThemeManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.getInstance().applyTheme(this)
        setContentView(R.layout.activity_splash)

    }


    override fun onStart() {
        super.onStart()
        Navigator.sample(this)
    }

}