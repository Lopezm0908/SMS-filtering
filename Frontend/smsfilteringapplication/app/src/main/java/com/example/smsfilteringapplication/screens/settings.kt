package com.example.smsfilteringapplication.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.example.smsfilteringapplication.MainActivity
import com.example.smsfilteringapplication.R
import com.example.smsfilteringapplication.dataclasses.GlobalValues

class settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        val mainmenubutton = findViewById<Button>(R.id.mainmenubtn) // navigation button to main menu
        mainmenubutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val useeval: CheckBox = findViewById(R.id.evalcheck)
        val uselogic: CheckBox = findViewById(R.id.logic)
        val usebert: CheckBox = findViewById(R.id.bertcheck)
        val usekey: CheckBox = findViewById(R.id.keycheck)
        // Add more checkboxes as needed...

        // Initialize checkboxes with their saved states
        useeval.isChecked = GlobalValues.getCheckboxState(this, "evalcheck")
        uselogic.isChecked = GlobalValues.getCheckboxState(this, "logic")
        usebert.isChecked = GlobalValues.getCheckboxState(this, "bertcheck")
        usekey.isChecked = GlobalValues.getCheckboxState(this, "keycheck")
        // Retrieve states for more checkboxes and initialize them...

        // Set listeners for checkboxes to save their states
        useeval.setOnCheckedChangeListener { _, isChecked ->
            GlobalValues.saveCheckboxState(this, "evalcheck", isChecked)
        }
        uselogic.setOnCheckedChangeListener { _, isChecked ->
            GlobalValues.saveCheckboxState(this, "logic", isChecked)
        }
        usebert.setOnCheckedChangeListener { _, isChecked ->
            GlobalValues.saveCheckboxState(this, "bertcheck", isChecked)
        }
        usekey.setOnCheckedChangeListener { _, isChecked ->
            GlobalValues.saveCheckboxState(this, "keycheck", isChecked)
        }
    }
}


