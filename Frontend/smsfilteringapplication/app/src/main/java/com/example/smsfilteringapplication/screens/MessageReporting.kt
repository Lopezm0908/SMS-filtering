package com.example.smsfilteringapplication.screens

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.BlockedNumberContract
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.smsfilteringapplication.MainActivity
import com.example.smsfilteringapplication.R
import com.example.smsfilteringapplication.dataclasses.QueryField
import com.example.smsfilteringapplication.dataclasses.removeItem
import com.example.smsfilteringapplication.dataclasses.stringItemQueryToArrayList
import com.example.smsfilteringapplication.services.SmsViewAdapter
import kotlinx.coroutines.launch

class MessageReporting : AppCompatActivity() {
    private val smsIdList = arrayListOf<String>()
    private val fromList = arrayListOf<String>()
    private val smsBodyList = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.messagereporting)

        val listView = findViewById<ListView>(R.id.message_reporting_listview)
        readSms()
        listView.adapter= SmsViewAdapter(this,fromList,smsBodyList)

        val mainMenuButton = findViewById<Button>(R.id.message_reporting_mainmenubtn) // navigation button to main menu
        mainMenuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val addItemButton = findViewById<Button>(R.id.message_reporting_additembtn)
        addItemButton.setOnClickListener {
            smsBodyList.clear()
            fromList.clear()
            readSms()
            listView.adapter= SmsViewAdapter(this,fromList,smsBodyList)
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        val whitelistNumbers = stringItemQueryToArrayList("Whitelist", QueryField.CONTENT)


            // Set the message and title for the dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Flag Message")
            builder.setMessage("This will delete the message and add the senders number to the blacklist")

            // Add a Confirm button and its logic
            builder.setPositiveButton("Confirm") { dialog, which ->
                // Perform actions after confirmation here
                //insert logic to add the item to the sms flagged database
                // the app will then delete the sms from the phone as it has been flagged as spam
                addNumberToBlockedList(fromList.get(position))
                lifecycleScope.launch {
                    if((fromList.get(position) in whitelistNumbers)) {
                        removeItem(fromList.get(position), "Whitelist")}
                        deleteSmsById(this@MessageReporting, smsIdList.get(position).toLong())
                        smsBodyList.clear()
                        fromList.clear()
                        smsIdList.clear()
                        readSms()
                        listView.adapter =
                            SmsViewAdapter(this@MessageReporting, fromList, smsBodyList)

                }

            }



            // Add a Cancel button and its logic
            builder.setNegativeButton("Cancel") { dialog, which ->

            }

            // set the dialog to not close when the user touches outside of it
            builder.setCancelable(false)

            // Create and show the AlertDialog
            val dialog = builder.create()
            dialog.show()



        }
    }

    private fun deleteSmsById(context: Context, smsId: Long) {
        val contentResolver: ContentResolver = context.contentResolver
        val uri = Uri.parse("content://sms/")

        try {
            // Attempt to delete the SMS
            val rowsDeleted = contentResolver.delete(Uri.withAppendedPath(uri, "$smsId"), null, null)

            if (rowsDeleted > 0) {
                println("SMS with ID $smsId deleted successfully.")
            } else {
                println("No SMS found with ID $smsId.")
            }
        } catch (e: Exception) {
            println("Error deleting SMS: ${e.message}")
        }
    }

    private fun readSms() {
        val cursor: Cursor? = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null)
        if (cursor?.moveToFirst() == true) { // must check the result to prevent exception
            do {
                val msgData = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                val senderData = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                val smsIdLocal = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                smsBodyList.add(msgData)
                fromList.add(senderData)
                smsIdList.add(smsIdLocal)

            } while (cursor.moveToNext())
        } else {
            // empty inbox
        }
        cursor?.close()
    }
    private fun addNumberToBlockedList(number: String) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            val values = ContentValues()
            values.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
            contentResolver.insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, values)
        } else {
            // Handle the lack of permission here.
        }
    }
}
