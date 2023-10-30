package com.jmmostazo.chatucam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jmmostazo.chatucam.fragmentos.FragmentoChats
import com.jmmostazo.chatucam.fragmentos.FragmentoUsuarios
import com.jmmostazo.chatucam.model.Usuario


class MainActivity : AppCompatActivity() {
    //private lateinit var Btn_cerrar_sesion : Button
    //Referencia a la base de datos firebase
    var reference : DatabaseReference?=null
    //usuario actual
    var firebaseuser : FirebaseUser?=null
    //Inicializamos TextView donde se va a mostrar el nombre de usuario conectado en tiempo real
    private lateinit var nombre_usuario : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //FirebaseAuth.getInstance().signOut()
        //Esta función inicializa todos los componentes que se muestran en la actividad principal
        InicializarComponentes()
        //Esta función obtiene el nombre de usuario de la base de datos y lo muestra en TextView
        ObtenerDato()
    }

    //Esta función inicializa todos los componentes
    fun InicializarComponentes(){

        val toolbar : Toolbar = findViewById(R.id.Toolbar_main)
        setSupportActionBar(toolbar)

        //Obtenemos el usuario de la base de datos
        firebaseuser = FirebaseAuth.getInstance().currentUser
        //Obtenemos la referencia a la base de datos
        reference = FirebaseDatabase.getInstance().reference.child("chatUCAM").child(firebaseuser!!.uid)
        //Obtenemos referencia a textview nombre de usuario
        nombre_usuario = findViewById(R.id.Nombre_usuario)

        val tabLayout : TabLayout = findViewById(R.id.TabLayoutMain)
        val viewPager : ViewPager = findViewById(R.id.ViewPagerMain)

        val viewpagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewpagerAdapter.addItem(FragmentoUsuarios(),"Usuarios")
        viewpagerAdapter.addItem(FragmentoChats(), "Chats")

        viewPager.adapter = viewpagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    //Con esta función obtenemos dato nombre de usuario en tiempo real
    fun ObtenerDato(){
        reference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot){
               //Si el usuario existe recuperamos su valor y lo incluimos en el TextView
                if(snapshot.exists()){
                    val usuario : Usuario? = snapshot.getValue(Usuario::class.java)
                    nombre_usuario.text = usuario!!.getN_Usuario()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }



    //Creamos una clase para inicializar el elemento ViewPager
    class ViewPagerAdapter(fragmentManager : FragmentManager): FragmentPagerAdapter(fragmentManager) {
        //Inicializamos y creamos la lista de fragmentos
        private val listaFragmentos : MutableList<Fragment> = ArrayList()
        private val listaTitulos : MutableList<String> = ArrayList()

        //Esta función devuelve el número de fragmentos
        override fun getCount(): Int {
            return listaFragmentos.size
        }
        //Esta función devuelve un fragmento dada su posición
        override fun getItem(position: Int): Fragment {
            return listaFragmentos[position]
        }
        //Esta función devuelve un título
        override fun getPageTitle(position: Int): CharSequence? {
            return listaTitulos[position]
        }

        //Esta función permite completar las listas con los fragmentos y sus titulos
        fun addItem(fragment: Fragment, titulo:String){
            listaFragmentos.add(fragment)
            listaTitulos.add(titulo)
        }
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