package com.example.jyc

import com.google.firebase.firestore.Exclude
import java.util.*
import kotlin.collections.ArrayList

class Post(
        var idUsuario: String? = null,
        var post: String? = null,
        var categoria: String? = null,
        var date: String? = null,
        var userName: String? = null,
        var image: String? = null,
        var cantidadDeLikes: Int?,
        var likes: ArrayList<String>? = arrayListOf(),
        var nombreEvento: String? = null,
        var cantidadDeComentarios: Int?,
        var listaIdcomentarios: ArrayList<String>? = arrayListOf()
) {
    @Exclude
    @set:Exclude
    @get:Exclude
    var uid: String? = null

    constructor() :
            this(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            )
}