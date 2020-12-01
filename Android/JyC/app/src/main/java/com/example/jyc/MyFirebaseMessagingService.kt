package com.example.jyc

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.android.HandlerDispatcher


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.e(TAG, "Message data payload: ${remoteMessage.data}")

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob()
                Looper.prepare()

                Handler().post{
                    var title = remoteMessage.data.get("title")
                    var body = remoteMessage.data.get("body")
                    var idPublicacion = remoteMessage.data.get("idPublicacion")
                    Toast.makeText(this, "Message Notification Body: ${remoteMessage.data}", Toast.LENGTH_LONG).show()
                }

                Looper.loop()

            } else {
                // Handle message within 10 seconds
//                handleNow()
            }
        }
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.e(TAG, "Message Notification Body: ${it.body}")
        }
    }

//    fun showAler(title: String?, message: String?) {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle(title)
//        builder.setMessage(message)
//        builder.setPositiveButton("Aceptar", null)
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//    }

 //    private fun showAler(title: String?, message: String?) {
//        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
//        builder.setTitle(title)
//        builder.setMessage(message)
//
//        //Adding positive button
//        builder.setPositiveButton("Aceptar", object : DialogInterface.OnClickListener {
//            override fun onClick(dialog: DialogInterface, id: Int) {
//                Log.e(TAG, "OK button was clicked")
//            }
//        })
//
//        //Adding negative button
//        builder.setNegativeButton("Cancelar", object : DialogInterface.OnClickListener {
//            override fun onClick(dialog: DialogInterface, id: Int) {
//                Log.e(TAG, "Cancel button was clicked")
//            }
//        })
//
//        //Adding neutral button
//        builder.setNeutralButton("Remind Me later", object : DialogInterface.OnClickListener {
//            override fun onClick(dialog: DialogInterface, id: Int) {
//                Log.e(TAG, "Remind me later button was clicked")
//            }
//        })
//
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//    }


}