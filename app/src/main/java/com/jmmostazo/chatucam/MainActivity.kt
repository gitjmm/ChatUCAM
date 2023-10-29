package com.jmmostazo.chatucam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var Btn_cerrar_sesion : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }



    //Con esta función creamos las opciones de menu y visualizamos el menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //var inflater : MenuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_principal,menu)
        return true
    }

    //Esta función permite seleccionar una opción del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Si seleccionamos un item y es Cerrar sesión
        return when(item.itemId) {
            R.id.menu_salir->{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, Inicio::class.java)
                startActivity(intent)
                return true
            }
            //En caso de no seleccionar item Cerrar sesión
            else -> super.onOptionsItemSelected(item)
        }
    }


}