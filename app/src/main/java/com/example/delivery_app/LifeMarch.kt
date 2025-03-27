package com.example.delivery_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LifeMarch : AppCompatActivity() {
    private val addButtons = listOf(R.id.addButton1, R.id.addButton2, R.id.addButton3, R.id.addButton4, R.id.addButton5, R.id.addButton6)
    private val quantityContainers = listOf(R.id.quantityContainer1, R.id.quantityContainer2, R.id.quantityContainer3, R.id.quantityContainer4, R.id.quantityContainer5, R.id.quantityContainer6)
    private val minusButtons = listOf(R.id.minusButton1, R.id.minusButton2, R.id.minusButton3, R.id.minusButton4, R.id.minusButton5, R.id.minusButton6)
    private val quantityTexts = listOf(R.id.quantityText1, R.id.quantityText2, R.id.quantityText3, R.id.quantityText4, R.id.quantityText5, R.id.quantityText6)
    private val plusButtons = listOf(R.id.plusButton1, R.id.plusButton2, R.id.plusButton3, R.id.plusButton4, R.id.plusButton5, R.id.plusButton6)
    private val prices = listOf(200.00, 150.00, 60.00, 59.00, 300.00, 90.00)
    private val quantities = MutableList(addButtons.size) { 0 }
    private lateinit var buyButton: Button
    private lateinit var backButton: ImageView

    private val foodImageResources = listOf(
        R.drawable.sushi, R.drawable.ris, R.drawable.coffee,
        R.drawable.humus, R.drawable.onigiri, R.drawable.lavanda
    )

    private val foodNames = listOf(
        "Суши", "Рис с овощами", "Кофе",
        "Хумус", "Онигири", "Лавандовый тоник"
    )

    private val descriptions = listOf(
        "Суши - это традиционное японское блюдо, состоящее из риса и рыбы.",
        "Рис с овощами - полезное и вкусное блюдо.",
        "Кофе - ароматный напиток для бодрости.",
        "Хумус - паста из нута, популярная на Ближнем Востоке.",
        "Онигири - японские рисовые шарики с начинкой.",
        "Лавандовый тоник - освежающий напиток с ароматом лаванды."
    )

    private val reviews = listOf(
        "Отзывы о суши: очень вкусно!",
        "Отзывы о рисе с овощами: отличный выбор.",
        "Отзывы о кофе: лучший кофе в городе.",
        "Отзывы о хумусе: аутентичный вкус.",
        "Отзывы о онигири: удобно и вкусно.",
        "Отзывы о лавандовом тонике: необычно и приятно."
    )

    private val foodImageIds = listOf(
        R.id.foodImage1, R.id.foodImage2, R.id.foodImage3,
        R.id.foodImage4, R.id.foodImage5, R.id.foodImage6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_march)
        buyButton = findViewById(R.id.buy_button)
        backButton = findViewById(R.id.back_button)

        val addButtonViews = addButtons.mapNotNull { findViewById<Button>(it) }
        val quantityContainerViews = quantityContainers.mapNotNull { findViewById<LinearLayout>(it) }
        val minusButtonViews = minusButtons.mapNotNull { findViewById<ImageView>(it) }
        val quantityTextViews = quantityTexts.mapNotNull { findViewById<TextView>(it) }
        val plusButtonViews = plusButtons.mapNotNull { findViewById<ImageView>(it) }

        if (savedInstanceState != null) {
            val savedQuantities = savedInstanceState.getIntegerArrayList("quantities")
            if (savedQuantities != null) {
                for (i in quantities.indices) {
                    quantities[i] = savedQuantities[i]
                    val addButtonVisible = savedInstanceState.getBoolean("addButtonVisibility$i", true)
                    addButtonViews[i].visibility = if (addButtonVisible) View.VISIBLE else View.GONE
                    quantityContainerViews[i].visibility = if (addButtonVisible) View.GONE else View.VISIBLE
                    quantityTextViews[i].text = quantities[i].toString()
                }
                updateTotalSum()
            }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, ListRestaurant::class.java)
            startActivity(intent)
        }

        buyButton.setOnClickListener {
            val selectedItems = mutableListOf<Bundle>()
            for (i in quantities.indices) {
                if (quantities[i] > 0) {
                    val itemBundle = Bundle().apply {
                        putInt("image", foodImageResources[i])
                        putString("name", foodNames[i])
                        putDouble("price", prices[i])
                        putInt("quantity", quantities[i])
                    }
                    selectedItems.add(itemBundle)
                }
            }
            val intent = Intent(this, SaleFood::class.java).apply {
                putParcelableArrayListExtra("selected_items", ArrayList(selectedItems))
            }
            startActivity(intent)
        }

        for (i in addButtonViews.indices) {
            if (i < quantityContainerViews.size && i < minusButtonViews.size && i < quantityTextViews.size && i < plusButtonViews.size) {
                setupAddButton(addButtonViews[i], quantityContainerViews[i], quantityTextViews[i], i)
                setupPlusButton(plusButtonViews[i], quantityTextViews[i], i)
                setupMinusButton(minusButtonViews[i], quantityTextViews[i], quantityContainerViews[i], addButtonViews[i], i)
            } else {
                android.util.Log.e("LifeMarch", "Mismatch at index $i")
            }
        }

        val foodImageViews = foodImageIds.mapNotNull { findViewById<ImageView>(it) }
        for (i in foodImageViews.indices) {
            foodImageViews[i].setOnClickListener {
                openDishDetails(foodImageResources[i], prices[i].toString(), descriptions[i], reviews[i])
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntegerArrayList("quantities", ArrayList(quantities))
        val addButtonViews = addButtons.mapNotNull { findViewById<Button>(it) }
        for (i in addButtonViews.indices) {
            outState.putBoolean("addButtonVisibility$i", addButtonViews[i].visibility == View.VISIBLE)
        }
    }

    private fun setupAddButton(addButton: Button, quantityContainer: LinearLayout, quantityText: TextView, index: Int) {
        addButton.setOnClickListener {
            addButton.visibility = View.GONE
            quantityContainer.visibility = View.VISIBLE
            quantities[index] = 1
            quantityText.text = quantities[index].toString()
            updateTotalSum()
        }
    }

    private fun setupPlusButton(plusButton: ImageView, quantityText: TextView, index: Int) {
        plusButton.setOnClickListener {
            quantities[index]++
            quantityText.text = quantities[index].toString()
            updateTotalSum()
        }
    }

    private fun setupMinusButton(minusButton: ImageView, quantityText: TextView, quantityContainer: LinearLayout, addButton: Button, index: Int) {
        minusButton.setOnClickListener {
            if (quantities[index] > 0) {
                quantities[index]--
                quantityText.text = quantities[index].toString()
                if (quantities[index] <= 0) {
                    quantityContainer.visibility = View.GONE
                    addButton.visibility = View.VISIBLE
                }
                updateTotalSum()
            }
        }
    }

    private fun updateTotalSum() {
        var totalSum = 0.0
        for (i in quantities.indices) {
            totalSum += quantities[i] * prices[i]
        }
        if (totalSum > 0) {
            buyButton.text = "Купить (${String.format("%.2f", totalSum)} р)"
            buyButton.visibility = View.VISIBLE
        } else {
            buyButton.visibility = View.GONE
        }
    }

    private fun openDishDetails(imageRes: Int, cost: String, description: String, reviews: String) {
        val intent = Intent(this, ItemDescriptions::class.java)
        intent.putExtra("dishImage", imageRes)
        intent.putExtra("dishCost", cost)
        intent.putExtra("dishDescription", description)
        intent.putExtra("dishReviews", reviews)
        startActivity(intent)
    }
}