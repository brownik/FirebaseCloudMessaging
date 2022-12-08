package com.brownik.firebasecloudmessaging

import com.google.firebase.database.FirebaseDatabase

object CommunicationService {

    private var rootInstance = FirebaseDatabase.getInstance()

    fun sendToken(token: String?) {
        val path = rootInstance.reference.child("id")
        path.setValue(token)
    }
}