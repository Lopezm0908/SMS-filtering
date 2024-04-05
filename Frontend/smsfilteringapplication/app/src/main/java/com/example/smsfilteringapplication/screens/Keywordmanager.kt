package com.example.smsfilteringapplication.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.smsfilteringapplication.MainActivity
import com.example.smsfilteringapplication.R
import com.example.smsfilteringapplication.services.blacklistAdapter
import com.example.smsfilteringapplication.dataclasses.DatabaseDriver
import com.example.smsfilteringapplication.dataclasses.addNumber
import com.example.smsfilteringapplication.dataclasses.realmQueryToArrayList
import com.example.smsfilteringapplication.dataclasses.removeNumber
import kotlinx.coroutines.launch


class KeywordManager : AppCompatActivity() {
    private var keyWordList = arrayListOf<String>()
    private val type = "KeyWord"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.keywordmanager)
        keyWordList = realmQueryToArrayList(type)
        val listView = findViewById<ListView>(R.id.keyword_listview)
        listView.adapter= blacklistAdapter(this,keyWordList)

        val mainmenubutton = findViewById<Button>(R.id.keyword_mainmenubtn) // navigation button to main menu
        mainmenubutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val addItemButton = findViewById<Button>(R.id.keyword_additembtn)
        addItemButton.setOnClickListener {

            //add item dialogue
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Add Keyword")

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
                        keyWordList = realmQueryToArrayList(type)
                        listView.adapter = blacklistAdapter(this@KeywordManager, keyWordList)
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
            val textContent = findViewById<TextView>(R.id.item_phone_number).text.toString()

            // Set the message and title for the dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Remove Keyword")
            builder.setMessage("Do you want to complete this action?")

            // Add a Confirm button and its logic
            builder.setPositiveButton("Confirm") { dialog, which ->
                // Perform actions after confirmation here
                val numToRemove = keyWordList[position]
                lifecycleScope.launch {
                    removeNumber(numToRemove, type)
                    keyWordList = realmQueryToArrayList(type)
                    listView.adapter = blacklistAdapter(this@KeywordManager, keyWordList)
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


    }
