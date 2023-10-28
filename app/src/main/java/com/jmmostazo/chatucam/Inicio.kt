/*
Autor: Jorge Martínez Mostazo
En esta clase Inicio vamos a realizar la llamada a las actividades de registro y al login

 */
package com.jmmostazo.chatucam


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class Inicio : AppCompatActivity() {

    private lateinit var Btn_ir_registros : Button
    private lateinit var Btn_ir_logueo : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        //Instanciamos los dos botones obteniendo su id
        Btn_ir_registros = findViewById(R.id.Btn_ir_registros)
        Btn_ir_logueo = findViewById(R.id.Btn_ir_logueo)
        //Evento cuando pulsamos el botón Registro
        Btn_ir_registros.setOnClickListener {
            val intent = Intent(this@Inicio, RegistroActivity::class.java)
            Toast.makeText(applicationContext,"Registros",Toast.LENGTH_LONG).show()
            startActivity(intent)
        }

        //Evento cuando pulsamos el botón Registro
        Btn_ir_logueo.setOnClickListener {
            val intent = Intent(this@Inicio, RegistroActivity::class.java)
            Toast.makeText(applicationContext,"Login",Toast.LENGTH_LONG).show()
            startActivity(intent)
        }


    }


}