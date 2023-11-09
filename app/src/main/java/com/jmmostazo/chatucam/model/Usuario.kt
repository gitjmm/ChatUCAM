package com.jmmostazo.chatucam.model

//Clase usuario correspondiente al usuario de firebase

class Usuario {
    private var uid : String = ""
    private var nom_usuario : String = ""
    private var email : String = ""
    private var telefono : String = ""
    private var imagen : String = ""
    private var buscar : String = ""
    private var nombre : String = ""
    private var apellido : String = ""
    private var edad : String = ""
    private var profesion : String = ""
    private var domicilio : String = ""

    constructor()

    constructor(
        uid: String,
        nom_usuario: String,
        email: String,
        //telefono: String,
        imagen: String,
        buscar: String,
        nombre: String,
        apellido: String,
        edad: String,
        profesion: String,
        domicilio: String
    ) {
        this.uid = uid
        this.nom_usuario = nom_usuario
        this.email = email
        //this.telefono = telefono
        this.imagen = imagen
        this.buscar = buscar
        this.nombre = nombre
        this.apellido = apellido
        this.edad = edad
        this.profesion = profesion
        this.domicilio = domicilio
    }

    //getters y setters
    fun getUid() : String?{
        return uid
    }

    fun setUid(uid : String){
        this.uid = uid
    }

    fun getN_Usuario() : String?{
        return nom_usuario
    }

    fun setN_Usuario(n_usuario : String){
        this.nom_usuario = n_usuario
    }

    fun getEmail() : String?{
        return email
    }

    fun setEmail(email : String){
        this.email = email
    }

    fun getTelefono() : String?{
        return telefono
    }

    fun setTelefono(telefono : String){
        this.telefono = telefono
    }


    fun getImagen() : String?{
        return imagen
    }

    fun setImagen(imagen : String){
        this.imagen = imagen
    }

    fun getBuscar() : String?{
        return buscar
    }

    fun setBuscar(buscar : String){
        this.buscar = buscar
    }

    fun getNombre() : String?{
        return nombre
    }

    fun setNombre(nombres : String){
        this.nombre = nombres
    }

    fun getApellido() : String?{
        return apellido
    }

    fun setApellido(apellidos : String){
        this.apellido = apellidos
    }

    fun getEdad() : String?{
        return edad
    }

    fun setEdad(edad : String){
        this.edad = edad
    }

    fun getProfesion() : String?{
        return profesion
    }

    fun setProfesion(profesion : String){
        this.profesion = profesion
    }



    fun getDomicilio() : String?{
        return domicilio
    }

    fun setDomicilio(domicilio : String){
        this.domicilio = domicilio
    }
}