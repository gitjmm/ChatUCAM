package com.jmmostazo.chatucam.perfil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jmmostazo.chatucam.R
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.jmmostazo.chatucam.MainActivity
import com.jmmostazo.chatucam.fragmentos.FragmentoUsuarios
import com.jmmostazo.chatucam.model.Usuario

class PerfilActivity : AppCompatActivity() {

    private lateinit var P_imagen : ImageView
    private lateinit var P_n_usuario : TextView
    private lateinit var P_email : TextView
    private lateinit var P_proveedor : TextView
    private lateinit var P_nombres : EditText
    private lateinit var P_apellidos : EditText
    private lateinit var P_profesion : EditText
    private lateinit var P_domicilio : EditText
    private lateinit var P_edad : EditText
    private lateinit var P_telefono : TextView
    private lateinit var Btn_guardar : Button

    //variables de firebase
    var user : FirebaseUser?=null
    var reference : DatabaseReference?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        InicializarVariables()
        //Cuando pulsamos Guardar actualizamos la información
        Btn_guardar.setOnClickListener {
            ObtenerDatos()
            ActualizarInformacion()

        }
    }
    //Estsa función inicializa todas las variables de la actividad Perfil usuario
    private fun InicializarVariables(){
        P_imagen = findViewById(R.id.P_imagen)
        P_n_usuario = findViewById(R.id.P_n_usuario)
        //P_proveedor = findViewById(R.id.P_proveedor)
        P_email = findViewById(R.id.P_email)
        P_nombres = findViewById(R.id.P_nombres)
        P_apellidos = findViewById(R.id.P_apellidos)
        P_profesion = findViewById(R.id.P_profesion)
        P_domicilio = findViewById(R.id.P_domicilio)
        P_edad = findViewById(R.id.P_edad)
        P_telefono = findViewById(R.id.P_telefono)
        Btn_guardar = findViewById(R.id.Btn_Guardar)


        //obtenemos usuario actual y referencia a la base de datos
        user = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().reference.child("Usuarios").child(user!!.uid)

    }
    //Esta función obtiene los datos de la base de datos
    private fun ObtenerDatos() {
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //Obtenemos los datos de Firebase
                    val usuario: Usuario? = snapshot.getValue(Usuario::class.java)
                    val str_n_usuario = usuario!!.getN_Usuario()
                    val str_email = usuario.getEmail()
                    //val str_proveedor = usuario.getProveedor()
                    val str_nombres = usuario.getNombre()
                    val str_apellidos = usuario.getApellido()
                    val str_profesion = usuario.getProfesion()
                    val str_domicilio = usuario.getDomicilio()
                    val str_edad = usuario.getEdad()
                    val str_telefono = usuario.getTelefono()

                    //Seteamos la información en las vistas
                    P_n_usuario.text = str_n_usuario
                    P_email.text = str_email
                    //P_proveedor.text = str_proveedor
                    P_nombres.setText(str_nombres)
                    P_apellidos.setText(str_apellidos)
                    P_profesion.setText(str_profesion)
                    P_domicilio.setText(str_domicilio)
                    P_edad.setText(str_edad)
                    P_telefono.setText(str_telefono)
                    Glide.with(applicationContext).load(usuario.getImagen())
                        .placeholder(R.drawable.ic_item_usuario).into(P_imagen)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }
    //Esta función  actualiza la información del perfil de usuario
    private fun ActualizarInformacion(){
        val str_nombres = P_nombres.text.toString()
        val str_apellidos = P_apellidos.text.toString()
        val str_profesion = P_profesion.text.toString()
        val str_domicilio = P_domicilio.text.toString()
        val str_edad = P_edad.text.toString()
        val str_telefono = P_telefono.text.toString()

        val hashmap = HashMap<String, Any>()
        hashmap["nombre"] = str_nombres
        hashmap["apellido"] = str_apellidos
        hashmap["profesion"] = str_profesion
        hashmap["domicilio"] = str_domicilio
        hashmap["edad"] = str_edad
        hashmap["telefono"] = str_telefono

        reference!!.updateChildren(hashmap).addOnCompleteListener{task->
            if (task.isSuccessful){
                Toast.makeText(applicationContext,"Se han actualizado los datos", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,"No se han actualizado los datos", Toast.LENGTH_SHORT).show()

            }
        }.addOnFailureListener{e->
            Toast.makeText(applicationContext,"Ha ocurrido un error ${e.message}", Toast.LENGTH_SHORT).show()

        }


    }
}

