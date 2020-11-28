package com.example.jyc

import com.google.firebase.firestore.Exclude
import java.util.*

class Comment(
        var idUsuario: String? = null,
        var userName: String? = null,
        var date: String? = null,
        var texto: String? =  null
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
                    null
            )
}