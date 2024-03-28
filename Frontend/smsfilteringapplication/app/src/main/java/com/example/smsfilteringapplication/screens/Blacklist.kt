package com.example.smsfilteringapplication.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import com.example.smsfilteringapplication.MainActivity
import com.example.smsfilteringapplication.R
import com.example.smsfilteringapplication.services.blacklistAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.provider.BlockedNumberContract
import android.database.Cursor
public class Blacklist : AppCompatActivity() {

    val numberlist = arrayListOf<String>("thing one")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.blacklist)
        val listView = findViewById<ListView>(R.id.blacklist_listview)
        listView.adapter= blacklistAdapter(this,getBlockedNumbers(this)) //custom list adapter telling list what to render.

        val mainmenubutton = findViewById<Button>(R.id.mainmenubtn) // navigation button to main menu
        mainmenubutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun getBlockedNumbers(context: Context): ArrayList<String> {
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
