package com.jmmostazo.chatucam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.jmmostazo.chatucam.fragmentos.FragmentoChats
import com.jmmostazo.chatucam.fragmentos.FragmentoUsuarios

class MainActivity : AppCompatActivity() {
    private lateinit var Btn_cerrar_sesion : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //FirebaseAuth.getInstance().signOut()
        InicializarComponentes()
    }

    //Esta función inicializa todos los componentes
    fun InicializarComponentes(){
        val tabLayout : TabLayout = findViewById(R.id.TabLayoutMain)
        val viewPager : ViewPager = findViewById(R.id.ViewPagerMain)

        val viewpagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewpagerAdapter.addItem(FragmentoUsuarios(),"Usuarios")
        viewpagerAdapter.addItem(FragmentoChats(), "Chats")

        viewPager.adapter = viewpagerAdapter
        tabLayout.setupWithViewPager(viewPager)
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