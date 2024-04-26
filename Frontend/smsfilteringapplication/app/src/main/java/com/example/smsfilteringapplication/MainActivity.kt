package com.example.smsfilteringapplication

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
//import com.chaquo.python.PyObject
//import com.chaquo.python.Python
import com.example.smsfilteringapplication.services.SmsReceiver
import com.example.smsfilteringapplication.services.MainMenuAdapter


class MainActivity : AppCompatActivity() {
    lateinit var receiver: SmsReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainredesign)
        val listView = findViewById<ListView>(R.id.main_menu_view)
        listView.adapter= MainMenuAdapter(this) //custom list adapter telling list what to render.


        //define and register receiver
        receiver = SmsReceiver()
        registerReceiver(receiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))

        checkAndRequestDefaultSmsApp(this)

        //checks if permissions already exist if they do not it asks for them
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.RECEIVE_MMS, android.Manifest.permission.WRITE_CONTACTS,android.Manifest.permission.RECEIVE_WAP_PUSH,android.Manifest.permission.READ_SMS,android.Manifest.permission.RECEIVE_SMS,android.Manifest.permission.SEND_SMS,android.Manifest.permission.SEND_SMS),111)
        }
        else{}

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

        private fun checkAndRequestDefaultSmsApp(activity: Activity) {
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
        private fun checkAndRequestRuntimePermission(activity: Activity) {
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

}


