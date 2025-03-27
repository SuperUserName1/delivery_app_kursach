package com.example.delivery_app

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ListRestaurant : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_restaurant)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val restaurant1 = findViewById<LinearLayout>(R.id.item1)
        val restaurant2 = findViewById<LinearLayout>(R.id.item2)
        val restaurant3 = findViewById<LinearLayout>(R.id.item3)
        val restaurant4 = findViewById<LinearLayout>(R.id.item4)

        restaurant1.setOnClickListener {
            val intent = Intent(this, LifeMarch::class.java)
            startActivity(intent)
        }
        restaurant2.setOnClickListener {
            val intent = Intent(this, StorePiterochka::class.java)
            startActivity(intent)
        }
        restaurant3.setOnClickListener {
            val intent = Intent(this, DodoPizza::class.java)
            startActivity(intent)
        }
        restaurant4.setOnClickListener {
            val intent = Intent(this, StoreRestaurant::class.java)
            startActivity(intent)
        }

    }
}