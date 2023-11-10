package com.jmmostazo.chatucam.fragmentos

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.jmmostazo.chatucam.adaptador.AdaptadorUsuario
//import com.jmmostazo.chatucam.model.ListaChats
import com.jmmostazo.chatucam.model.Usuario
import com.jmmostazo.chatucam.R
import com.jmmostazo.chatucam.model.ListaChats

//En este fragment vamos a mostrar los chats realizados por cada usuario
class FragmentoChats : Fragment() {

    private var usuarioAdaptador : AdaptadorUsuario?=null
    private var usuarioLista : List<Usuario>?=null
    private var usuarioListaChats : List<ListaChats>?= null

    lateinit var RV_ListaChats : RecyclerView
    private var firebaseUser : FirebaseUser?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view : View = inflater.inflate(R.layout.fragment_fragmento_chats, container, false)

        RV_ListaChats = view.findViewById(R.id.RV_ListaChats)
        RV_ListaChats.setHasFixedSize(true)
        RV_ListaChats.layoutManager = LinearLayoutManager(context)

        //Recuperamos el usuario actual
        firebaseUser = FirebaseAuth.getInstance().currentUser
        usuarioListaChats = ArrayList()
        val ref = FirebaseDatabase.getInstance().reference.child("ListaMensajes").child(firebaseUser!!.uid)
        ref!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (usuarioListaChats as ArrayList).clear()
                for (dataSnapShot in snapshot.children){
                    val chatList = dataSnapShot.getValue(ListaChats::class.java)
                    (usuarioListaChats as ArrayList).add(chatList!!)
                }
                //Llamos
                RecuperarListaChats()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return view
    }

    //Esta función va a recuperar la lista de chats de un usuario y la mostrará en el fragment
    private fun RecuperarListaChats(){
        usuarioLista = ArrayList()
        val reference = FirebaseDatabase.getInstance().reference.child("Usuarios")
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (usuarioLista as ArrayList).clear()
                for (dataSnapshot in snapshot.children){
                    val user = dataSnapshot.getValue(Usuario::class.java)
                    //Añadimos a la lista de usuarios el usuario con el que se establece conversación
                    for (cadaLista in usuarioListaChats!!){
                        if (user!!.getUid().equals(cadaLista.getUid())){
                            (usuarioLista as ArrayList).add(user!!)
                        }
                    }
                    //Mostramos por pantalla los usuarios con los que establecemos conversación
                    usuarioAdaptador = AdaptadorUsuario(context!!, (usuarioLista as ArrayList<Usuario>))
                    RV_ListaChats.adapter = usuarioAdaptador
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}