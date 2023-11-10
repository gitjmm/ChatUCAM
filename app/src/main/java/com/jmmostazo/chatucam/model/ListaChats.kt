package com.jmmostazo.chatucam.model

//Esta clase se va a corresponder con la base de datos de firebase ListaMensajes
class ListaChats {

    private var uid : String = ""

    constructor()

    constructor(uid: String) {
        this.uid = uid
    }

    fun getUid() : String?{
        return uid
    }

    fun setUid (uid: String?){
        this.uid = uid!!
    }
}