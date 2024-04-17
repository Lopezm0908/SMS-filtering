package com.example.smsfilteringapplication

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Button
import android.widget.ListView
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
import com.example.smsfilteringapplication.services.blacklistAdapter
import com.example.smsfilteringapplication.services.mainmenuadapter
import java.io.DataOutputStream


class MainActivity : AppCompatActivity() {
    lateinit var receiver: SmsReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainredesign)
        val listView = findViewById<ListView>(R.id.main_menu_view)
        listView.adapter= mainmenuadapter(this) //custom list adapter telling list what to render.
        listView.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(
                applicationContext,
                "permissions granted",
                Toast.LENGTH_LONG
            ).show()
            snapToListItem(listView, position)
        }
        //val suProcess = Runtime.getRuntime().exec("su")

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
             fun snapToListItem(listView: ListView, position: Int) {
                 listView.post {
                     val itemHeight = listView.getChildAt(0).height
                     val screenHeight = resources.displayMetrics.heightPixels
                     val scrollY = position * itemHeight - screenHeight / 2 + itemHeight / 2
                     listView.smoothScrollToPositionFromTop(position, scrollY)
                 }
}}


