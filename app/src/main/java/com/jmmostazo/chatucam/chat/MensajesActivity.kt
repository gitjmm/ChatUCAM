package com.jmmostazo.chatucam.chat



import com.jmmostazo.chatucam.R
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.jmmostazo.chatucam.adaptador.AdaptadorUsuario
import com.jmmostazo.chatucam.model.Usuario


//En esta clase progrmaremos la opción de pulsar sobre un usuario y que nos permita chatear
class MensajesActivity : AppCompatActivity() {

    private lateinit var Et_mensaje : EditText
    private lateinit var IB_Enviar : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajes)

        InicializarVistas()

        IB_Enviar.setOnClickListener {
            //notificar : boolean = true
            val mensaje = Et_mensaje.text.toString()
            if (mensaje.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Por favor ingrese un mensaje",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //EnviarMensaje(firebaseUser!!.uid, uid_usuario_seleccionado, mensaje)
                //Et_mensaje.setText("")
                EnviarMensaje()
            }
        }




    }
    private fun InicializarVistas() {
        Et_mensaje = findViewById(R.id.Et_mensaje)
        IB_Enviar = findViewById(R.id.IB_Enviar)
    }

    private fun EnviarMensaje(){
        Toast.makeText(
            applicationContext,
            "Prueba enviar mensaje",
            Toast.LENGTH_SHORT
        ).show()
    }
    /*private fun EnviarMensaje(uid_emisor: String, uid_receptor: String, mensaje: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val mensajeKey = reference.push().key

        val infoMensaje = HashMap<String, Any?>()
        infoMensaje["id_mensaje"] = mensajeKey
        infoMensaje["emisor"] = uid_emisor
        infoMensaje["receptor"] = uid_receptor
        infoMensaje["mensaje"] = mensaje
        infoMensaje["url"] = ""
        infoMensaje["visto"] = false
        reference.child("Chats").child(mensajeKey!!).setValue(infoMensaje)
            .addOnCompleteListener { tarea ->
                if (tarea.isSuccessful) {
                    val listaMensajesEmisor =
                        FirebaseDatabase.getInstance().reference.child("ListaMensajes")
                            .child(firebaseUser!!.uid)
                            .child(uid_usuario_seleccionado)

                    listaMensajesEmisor.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists()) {
                                listaMensajesEmisor.child("uid")
                                    .setValue(uid_usuario_seleccionado)
                            }

                            val listaMensajesReceptor =
                                FirebaseDatabase.getInstance().reference.child("ListaMensajes")
                                    .child(uid_usuario_seleccionado)
                                    .child(firebaseUser!!.uid)
                            listaMensajesReceptor.child("uid").setValue(firebaseUser!!.uid)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            }

    }*/
}