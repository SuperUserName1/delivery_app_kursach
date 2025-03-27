package com.example.delivery_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DodoPizza : AppCompatActivity() {
    private val addButtons = listOf(R.id.addButton1, R.id.addButton2, R.id.addButton3, R.id.addButton4, R.id.addButton5, R.id.addButton6)
    private val quantityContainers = listOf(R.id.quantityContainer1, R.id.quantityContainer2, R.id.quantityContainer3, R.id.quantityContainer4, R.id.quantityContainer5, R.id.quantityContainer6)
    private val minusButtons = listOf(R.id.minusButton1, R.id.minusButton2, R.id.minusButton3, R.id.minusButton4, R.id.minusButton5, R.id.minusButton6)
    private val quantityTexts = listOf(R.id.quantityText1, R.id.quantityText2, R.id.quantityText3, R.id.quantityText4, R.id.quantityText5, R.id.quantityText6)
    private val plusButtons = listOf(R.id.plusButton1, R.id.plusButton2, R.id.plusButton3, R.id.plusButton4, R.id.plusButton5, R.id.plusButton6)

    private val prices = listOf(319.00, 539.00, 339.00, 489.00, 539.00, 539.00)

    private val quantities = MutableList(addButtons.size) { 0 }

    private lateinit var buyButton: Button
    private lateinit var backButton: ImageView

    private val foodImageResources = listOf(
        R.drawable.pizza_peperoni_fresh, R.drawable.pizza_meet_adjika, R.drawable.pizza_chees,
        R.drawable.chill_grill, R.drawable.bavarskay, R.drawable.befstrogonov
    )

    private val foodNames = listOf(
        "Пепперони фреш",
        "Мясная с аджикой",
        "Сырная",
        "Чилл Грилл",
        "Баварская",
        "Бефстроганов"
    )
    private val descriptions = listOf(
        "Классическая пицца с острой пепперони, свежими томатами и сыром моцарелла на тонком тесте.",  // Пепперони фреш
        "Сочная пицца с разнообразными видами мяса, приправленная острой аджикой для любителей пикантных вкусов.",  // Мясная с аджикой
        "Нежная пицца с четырьмя видами сыра: моцарелла, чеддер, пармезан и дорблю, для настоящих сырных гурманов.",  // Сырная
        "Пицца с grilled chicken, болгарским перцем и луком, приправленная соусом BBQ для любителей гриля.",  // Чилл Грилл
        "Традиционная баварская пицца с колбасками, беконом, луком и горчичным соусом.",  // Баварская
        "Пицца с нежной говядиной, грибами и сливочным соусом, вдохновленная классическим блюдом бефстроганов."  // Бефстроганов
    )

    private val reviews = listOf(
        "Отличная пицца! Пепперони идеально сочетается с томатами, а тесто хрустящее и вкусное.",  // Пепперони фреш
        "Очень сытная и острая пицца! Аджика добавляет пикантности, а мясо просто тает во рту.",  // Мясная с аджикой
        "Если вы любите сыр, эта пицца для вас! Четыре вида сыра создают невероятный вкус.",  // Сырная
        "BBQ соус и grilled chicken — идеальное сочетание. Пицца получилась очень ароматной!",  // Чилл Грилл
        "Колбаски и бекон — это классика! Горчичный соус добавляет интересный акцент.",  // Баварская
        "Необычная пицца с говядиной и грибами. Сливочный соус делает её очень нежной."  // Бефстроганов
    )

    private val foodImageIds = listOf(
        R.id.foodImage1, R.id.foodImage2, R.id.foodImage3,
        R.id.foodImage4, R.id.foodImage5, R.id.foodImage6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dodo_pizza)
        buyButton = findViewById(R.id.buy_button)
        backButton = findViewById(R.id.back_button)

        backButton.setOnClickListener {
            val intent = Intent(this, ListRestaurant::class.java).apply {  }
            startActivity(intent)
        }

        buyButton.setOnClickListener {
            val selectedItems = mutableListOf<Bundle>()

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