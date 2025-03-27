package com.example.delivery_app

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ItemDescriptions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_descriptions)

        val dishImageView: ImageView = findViewById(R.id.dishImageView)
        val dishCostTextView: TextView = findViewById(R.id.dishCostTextView)
        val dishDescriptionTextView: TextView = findViewById(R.id.dishDescriptionTextView)
        val dishReviewsTextView: TextView = findViewById(R.id.dishReviewsTextView)

        // Получаем данные из Intent extras
        val dishImageRes = intent.getIntExtra("dishImage", 0)
        val dishCost = intent.getStringExtra("dishCost")
        val dishDescription = intent.getStringExtra("dishDescription")
        val dishReviews = intent.getStringExtra("dishReviews")

        if (dishImageRes != 0) {
            dishImageView.setImageResource(dishImageRes)
        }
        dishCostTextView.text = dishCost ?: "Цена не указана"
        dishDescriptionTextView.text = dishDescription ?: "Описание отсутствует"
        dishReviewsTextView.text = dishReviews ?: "Отзывы отсутствуют"
    }
}
