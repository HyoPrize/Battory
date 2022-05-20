package com.example.battory_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val customButton: Button = findViewById(R.id.custom_button)
        customButton.setOnClickListener { clickBtn() }
    }

    private fun clickBtn() {
        Toast.makeText(this, "button clicked",
            Toast.LENGTH_SHORT).show()
    }
}