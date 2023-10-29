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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Inicio : AppCompatActivity() {

    private lateinit var Btn_ir_registros : Button
    private lateinit var Btn_ir_logueo : Button

    //variable de usuario de firebase para el login
    var firebaseUser : FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        //Instanciamos los dos botones obteniendo su id
        Btn_ir_registros = findViewById(R.id.Btn_ir_registros)
        Btn_ir_logueo = findViewById(R.id.Btn_ir_logueo)

        //Evento cuando pulsamos el botón Registro
        Btn_ir_registros.setOnClickListener {
            val intent = Intent(this@Inicio, RegistroActivity::class.java)
            Toast.makeText(applicationContext,"Registro de usuario",Toast.LENGTH_LONG).show()
            startActivity(intent)
        }

        //Evento cuando pulsamos el botón Login
        Btn_ir_logueo.setOnClickListener {
            val intent = Intent(this@Inicio, LoginActivity::class.java)
            Toast.makeText(applicationContext,"Login de usuario",Toast.LENGTH_LONG).show()
            startActivity(intent)
        }


    }
    //En esta función vamos a comprobar el login de usuario en Firebase.
    //Si existe el usuario nos vamos a la actividad principal
    private fun ComprobarSesion(){
        firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser!=null){
            val intent = Intent(this@Inicio, MainActivity::class.java)
            Toast.makeText(applicationContext,"Sesión Activa",Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()
        }
    }

    //Cuando inicie la aplicación comprobará en Firebase si la sesión de usuario está activa
    override fun onStart(){
        ComprobarSesion()
        super.onStart()
    }

}