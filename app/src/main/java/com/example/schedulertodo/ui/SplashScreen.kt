package com.example.schedulertodo.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.schedulertodo.R
import com.example.schedulertodo.auth.LoginActivity

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 3000L
    var token :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val preference=getSharedPreferences("todo", Context.MODE_PRIVATE)
        val editor=preference.edit()
        token = preference.getString("token","")
        Handler().postDelayed(
            {
                if(token!=""){
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }
                else{
                    val i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }, SPLASH_TIME_OUT)
    }
}
