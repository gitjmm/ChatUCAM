package com.jmmostazo.chatucam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
/*
En esta clase vamos a recoger la información correspondiente al login de usuario.
También realizaremos la comprobación de dicho login en Firebase
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var Et_email : EditText
    private lateinit var Et_password : EditText
    private lateinit var Btn_login : Button
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        InicializarVariables()

        Btn_login.setOnClickListener {
            ValidarDatos()
        }
    }


    private fun InicializarVariables(){
        Et_email = findViewById(R.id.Et_email_login)
        Et_password = findViewById(R.id.Et_password_login)
        Btn_login = findViewById(R.id.Btn_login)
        auth = FirebaseAuth.getInstance()

    }

    private fun ValidarDatos(){
        val email : String = Et_email.text.toString()
        val password : String = Et_password.text.toString()
        /*if (!email.isEmpty() && !password.isEmpty()) {
            Toast.makeText(applicationContext, "Haciendo Login", Toast.LENGTH_LONG).show()
            LoginUsuario(email, password)
        }*/

        if (email.isEmpty()){
            Toast.makeText(applicationContext,"Introduzca email",Toast.LENGTH_LONG).show()
        }else if(password.isEmpty()){
            Toast.makeText(applicationContext,"Introduzca contraseña",Toast.LENGTH_LONG).show()
        }else {
            //Toast.makeText(applicationContext, "Haciendo Login", Toast.LENGTH_LONG).show()
            LoginUsuario(email, password)
        }

    }
    //En esta función realizamos el login de usuario en Firebase mediante email y password
    private fun LoginUsuario(email : String,password : String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    Toast.makeText(applicationContext,"Se ha logueado",Toast.LENGTH_LONG).show()
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(applicationContext,"Ha ocurrido un error",Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener{
                e-> Toast.makeText(applicationContext, "{ ${e.message }}",Toast.LENGTH_LONG).show()
            }


    }


}