package com.jmmostazo.chatucam.fragmentos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jmmostazo.chatucam.R
import com.jmmostazo.chatucam.adaptador.AdaptadorUsuario
import com.jmmostazo.chatucam.model.Usuario

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * En este fragment vamos a mostrar el listado de usuarios de firebase
 *
 *
 */
class FragmentoUsuarios : Fragment() {

    private var usuarioAdpatador: AdaptadorUsuario?=null
    private var usuariolista : List<Usuario>?=null
    private var rvUsuarios : RecyclerView?=null
    private lateinit var Et_buscar_usuario:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Hacemos inflate del layout fragmento_usuarios
        val view : View  = inflater.inflate(R.layout.fragment_fragmento_usuarios, container, false)

        rvUsuarios = view.findViewById(R.id.RV_usuario)
        rvUsuarios!!.setHasFixedSize(true)
        rvUsuarios!!.layoutManager = LinearLayoutManager(context)
        Et_buscar_usuario = view.findViewById(R.id.Et_buscar_usuario)
        //Array que va a contener los usuarios
        usuariolista = ArrayList()
        ObtenerUsuariosBD()

        //Realizamos la busqueda de un usuario mediante el edittext para buscar usuario
        Et_buscar_usuario.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }
            //Lo que escribamos en el teclado estará en minúsculas
            override fun onTextChanged(usuario: CharSequence?, p1: Int, p2: Int, p3: Int) {
                BuscarUsuario(usuario.toString().lowercase())
            }

            override fun afterTextChanged(p0: Editable?) {
                TODO("Not yet implemented")
            }

        })

        return view
    }
    //Con esta función obtenemos los usuarios de la base de datos de firebase.
    // Ordenamos los usuarios por el campo n_usuario
    private fun ObtenerUsuariosBD(){

        //Obtenemos el usuario actual de firebase
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
        //Obtenemos una referencia a los usuarios de la base de datos ordenados por el campo n_usuario
        val reference = FirebaseDatabase.getInstance().reference.child("Usuarios").orderByChild("nom_usuario")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (usuariolista as ArrayList<Usuario>).clear()
                //Listamos los usuarios que solo han iniciado sesión.
                //Si no es usuario de firebase lo añadimos al arraylist
                for (sh in snapshot.children) {
                    val usuario: Usuario? = sh.getValue(Usuario::class.java)
                    //Solo muestra los usuarios que no están en la sesión
                    if (!(usuario!!.getUid()).equals(firebaseUser))
                        {
                            (usuariolista as ArrayList<Usuario>).add(usuario)
                        }
                    }
                usuarioAdpatador = AdaptadorUsuario(context!!, usuariolista!!)
                rvUsuarios!!.adapter = usuarioAdpatador

                }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    //Función que busca un usuario en la base de datos firebase mediante el campo buscar (campo con nombre en minúsculas)
    //El listado de usuarios se irá mostrando conforme lo que se vaya escribiendo en el Edittext
    private fun BuscarUsuario(usuario:String){
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
        val consulta = FirebaseDatabase.getInstance().reference.child("Usuarios").orderByChild("buscar")
            .startAt(usuario).endAt(usuario + "\uf8ff")
        consulta.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                (usuariolista as ArrayList<Usuario>).clear()
                //Si el Edittext está vacío mostramos los usuarios
                if (Et_buscar_usuario.text.toString().isEmpty()){
                    //Listamos los usuarios que solo han iniciado sesión.
                    //Si no es usuario de firebase lo añadimos al arraylist
                    for (sh in snapshot.children) {
                        val usuario: Usuario? = sh.getValue(Usuario::class.java)
                        //Solo muestra los usuarios que no están en la sesión
                        if (!(usuario!!.getUid()).equals(firebaseUser))
                        {
                            (usuariolista as ArrayList<Usuario>).add(usuario)
                        }
                    }
                }

                usuarioAdpatador = AdaptadorUsuario(context!!, usuariolista!!)
                rvUsuarios!!.adapter = usuarioAdpatador
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

}