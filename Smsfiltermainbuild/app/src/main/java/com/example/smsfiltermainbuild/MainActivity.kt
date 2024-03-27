package com.example.smsfiltermainbuild

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val BLbutton = findViewById<Button>(R.id.blacklistbtn)
        BLbutton.setOnClickListener {
            val intent = Intent(this, blacklistCL::class.java)
            startActivity(intent)
            finish()
        }

    }
}