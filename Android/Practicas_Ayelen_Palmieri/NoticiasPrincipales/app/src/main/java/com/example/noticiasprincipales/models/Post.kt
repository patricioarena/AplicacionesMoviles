package com.example.noticiasprincipales.models

import android.provider.ContactsContract
import java.util.*
import kotlin.collections.ArrayList

class Post(val post: String? = null, val photo: String? = null, val date: Date? = null, val username:String? = null, val likes: ArrayList<String>? = arrayListOf()){
    @Exclude
    @set:Exclude
    @get:Exclude
    var uid: String? = null

    constructor() : this (null, null, null, null, null)
}