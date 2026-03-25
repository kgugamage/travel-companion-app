package com.example.travelapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var categorySpinner: Spinner
    lateinit var fromSpinner: Spinner
    lateinit var toSpinner: Spinner
    lateinit var inputValue: EditText
    lateinit var convertButton: Button
    lateinit var resultText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        categorySpinner = findViewById(R.id.spCategory)
        fromSpinner = findViewById(R.id.spFrom)
        toSpinner = findViewById(R.id.spTo)
        inputValue = findViewById(R.id.etValue)
        convertButton = findViewById(R.id.btnConvert)
        resultText = findViewById(R.id.tvResult)

        val categories = arrayOf("Currency", "Fuel", "Temperature", "Volume")
        categorySpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                updateUnits(categories[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        convertButton.setOnClickListener {
            convertValue()
        }
    }

    private fun updateUnits(category: String) {
        val units = when(category) {
            "Currency" -> arrayOf("USD","AUD","EUR","JPY","GBP")
            "Fuel" -> arrayOf("mpg","km/L")
            "Volume" -> arrayOf("Gallon","Liter")
            else -> arrayOf("C","F","K")
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, units)
        fromSpinner.adapter = adapter
        toSpinner.adapter = adapter
    }
    private fun convertValue() {
        val input = inputValue.text.toString()
        val value = input.toDoubleOrNull() ?: 0.0

        val from = fromSpinner.selectedItem.toString()
        val to = toSpinner.selectedItem.toString()
        val category = categorySpinner.selectedItem.toString()

        var result = 0.0

        when(category) {

            "Currency" -> {
                val usd = when(from) {
                    "AUD" -> value / 1.55
                    "EUR" -> value / 0.92
                    "JPY" -> value / 148.50
                    "GBP" -> value / 0.78
                    else -> value
                }

                result = when(to) {
                    "AUD" -> usd * 1.55
                    "EUR" -> usd * 0.92
                    "JPY" -> usd * 148.50
                    "GBP" -> usd * 0.78
                    else -> usd
                }
            }

            "Fuel" -> {
                result = if (from == "mpg" && to == "km/L") {
                    value * 0.425
                } else {
                    value / 0.425
                }
            }

            "Temperature" -> {
                result = when {
                    from == "C" && to == "F" -> (value * 1.8) + 32
                    from == "F" && to == "C" -> (value - 32) / 1.8
                    from == "C" && to == "K" -> value + 273.15
                    else -> value
                }
            }

            "Volume" -> {
                result = if (from == "Gallon" && to == "Liter") {
                    value * 3.785
                } else {
                    value / 3.785
                }
            }
        }

        resultText.text = "Result: $result"
    }
}
