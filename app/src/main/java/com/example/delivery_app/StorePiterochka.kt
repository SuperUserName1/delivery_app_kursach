package com.example.delivery_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StorePiterochka : AppCompatActivity() {
    private val addButtons = listOf(R.id.addButton1, R.id.addButton2, R.id.addButton3, R.id.addButton4, R.id.addButton5, R.id.addButton6)
    private val quantityContainers = listOf(R.id.quantityContainer1, R.id.quantityContainer2, R.id.quantityContainer3, R.id.quantityContainer4, R.id.quantityContainer5, R.id.quantityContainer6)
    private val minusButtons = listOf(R.id.minusButton1, R.id.minusButton2, R.id.minusButton3, R.id.minusButton4, R.id.minusButton5, R.id.minusButton6)
    private val quantityTexts = listOf(R.id.quantityText1, R.id.quantityText2, R.id.quantityText3, R.id.quantityText4, R.id.quantityText5, R.id.quantityText6)
    private val plusButtons = listOf(R.id.plusButton1, R.id.plusButton2, R.id.plusButton3, R.id.plusButton4, R.id.plusButton5, R.id.plusButton6)
    private val prices = listOf(180.00, 39.00, 50.00, 59.00, 30.00, 90.00)
    private val quantities = MutableList(addButtons.size) { 0 }
    private lateinit var buyButton: Button
    private lateinit var backButton: ImageView

    private val foodImageResources = listOf(
        R.drawable.chiken, R.drawable.bread, R.drawable.icecream_1,
        R.drawable.horns, R.drawable.samsa, R.drawable.tea
    )

    private val foodNames = listOf(
        "Грудка", "Хлеб", "Мороженное",
        "Рожки", "Самса с курицей", "Черный чай"
    )

    private val descriptions = listOf(
        "Нежная и сочная куриная грудка, идеальная для приготовления различных блюд.",  // Грудка
        "Свежий и ароматный хлеб с хрустящей корочкой и мягкой серединой.",  // Хлеб
        "Кремовое мороженое с разнообразием вкусов, от классических до экзотических.",  // Мороженное
        "Макаронные изделия в форме рожков, отлично сочетаются с любыми соусами.",  // Рожки
        "Хрустящая выпечка с сочной куриной начинкой, приправленной специями.",  // Самса с курицей
        "Крепкий и ароматный напиток, который бодрит и согревает."  // Черный чай
    )

    private val reviews = listOf(
        "Грудка была очень свежей и вкусной, идеально подошла для салата.",  // Грудка
        "Хлеб был просто великолепен — хрустящий снаружи и мягкий внутри.",  // Хлеб
        "Мороженое было невероятно вкусным, особенно понравился ванильный вкус.",  // Мороженное
        "Рожки отлично держат форму, паста получилась очень аппетитной.",  // Рожки
        "Самса была горячей и ароматной, начинка просто тает во рту.",  // Самса с курицей
        "Чай был крепким и бодрящим, идеальный для утреннего завтрака."  // Черный чай
    )

    private val foodImageIds = listOf(
        R.id.foodImage1, R.id.foodImage2, R.id.foodImage3,
        R.id.foodImage4, R.id.foodImage5, R.id.foodImage6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_piterochka)
        buyButton = findViewById(R.id.buy_button)
        backButton = findViewById(R.id.back_button)

        backButton.setOnClickListener {
            val intent = Intent(this, ListRestaurant::class.java).apply {  }
            startActivity(intent)
        }

        buyButton.setOnClickListener {
            // Создаем список для хранения данных о выбранных товарах
            val selectedItems = mutableListOf<Bundle>()

            // Проходим по всем товарам
            for (i in quantities.indices) {
                if (quantities[i] > 0) { // Если количество больше 0
                    val itemBundle = Bundle().apply {
                        putInt("image", foodImageResources[i]) // ID ресурса изображения
                        putString("name", foodNames[i])        // Название товара
                        putDouble("price", prices[i])          // Цена товара
                        putInt("quantity", quantities[i])      // Количество товара
                    }
                    selectedItems.add(itemBundle)
                }
            }

            // Создаем Intent и передаем данные
            val intent = Intent(this, SaleFood::class.java).apply {
                putParcelableArrayListExtra("selected_items", ArrayList(selectedItems))
            }
            startActivity(intent)
        }

        val addButtonViews = addButtons.mapNotNull { findViewById<Button>(it) }
        val quantityContainerViews = quantityContainers.mapNotNull { findViewById<LinearLayout>(it) }
        val minusButtonViews = minusButtons.mapNotNull { findViewById<ImageView>(it) }
        val quantityTextViews = quantityTexts.mapNotNull { findViewById<TextView>(it) }
        val plusButtonViews = plusButtons.mapNotNull { findViewById<ImageView>(it) }

        for (i in addButtonViews.indices) {
            if (i < quantityContainerViews.size && i < minusButtonViews.size && i < quantityTextViews.size && i < plusButtonViews.size) {
                setupAddButton(addButtonViews[i], quantityContainerViews[i], quantityTextViews[i], i)
                setupPlusButton(plusButtonViews[i], quantityTextViews[i], i)
                setupMinusButton(minusButtonViews[i], quantityTextViews[i], quantityContainerViews[i], addButtonViews[i], i)
            } else {
                android.util.Log.e("StoreRestaurant", "Mismatch at index $i")
            }
        }
        val foodImageViews = foodImageIds.mapNotNull { findViewById<ImageView>(it) }
        for (i in foodImageViews.indices) {
            foodImageViews[i].setOnClickListener {
                openDishDetails(foodImageResources[i], prices[i].toString(), descriptions[i], reviews[i])
            }
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