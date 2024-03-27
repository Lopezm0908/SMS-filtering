package com.example.smsfilteringapplication.screens

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.example.smsfilteringapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

public class Blacklist : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ArrayAdapter<String>
    private val itemList = mutableListOf("Item 1", "Item 2", "Item 3")

    //@SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blacklist)
        listView = findViewById(R.id.bllistView)
        searchView = findViewById(R.id.blsearchView)

        // Set up the adapter
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemList)
        listView.adapter = adapter

       //  Add Item Button
        val addItemButton: Button = findViewById(R.id.bladdItemButton)
        addItemButton.setOnClickListener {
            showAddItemDialog()
        }

        // Search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        // Item removal functionality
        listView.setOnItemClickListener { _, _, position, _ ->
            itemList.removeAt(position)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showAddItemDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Item")

        val inflater = LayoutInflater.from(this)
        val dialogLayout = inflater.inflate(R.layout.dialogue_add_item, null)

        val editText: EditText = dialogLayout.findViewById(R.id.editTextItem)
        builder.setView(dialogLayout)

        builder.setPositiveButton("Add") { _, _ ->
            val newItem = editText.text.toString().trim()
            if (newItem.isNotEmpty()) {
                itemList.add(newItem)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "Item cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}