package com.jmmostazo.chatucam
import android.content.Intent
import com.jmmostazo.chatucam.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
/*
En esta actividad se realiza lo siguiente:
1. Registro de usuario en Firebase
2. Login de usuario en Firebase
 */
class RegistroActivity : AppCompatActivity() {
    private lateinit var Et_nombre_usuario : EditText
    private lateinit var Et_email : EditText
    private lateinit var Et_password : EditText
    private lateinit var Et_r_password : EditText
    private lateinit var Btn_registrar : Button
    //Estas variables nos sirven para la autentificación y referenciar a la bd firebase
    private lateinit var auth : FirebaseAuth
    private lateinit var reference : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        //supportActionBar!!.title ="Registros"
        //Llamamos a la función InicializarVariables
        InicializarVariables()

        Btn_registrar.setOnClickListener {
            ValidarDatos()
        }


    }
    //Función que inicializa las variables con valores recogidos desde la actividad Registro
    private fun InicializarVariables(){
        Et_nombre_usuario = findViewById(R.id.Et_nombre_usuario)
        Et_email = findViewById(R.id.Et_email)
        Et_password= findViewById(R.id.Et_password)
        Et_r_password = findViewById(R.id.Et_r_password)
        Btn_registrar = findViewById(R.id.Btn_registrar)

        //Creamos la instancia para Firebase
        auth = FirebaseAuth.getInstance()


    }
    //Función que se encarga de validar los datos introducidos en la actividad Registro
    private fun ValidarDatos(){
        val nombre_usuario : String = Et_nombre_usuario.text.toString()
        val email : String = Et_email.text.toString()
        val password : String = Et_password.text.toString()
        val r_password : String = Et_r_password.text.toString()

        if (nombre_usuario.isEmpty()){
            Toast.makeText(applicationContext,"Introduce nombre usuario",Toast.LENGTH_SHORT).show()
        }else if (email.isEmpty()){
            Toast.makeText(applicationContext,"Introduce email",Toast.LENGTH_SHORT).show()
        }else if (password.isEmpty()){
            Toast.makeText(applicationContext,"Introduce password",Toast.LENGTH_SHORT).show()
        }else if (r_password.isEmpty()){
            Toast.makeText(applicationContext,"Repite password",Toast.LENGTH_SHORT).show()
        }else{
            RegistrarUsuario(email,password)
        }


    }
    /*
    En esta función se realiza lo siguiente:
    1. Se crea autentificación de usuario en Firebase
    2. Se guarda en Firebase la información de usuario mediante un hashmap
    3. Una vez guardada la información se vuelve a la Actividad principal
    4. Se controlan las excepciones
     */
    private fun RegistrarUsuario(email : String, password : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var uid: String = "" //identificador de cada usuario
                    uid = auth.currentUser!!.uid //Vamos a obtener el id del usuario actual
                    reference =
                        FirebaseDatabase.getInstance().reference.child("Usuarios").child(uid)

                    //Creamos un hashmap para guardar los datos. Posteriormente serán recuperados por firebase
                    val hashMap = HashMap<String, Any>()
                    val h_nombre_usuario: String = Et_nombre_usuario.text.toString()
                    val h_email: String = Et_email.text.toString()

                    //Almacenamos en el hashmap uid, nombre usuario e email
                    hashMap["uid"] = uid
                    hashMap["nom_usuario"] = h_nombre_usuario
                    hashMap["email"] = h_email
                    hashMap["imagen"] = ""
                    hashMap["buscar"] = h_nombre_usuario.lowercase()

                    //Actualizamos los datos en Firebase con el hashMap. Posteriormente volvemos a MainActivity meidante intent
                    reference.updateChildren(hashMap).addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            val intent = Intent(this@RegistroActivity, MainActivity::class.java)
                            Toast.makeText(
                                applicationContext,
                                "Usuario registrado con éxito",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(intent)
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_LONG).show()
                    }
                //Si task no se produce correctamente
                } else {
                    Toast.makeText(applicationContext, "Ha ocurrido un error", Toast.LENGTH_LONG)
                        .show()
                }
            }.addOnFailureListener{e ->
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}