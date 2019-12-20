package com.hans.gruposs.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hans.gruposs.R
import com.hans.gruposs.common.PreferencesHelper
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {


    companion object {
        const val SPLASH_TIME:Long=3000  //ms
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val timer= Timer()

        timer.schedule(timerTask {
            verifySession()
        }, SPLASH_TIME)
    }

    private fun goToLogIn(){

        val intent= Intent(this, LogInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun verifySession(){

        val session = PreferencesHelper.isSignedIn(this@SplashActivity)
        intent = if (session) {
            Intent(this@SplashActivity, TransportListActivity::class.java)
        } else {
            Intent(this@SplashActivity, LogInActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
