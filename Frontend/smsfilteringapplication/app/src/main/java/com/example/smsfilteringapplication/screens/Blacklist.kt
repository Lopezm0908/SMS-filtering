package com.example.smsfilteringapplication.screens

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.smsfilteringapplication.MainActivity
import com.example.smsfilteringapplication.R
import com.example.smsfilteringapplication.services.blacklistAdapter
import android.provider.BlockedNumberContract
import android.database.Cursor
import android.widget.AdapterView
import androidx.core.content.ContextCompat



public class Blacklist : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blacklist)
        val listView = findViewById<ListView>(R.id.blacklist_listview)
        listView.adapter= blacklistAdapter(this,getBlockedNumbers(this)) //custom list adapter telling list what to render.

        val mainMenuButton = findViewById<Button>(R.id.mainmenubtn) // navigation button to main menu
        mainMenuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val addItemButton = findViewById<Button>(R.id.additembtn)
        addItemButton.setOnClickListener {

            //add item dialogue
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add Item")
            val inflater = LayoutInflater.from(this)
            val dialogLayout = inflater.inflate(R.layout.dialogue_add_item, null)

            val editText: EditText = dialogLayout.findViewById(R.id.editTextItem)
            builder.setView(dialogLayout)

            builder.setPositiveButton("Add") { _, _ ->
                val newItem = editText.text.toString().trim()
                if (newItem.isNotEmpty() && newItem.matches(Regex("^[0-9]+$"))) {
                    //if conditions are met the item is added to the back end blacklist and the list view is updated
                    addNumberToBlockedList(newItem)
                    listView.adapter= blacklistAdapter(this,getBlockedNumbers(this))
                } else {
                    Toast.makeText(this, "Item cannot be empty and must be numbers only ", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
            //
            listView.adapter= blacklistAdapter(this,getBlockedNumbers(this))
        }
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            // Reading the text content of the clicked TextView
            val textContent = findViewById<TextView>(R.id.item_phone_number).text.toString()

            // Set the message and title for the dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Remove Number")
            builder.setMessage("Do you want to complete this action?")

            // Add a Confirm button and its logic
            builder.setPositiveButton("Confirm") { dialog, which ->
                // Perform actions after confirmation here

                removeBlockedNumber(this,textContent)
                listView.adapter= blacklistAdapter(this,getBlockedNumbers(this))
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
    private fun addNumberToBlockedList(number: String) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            val values = ContentValues()
            values.put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
            contentResolver.insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, values)
        } else {
            // Handle the lack of permission here.
        }
    }

    private fun removeBlockedNumber(context: Context, phoneNumber: String): Boolean {
        val contentResolver: ContentResolver = context.contentResolver

        // Build the selection clause to find the blocked number
        val selection = "${BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER} = ?"
        val selectionArgs = arrayOf(phoneNumber)

        // Attempt to delete the number from the blocked list
        val rowsDeleted = contentResolver.delete(
            BlockedNumberContract.BlockedNumbers.CONTENT_URI,
            selection,
            selectionArgs
        )

        // If rowsDeleted is more than 0, the operation was successful
        return rowsDeleted > 0
    }
    private fun getBlockedNumbers(context: Context): ArrayList<String> {
        val blockedNumbersList = ArrayList<String>()

        // Requires permission: Manifest.permission.READ_BLOCKED_NUMBERS
        val cursor: Cursor? = context.contentResolver.query(
            BlockedNumberContract.BlockedNumbers.CONTENT_URI,
            arrayOf(BlockedNumberContract.BlockedNumbers.COLUMN_ID, BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER),
            null,
            null,
            null
        )

        cursor?.use {
            val numberIndex = it.getColumnIndex(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER)
            while (it.moveToNext()) {
                val number = it.getString(numberIndex)
                blockedNumbersList.add(number)
            }
        }
        return blockedNumbersList
    }

}
