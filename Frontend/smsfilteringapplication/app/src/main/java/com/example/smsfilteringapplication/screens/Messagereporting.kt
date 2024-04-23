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
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.smsfilteringapplication.MainActivity
import com.example.smsfilteringapplication.R
import com.example.smsfilteringapplication.dataclasses.QueryField
import com.example.smsfilteringapplication.dataclasses.removeItem
import com.example.smsfilteringapplication.dataclasses.stringItemQueryToArrayList
import com.example.smsfilteringapplication.services.blacklistAdapter
import com.example.smsfilteringapplication.services.smsviewadapter
import kotlinx.coroutines.launch

public class Messagereporting : AppCompatActivity() {
    val sms_id_list = arrayListOf<String>()
    private val fromlist = arrayListOf<String>()
    val bodylist = arrayListOf<String>()
    private val smsbodyList = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.messagereporting)

        val listView = findViewById<ListView>(R.id.message_reporting_listview)
        readSms()
        listView.adapter= smsviewadapter(this,fromlist,smsbodyList)

        val mainmenubutton = findViewById<Button>(R.id.message_reporting_mainmenubtn) // navigation button to main menu
        mainmenubutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val addItemButton = findViewById<Button>(R.id.message_reporting_additembtn)
        addItemButton.setOnClickListener {
            smsbodyList.clear()
            fromlist.clear()
            readSms()
            listView.adapter= smsviewadapter(this,fromlist,smsbodyList)
        }

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
        val whitelistnumbers = stringItemQueryToArrayList("Whitelist", QueryField.CONTENT)


            // Set the message and title for the dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Flag Message")
            builder.setMessage("This will delete the message and add the senders number to the blacklist")

            // Add a Confirm button and its logic
            builder.setPositiveButton("Confirm") { dialog, which ->
                // Perform actions after confirmation here
                //insert logic to add the item to the sms flagged database
                // the app will then delete the sms from the phone as it has been flagged as spam
                addNumberToBlockedList(fromlist.get(position))
                lifecycleScope.launch {
                    if((fromlist.get(position) in whitelistnumbers)) {
                        removeItem(fromlist.get(position), "Whitelist")}
                        deleteSmsById(this@Messagereporting, sms_id_list.get(position).toLong())
                        smsbodyList.clear()
                        fromlist.clear()
                        sms_id_list.clear()
                        readSms()
                        listView.adapter =
                            smsviewadapter(this@Messagereporting, fromlist, smsbodyList)

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

    fun deleteSmsById(context: Context, smsId: Long) {
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
                val smsidlocal = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                smsbodyList.add(msgData)
                fromlist.add(senderData)
                sms_id_list.add(smsidlocal)

            } while (cursor.moveToNext())
        } else {
            // empty inbox
        }
        cursor?.close()
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
