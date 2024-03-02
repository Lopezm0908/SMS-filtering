package com.example.newproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val BLbutton = findViewById<Button>(R.id.BLbutton)
        BLbutton.setOnClickListener {
            val intent = Intent(this, Activity2::class.java)
            startActivity(intent)
        }

        val  MSbutton = findViewById<Button>(R.id.MSbutton)
        MSbutton.setOnClickListener {
            val intent = Intent(this, Activity3::class.java)
            startActivity(intent)
        }

        val  WLbutton = findViewById<Button>(R.id.WLbutton)
        WLbutton.setOnClickListener {
            val intent = Intent(this, Activity4::class.java)
            startActivity(intent)
        }

        val  KWbutton = findViewById<Button>(R.id.KWbutton)
        KWbutton.setOnClickListener {
            val intent = Intent(this, Activity5::class.java)
            startActivity(intent)
        }
    }
}