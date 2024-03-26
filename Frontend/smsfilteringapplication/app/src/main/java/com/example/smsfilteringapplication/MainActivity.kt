package com.example.smsfilteringapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    lateinit var receiver: SmsReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        receiver = SmsReceiver()
        registerReceiver(receiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //checks if permissions already exist if they do not it asks for them if they do then it runs the recievemsg service to listen for msg
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.RECEIVE_SMS,android.Manifest.permission.SEND_SMS),111)
        }
        else
         //   receiveMsg()
            Toast.makeText(
                applicationContext,
                "permissions granted",
                Toast.LENGTH_LONG
            ).show()
    }
    // runs after permissions request goes through.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
          //  receiveMsg()
            Toast.makeText(
                                    applicationContext,
                                   "permissions granted",
                                    Toast.LENGTH_LONG
                                ).show()
    }
    // waits for message and if message receive it displays body in toast msg
   // private fun receiveMsg() {
  //      var br = object: BroadcastReceiver(){
    //        override fun onReceive(p0: Context?, p1: Intent?) {
    //            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT && Telephony.Sms.ADDRESS != "4099445824"){
      //              for(sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)) {
      //                  Toast.makeText(
       //                     applicationContext,
        //                    sms.displayMessageBody,
        //                    Toast.LENGTH_LONG
        //                ).show()

     //               }
     //           }

       //     }

        }
        //registration for broadcast receiver.




    fun deleteSMS(context: Context, message: String, number: String) {
        try {
          Log.d("sms blocker", "Deleting SMS from inbox")
            val uriSms = Uri.parse("content://sms/inbox")
            val c = context.contentResolver.query(
                uriSms, arrayOf(
                    "_id", "thread_id", "address",
                    "person", "date", "body"
                ), null, null, null
            )
            if (c != null && c.moveToFirst()) {
                do {
                    val id = c.getLong(0)
                    val threadId = c.getLong(1)
                    val address = c.getString(2)
                    val body = c.getString(5)
                    if (message == body && address == number) {
                        Log.d("sms blocker","Deleting SMS with id: $threadId")
                        context.contentResolver.delete(
                            Uri.parse("content://sms/$id"), null, null
                        )
                    }
                } while (c.moveToNext())
            }
        } catch (e: Exception) {
           Log.d("sms blocker","Could not delete SMS from inbox: " + e.message)
        }

    }

