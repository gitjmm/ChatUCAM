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
    private lateinit var imagen_perfil_chat : ImageView
    private lateinit var Et_mensaje : EditText
    private lateinit var IB_Enviar : ImageButton
    private lateinit var IB_Adjuntar : ImageButton
    private lateinit var N_usuario_chat : TextView
    var uid_usuario_seleccionado : String = ""
    var firebaseUser : FirebaseUser ?= null
    private var imagenUri : Uri ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensajes)

        InicializarVistas()
        ObtenerUid()
        LeerInfoUsuarioSeleccionado()

        //Cuando pulsamos el botón de adjuntar imagen llamamos a AbrirGaleria
        IB_Adjuntar.setOnClickListener{
            AbrirGaleria()
        }


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
                //Esta función EnviarMensaje recibe: uid del usuario actual, usuario seleccionado del adaptador, mensaje
                EnviarMensaje(firebaseUser!!.uid, uid_usuario_seleccionado, mensaje)
                Et_mensaje.setText("")

            }
        }




    }
    //Esta función InicializarVistas inicializamos los elementos utilizados
    //así como el usuario actual de la base de datos firebase
    private fun InicializarVistas() {
        Et_mensaje = findViewById(R.id.Et_mensaje)
        IB_Enviar = findViewById(R.id.IB_Enviar)
        IB_Adjuntar = findViewById(R.id.IB_Adjuntar)
        imagen_perfil_chat = findViewById(R.id.imagen_perfil_chat)
        N_usuario_chat = findViewById(R.id.N_usuario_chat)
        firebaseUser = FirebaseAuth.getInstance().currentUser

    }
    //En esta función obtenemos el uid que seleccionamos del adaptador
    private fun ObtenerUid(){
        intent = intent
        uid_usuario_seleccionado = intent.getStringExtra("uid_usuario").toString()
    }

    /*private fun EnviarMensaje(){
        Toast.makeText(
            applicationContext,
            "Prueba enviar mensaje",
            Toast.LENGTH_SHORT
        ).show()
    }*/

    //Leemos información del usuario seleccionado en el fragment usuarios
    private fun LeerInfoUsuarioSeleccionado() {
        val reference = FirebaseDatabase.getInstance().reference.child("Usuarios")
            .child(uid_usuario_seleccionado)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario: Usuario? = snapshot.getValue(Usuario::class.java)
                //Obtener el nombre de usuario
                N_usuario_chat.text = usuario!!.getN_Usuario()
                //Obtenemos la imagen de perfil
                Glide.with(applicationContext).load(usuario.getImagen())
                    .placeholder(R.drawable.ic_item_usuario)
                    .into(imagen_perfil_chat)

                //RecuperarMensajes(firebaseUser!!.uid, uid_usuario_seleccionado, usuario.getImagen())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
        //Esta función EnviarMensaje recibe: uid del usuario actual, usuario seleccionado del adaptador, mensaje
        //Se entabla una conversación entre emisor y receptor
        //Se alamacenará en la base de datos lista de mensajes donde enlazamos
        private fun EnviarMensaje(uid_emisor: String, uid_receptor: String, mensaje: String) {
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

    }

    //Esta función se va a encargar de abrir la galería para seleccionar una imagen
    private fun AbrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galeriaARL.launch(intent)
    }


    //galeriaARL Activity Resource Launcher
    //Esta actividad nos va a permitir seleccionar una imagen y enviarla por nuestro chat
    //Esta imagen se guardará en Firebase en la base de datos Chat
    private val galeriaARL = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {resultado->
            if (resultado.resultCode == RESULT_OK){
                val data = resultado.data
                imagenUri = data!!.data

                //Cargamos la imagen
                val cargandoImagen = ProgressDialog(this@MensajesActivity)
                cargandoImagen.setMessage("Por favor espere, la imagen se está enviando")
                cargandoImagen.setCanceledOnTouchOutside(false)
                cargandoImagen.show()

                //Creamos la carpeta donde almacenamos las imagenes
                val carpetaImagenes = FirebaseStorage.getInstance().reference.child("Imágenes de mensajes")
                val reference = FirebaseDatabase.getInstance().reference
                //Creamos la id del mensaje y el nombre. Le añadimos .jpg
                val idMensaje = reference.push().key
                val nombreImagen = carpetaImagenes.child("$idMensaje.jpg")
                //Cargamos la imagen
                val uploadTask : StorageTask<*>
                uploadTask = nombreImagen.putFile(imagenUri!!)
                uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{task->
                    //Control de excepciones
                    if (!task.isSuccessful){
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation nombreImagen.downloadUrl
                }).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        cargandoImagen.dismiss()
                        val downloadUrl = task.result
                        val url = downloadUrl.toString()

                        //Establecemos los atributos de un mensaje
                        val infoMensajeImagen = HashMap<String, Any?>()
                        infoMensajeImagen["id_mensaje"] = idMensaje
                        infoMensajeImagen["emisor"] = firebaseUser!!.uid
                        infoMensajeImagen["receptor"] = uid_usuario_seleccionado
                        infoMensajeImagen["mensaje"] = "Se ha enviado la imagen"
                        infoMensajeImagen["url"] = url
                        infoMensajeImagen["visto"] = false

                        //Pasamos la información anterior a la base de datos
                        /*reference.child("Chats").child(idMensaje!!).setValue(infoMensajeImagen)
                            .addOnCompleteListener { tarea->
                                if (tarea.isSuccessful){
                                    val usuarioReference = FirebaseDatabase.getInstance().reference
                                        .child("Usuarios").child(firebaseUser!!.uid)
                                    usuarioReference.addValueEventListener(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val usuario = snapshot.getValue(Usuario::class.java)
                                            if (notificar){
                                                enviarNotificacion(
                                                    uid_usuario_seleccionado,
                                                    usuario!!.getN_Usuario(),
                                                    "Se ha enviado la imagen")
                                            }
                                            notificar = false
                                        }

                                        override fun onCancelled(error: DatabaseError) {

                                        }
                                    })

                                }
                            }*/
                        //Pasamos la información anterior a la base de datos
                        reference.child("Chats").child(idMensaje!!).setValue(infoMensajeImagen)
                            .addOnCompleteListener { tarea->
                                if (tarea.isSuccessful){
                                    val listaMensajesEmisor = FirebaseDatabase.getInstance().reference.child("ListaMensajes")
                                        .child(firebaseUser!!.uid)
                                        .child(uid_usuario_seleccionado)

                                    listaMensajesEmisor.addListenerForSingleValueEvent(object  : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (!snapshot.exists()){
                                                listaMensajesEmisor.child("uid").setValue(uid_usuario_seleccionado)
                                            }

                                            val listaMensajesReceptor = FirebaseDatabase.getInstance().reference.child("ListaMensajes")
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
                        Toast.makeText(applicationContext,"La imagen se ha enviado con éxito", Toast.LENGTH_SHORT).show()

                    }
                }
            }
            else{
                Toast.makeText(applicationContext,"Cancelado por el usuario", Toast.LENGTH_SHORT).show()
            }

        }
    )


}