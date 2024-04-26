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
        val mainMenuButton = findViewById<Button>(R.id.mainmenubtn) // navigation button to main menu
        mainMenuButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val useEval: CheckBox = findViewById(R.id.evalcheck)
        val useLogic: CheckBox = findViewById(R.id.logic)
        val useBert: CheckBox = findViewById(R.id.bertcheck)
        val useKey: CheckBox = findViewById(R.id.keycheck)
        // Add more checkboxes as needed...

        // Initialize checkboxes with their saved states
        useEval.isChecked = GlobalValues.getCheckboxState(this, "evalcheck")
        useLogic.isChecked = GlobalValues.getCheckboxState(this, "logic")
        useBert.isChecked = GlobalValues.getCheckboxState(this, "bertcheck")
        useKey.isChecked = GlobalValues.getCheckboxState(this, "keycheck")
        // Retrieve states for more checkboxes and initialize them...

        // Set listeners for checkboxes to save their states
        useEval.setOnCheckedChangeListener { _, isChecked ->
            GlobalValues.saveCheckboxState(this, "evalcheck", isChecked)
        }
        useLogic.setOnCheckedChangeListener { _, isChecked ->
            GlobalValues.saveCheckboxState(this, "logic", isChecked)
        }
        useBert.setOnCheckedChangeListener { _, isChecked ->
            GlobalValues.saveCheckboxState(this, "bertcheck", isChecked)
        }
        useKey.setOnCheckedChangeListener { _, isChecked ->
            GlobalValues.saveCheckboxState(this, "keycheck", isChecked)
        }
    }
}


