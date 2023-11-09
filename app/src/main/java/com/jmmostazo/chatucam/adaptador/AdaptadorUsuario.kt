package com.jmmostazo.chatucam.adaptador

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jmmostazo.chatucam.R
import com.jmmostazo.chatucam.chat.MensajesActivity
import com.jmmostazo.chatucam.model.Usuario

/*
En esta clase vamos a definir el adaptador de usuario
Daremos valores a los elementos definidos en el layout item_usuario.xml
 */


class AdaptadorUsuario(context : Context, listaUsuarios: List<Usuario>) : RecyclerView.Adapter<AdaptadorUsuario.ViewHolder?>(){

    //Contexto y lista de usuarios
    private val context : Context
    private val listaUsuarios : List<Usuario>

    init {
        this.context = context
        this.listaUsuarios = listaUsuarios
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var nombre_usuario:TextView
        var email_usuario:TextView
        var imagen_usuario:ImageView

        init{
            nombre_usuario = itemView.findViewById(R.id.Item_nombre_usuario)
            email_usuario = itemView.findViewById(R.id.Item_email_usuario)
            imagen_usuario = itemView.findViewById(R.id.Item_imagen)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_usuario,parent,false)
        return ViewHolder(view)
    }
    //Devuelve el número de usuarios
    override fun getItemCount(): Int {
        return listaUsuarios.size
    }
    //Asignamos los datos de firebase en las vistas de item_usuario.xml
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario: Usuario = listaUsuarios[position]
        holder.nombre_usuario.text = usuario.getN_Usuario()
        holder.email_usuario.text = usuario.getEmail()
        //Mientras se carga la imagen desde firebase se va a mostrar el icono
        Glide.with(context).load(usuario.getImagen()).placeholder(R.drawable.ic_item_usuario)
            .into(holder.imagen_usuario)

        //Se muestra un usuario cuando pulsamos sobre el mismo en la lista de usuarios.Pasamos el uid
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MensajesActivity::class.java)
            //Enviamos el uid del usuario seleccionado
            intent.putExtra("uid_usuario", usuario.getUid())
            //Toast.makeText(context, "El usuario seleccionado es: "+usuario.getN_Usuario(),Toast.LENGTH_SHORT).show()
            context.startActivity(intent)
        }
    }
}