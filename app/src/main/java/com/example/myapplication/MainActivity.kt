package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val questionbutton = findViewById<Button>(R.id.question)
        questionbutton.setOnClickListener{
            val Intent = Intent(this,question::class.java)
            startActivity(Intent)
        }
        val playbutton = findViewById<Button>(R.id.play)
        playbutton.setOnClickListener {
            val Intent = Intent(this,play::class.java)
            startActivity(Intent)
        }


    }

}