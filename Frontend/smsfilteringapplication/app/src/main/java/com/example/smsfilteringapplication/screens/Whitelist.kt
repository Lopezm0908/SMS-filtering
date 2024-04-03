package com.example.smsfilteringapplication.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smsfilteringapplication.MainActivity
import com.example.smsfilteringapplication.R
import com.example.smsfilteringapplication.dataclasses.StringItem
import com.example.smsfilteringapplication.services.blacklistAdapter
import com.example.smsfilteringapplication.MyApp
import com.example.smsfilteringapplication.dataclasses.DatabaseDriver
import com.example.smsfilteringapplication.dataclasses.addNumber
import com.example.smsfilteringapplication.dataclasses.realmQueryToArrayList
import com.example.smsfilteringapplication.dataclasses.removeNumber
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

public class Whitelist : AppCompatActivity() {
    private var arrayListOfNumbers = arrayListOf<String>()
    private val type = "Whitelist"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.whitelist)
        arrayListOfNumbers = realmQueryToArrayList(type)


        val listView = findViewById<ListView>(R.id.whitelist_listview)
        listView.adapter= blacklistAdapter(this, arrayListOfNumbers)

        val mainmenubutton = findViewById<Button>(R.id.whitlist_mainmenubtn) // navigation button to main menu
        mainmenubutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val addItemButton = findViewById<Button>(R.id.whitlist_additembtn)
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
                if (newItem.isNotEmpty()) {
                    //if conditions are met the item is added to the back end blacklist and the list view is updated
                    lifecycleScope.launch {
                        addNumber(newItem, type)
                        arrayListOfNumbers = realmQueryToArrayList(type)
                        listView.adapter = blacklistAdapter(this@Whitelist, arrayListOfNumbers)
                    }
                } else {
                    Toast.makeText(this, "Item cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            builder.show()
        }
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            // Reading the text content of the clicked TextView
            //val textContent = findViewById<TextView>(R.id.item_phone_number).text.toString()

            // Set the message and title for the dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirm Action")
            builder.setMessage("Do you want to complete this action?")

            // Add a Confirm button and its logic
            builder.setPositiveButton("Confirm") { dialog, which ->
                // Perform actions after confirmation here
                val numToRemove = arrayListOfNumbers[position]
                lifecycleScope.launch {
                    removeNumber(numToRemove)
                    arrayListOfNumbers = realmQueryToArrayList(type)
                    listView.adapter = blacklistAdapter(this@Whitelist, arrayListOfNumbers)
                }
                //arrayListOfNumbers.removeAt(position)
                //listView.adapter= blacklistAdapter(this,arrayListOfNumbers)
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
}