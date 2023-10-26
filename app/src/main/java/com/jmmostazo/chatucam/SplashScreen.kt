package com.jmmostazo.chatucam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        MostrarBienvenida()
    }

    fun MostrarBienvenida(){
        //Permite ejecutar un codigo pasado cierto tiempo
        object : CountDownTimer(5000,1000){
            override fun onTick(p0: Long) {

            }
            //Una vez finalizado el tiempo nos dirigimos al Activity principal Inicio
            override fun onFinish() {
                val intent = Intent(applicationContext,Inicio::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}