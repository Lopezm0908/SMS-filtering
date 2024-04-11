package com.example.smsfilteringapplication

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smsfilteringapplication.screens.Blacklist
import com.example.smsfilteringapplication.screens.Evalmailbox
import com.example.smsfilteringapplication.screens.KeywordManager
import com.example.smsfilteringapplication.screens.Messagereporting
import com.example.smsfilteringapplication.screens.Whitelist
import com.example.smsfilteringapplication.services.SmsReceiver


class MainActivity : AppCompatActivity() {
    lateinit var receiver: SmsReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        //define and register receiver
        receiver = SmsReceiver()
        registerReceiver(receiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ask to make default sms application. popup doesnt appear for some reason if someone has a way to fix that would be cool.
        val setSmsAppIntent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
        setSmsAppIntent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, packageName)
        startActivity(setSmsAppIntent)

        //checks if permissions already exist if they do not it asks for them
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.RECEIVE_MMS, android.Manifest.permission.WRITE_CONTACTS,android.Manifest.permission.RECEIVE_WAP_PUSH,android.Manifest.permission.READ_SMS,android.Manifest.permission.RECEIVE_SMS,android.Manifest.permission.SEND_SMS,android.Manifest.permission.SEND_SMS),111)
        }
        else{}
           // Toast.makeText(
        //        applicationContext,
       //         "permissions granted",
        //        Toast.LENGTH_LONG
        //    ).show()

        //button navigation to other pages.
        val evalbtn = findViewById<Button>(R.id.eval_mailbox_button)
        evalbtn.setOnClickListener{
            val intent = Intent(this, Evalmailbox::class.java)
            startActivity(intent)
        }

        val BLbutton = findViewById<Button>(R.id.BLbutton)
        BLbutton.setOnClickListener {
            val intent = Intent(this, Blacklist::class.java)
            startActivity(intent)
        }
        val  MSbutton = findViewById<Button>(R.id.MSbutton)
        MSbutton.setOnClickListener {
            val intent = Intent(this, Messagereporting::class.java)
            startActivity(intent)
        }
        val  WLbutton = findViewById<Button>(R.id.WLbutton)
        WLbutton.setOnClickListener {
            val intent = Intent(this, Whitelist::class.java )
            startActivity(intent)
        }
        val  KWbutton = findViewById<Button>(R.id.KWbutton)
        KWbutton.setOnClickListener {
            val intent = Intent(this, KeywordManager::class.java)
            startActivity(intent)
        }
    }
    // runs after permissions request goes through to check if permissions were granted or not.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
       super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

            Toast.makeText(
                                    applicationContext,
                                   "permissions granted",
                                    Toast.LENGTH_LONG
                                ).show()
    }



    //more testing functions
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
        fun checkAndRequestDefaultSmsApp(activity: Activity) {
            if (Telephony.Sms.getDefaultSmsPackage(activity) != activity.packageName) {
                // Not default SMS app
                val intent = Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, activity.packageName)
                activity.startActivityForResult(intent, 111)
            } else {
                // Already default SMS app, proceed to check runtime permission
                checkAndRequestRuntimePermission(activity)
            }
        }
        fun checkAndRequestRuntimePermission(activity: Activity) {
            if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_SMS) !=
                PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(android.Manifest.permission.READ_SMS),
                    111
                )
            } else {
                // Permission already granted, proceed with reading SMS or blocked numbers
            }
        }

        // Handle the permission result
      //  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
     //       super.onRequestPermissionsResult(requestCode, permissions, grantResults)
     //       if (requestCode == 111) {
     //           if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with the action
     //           } else {
                    // Permission denied, handle the error
      //          }
    //        }
  //      }
        }


// ignore this
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
