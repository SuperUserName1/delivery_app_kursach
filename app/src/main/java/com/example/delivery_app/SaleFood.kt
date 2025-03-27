package com.example.delivery_app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SaleFood : AppCompatActivity() {

    private lateinit var backButton: ImageView
    private lateinit var buyButton: Button
    private lateinit var cardNumber: EditText
    private lateinit var cardExpiry: EditText
    private lateinit var cardCvv: EditText
    private lateinit var deliveryOptions: RadioGroup // Добавляем RadioGroup
    private val itemQuantities = mutableListOf<Int>() // Хранение количества каждого товара
    private val itemPrices = mutableListOf<Double>() // Хранение цен товаров

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sale_food)

        // Инициализация элементов интерфейса
        backButton = findViewById(R.id.back_button)
        buyButton = findViewById(R.id.buy_button)
        cardNumber = findViewById(R.id.card_number)
        cardExpiry = findViewById(R.id.card_expiry)
        cardCvv = findViewById(R.id.card_cvv)
        deliveryOptions = findViewById(R.id.delivery_options) // Инициализируем RadioGroup

        // Обработчик кнопки "Назад"
        backButton.setOnClickListener {
            finish()
        }

        // Получаем переданные данные
        val selectedItems = intent.getParcelableArrayListExtra<Bundle>("selected_items")
        val container = findViewById<LinearLayout>(R.id.items_container)

        // Отображаем каждый товар
        if (selectedItems != null) {
            for ((index, item) in selectedItems.withIndex()) {
                val name = item.getString("name")
                val imageResId = item.getInt("image")
                val price = item.getDouble("price")
                val quantity = item.getInt("quantity")

                // Сохраняем начальные значения
                itemPrices.add(price)
                itemQuantities.add(quantity)

                // Создаем карточку товара из шаблона
                val cardView = LayoutInflater.from(this).inflate(R.layout.item_order, container, false)
                val imageView = cardView.findViewById<ImageView>(R.id.order_image)
                val nameTextView = cardView.findViewById<TextView>(R.id.order_name)
                val priceTextView = cardView.findViewById<TextView>(R.id.order_price)
                val quantityTextView = cardView.findViewById<TextView>(R.id.order_quantity)
                val minusButton = cardView.findViewById<Button>(R.id.minus_button)
                val plusButton = cardView.findViewById<Button>(R.id.plus_button)

                // Заполняем данными
                imageView.setImageResource(imageResId)
                nameTextView.text = name
                priceTextView.text = "Цена: $price р"
                quantityTextView.text = "Кол-во: $quantity"

                // Обработчики кнопок "плюс" и "минус"
                minusButton.setOnClickListener {
                    if (itemQuantities[index] > 1) {
                        itemQuantities[index]--
                        quantityTextView.text = "Кол-во: ${itemQuantities[index]}"
                        updateTotalPrice()
                    }
                }
                plusButton.setOnClickListener {
                    itemQuantities[index]++
                    quantityTextView.text = "Кол-во: ${itemQuantities[index]}"
                    updateTotalPrice()
                }

                // Добавляем карточку в контейнер
                container.addView(cardView)
            }
        }

        // Изначальное обновление итоговой цены
        updateTotalPrice()

        // Обработчик кнопки "Купить"
        buyButton.setOnClickListener {
            if (validateFields()) {
                // Определяем выбранный способ получения
                val selectedOption = when (deliveryOptions.checkedRadioButtonId) {
                    R.id.radio_delivery -> "Доставка"
                    R.id.radio_pickup -> "Самовывоз"
                    else -> "Доставка" // По умолчанию
                }
                // Формируем сообщение в зависимости от выбора
                val message = if (selectedOption == "Доставка") {
                    "Покупка завершена! Курьер уже в пути."
                } else {
                    "Покупка завершена! Ваш заказ готов к самовывозу."
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                val intent = Intent(this, ListRestaurant::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Метод для обновления итоговой цены
    private fun updateTotalPrice() {
        var total = 0.0
        for (i in itemQuantities.indices) {
            total += itemPrices[i] * itemQuantities[i]
        }
        buyButton.text = "Купить ($total р)"
    }

    // Метод для проверки заполнения всех полей
    private fun validateFields(): Boolean {
        return cardNumber.text.isNotEmpty() && cardExpiry.text.isNotEmpty() && cardCvv.text.isNotEmpty()
    }
}