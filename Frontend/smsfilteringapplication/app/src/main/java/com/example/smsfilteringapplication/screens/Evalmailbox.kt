package com.example.smsfilteringapplication.screens

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.BlockedNumberContract
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smsfilteringapplication.MainActivity
import com.example.smsfilteringapplication.R
import com.example.smsfilteringapplication.services.smsviewadapter
import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.example.smsfilteringapplication.dataclasses.QueryField
import com.example.smsfilteringapplication.dataclasses.removeItem
import com.example.smsfilteringapplication.dataclasses.stringItemQueryToArrayList
import com.example.smsfilteringapplication.services.blacklistAdapter
import kotlinx.coroutines.launch

class Evalmailbox : AppCompatActivity() {
    private val type = "Eval"
    var sms_id_list = arrayListOf<String>()
    private var fromlist = arrayListOf<String>()
    private var smsbodyList = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.eval_mailbox)

        val listView = findViewById<ListView>(R.id.eval_listview)
        //function to query database sms messages into the from list and body list must be called here @cameron
        //these are filler values
        //smsbodyList.add("this is a test message")
        smsbodyList = stringItemQueryToArrayList(type, QueryField.CONTENT)
        //fromlist.add("4096655441")
        fromlist = stringItemQueryToArrayList(type, QueryField.SENDER)
        sms_id_list = stringItemQueryToArrayList(type, QueryField.ID)

        listView.adapter= smsviewadapter(this,fromlist,smsbodyList)

        val mainmenubutton = findViewById<Button>(R.id.eval_mainmenubtn) // navigation button to main menu
        mainmenubutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val refreshButton = findViewById<Button>(R.id.eval_additembtn)
        refreshButton.setOnClickListener {
            smsbodyList.clear()
            fromlist.clear()
            //need call to function to query messages from database and refresh fromlist and bodylist
            listView.adapter= smsviewadapter(this,fromlist,smsbodyList)
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            // Set the message and title for the dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Manage Message")
            builder.setMessage("Do you want to flag the message or approve the message? click any where outside of the window to cancel.")

            // Add a Confirm button and its logic
            builder.setPositiveButton("Approve") { dialog, which ->


                // need function to remove sms from database here
                // need function to write sms into file here
                val values = ContentValues().apply {
                    put("address", fromlist.get(position))
                    put("body", smsbodyList.get(position))
                    put("read", false) // 0 for unread, 1 for read
                    put("date", System.currentTimeMillis())
                    // For API level 19 and above, use "type" -> Telephony.Sms.MESSAGE_TYPE_INBOX
                    put("type", "1") // 1 for received SMS
                }

                try {
                    this.contentResolver.insert(Uri.parse("content://sms/inbox"), values)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                //smsbodyList.clear()
                //fromlist.clear()
                //sms_id_list.clear()

                //call function to query messages from database into bodylist and from list
                smsbodyList = stringItemQueryToArrayList(type, QueryField.CONTENT)
                fromlist = stringItemQueryToArrayList(type, QueryField.SENDER)
                sms_id_list = stringItemQueryToArrayList(type, QueryField.ID)
                //listView.adapter= smsviewadapter(this,fromlist,smsbodyList)
            }

            // Add a Cancel button and its logic
            builder.setNegativeButton("Flag") { dialog, which ->
            // need function to delete message from database here placeholder below
//                fromlist.removeAt(position)
//                smsbodyList.removeAt(position)
//                sms_id_list.removeAt(position)
                val fromListToRemove = fromlist[position]
                val smsBodyListToRemove = smsbodyList[position]
                val smsIdListToRemove = sms_id_list[position]

                lifecycleScope.launch {
                    removeItem(smsBodyListToRemove, type)
                    smsbodyList = stringItemQueryToArrayList(type, QueryField.CONTENT)
                    listView.adapter = blacklistAdapter(this@Evalmailbox, smsbodyList)
                }
                //function to add number to blacklist
                addNumberToBlockedList(fromlist.get(position))
                //smsbodyList.clear()
                //fromlist.clear()
                //sms_id_list.clear()
                //requery sms
                listView.adapter= smsviewadapter(this,fromlist,smsbodyList)
            }

            // set the dialog to close when the user touches outside of it
            builder.setCancelable(true)

            // Create and show the AlertDialog
            val dialog = builder.create()
            dialog.show()



        }
    }
    fun addNumberToBlockedList(number: String) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            val values = ContentValues()
            values.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
            contentResolver.insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, values)
        } else {
            // Handle the lack of permission here.
        }
    }
}