package com.example.wellcheck.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wellcheck.R
import kotlin.math.pow



class Bmi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        // Connect XML views to Kotlin code
        val heightInput = findViewById<EditText>(R.id.heightInput)
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val calculateButton = findViewById<Button>(R.id.calculateButton)
        val resultText = findViewById<TextView>(R.id.resultText)
        val tipsText = findViewById<TextView>(R.id.tipsText)

        // Set up calculate button click listener
        calculateButton.setOnClickListener {
            val heightFeet = heightInput.text.toString().toDoubleOrNull()
            val weightKg = weightInput.text.toString().toDoubleOrNull()

            if (heightFeet != null && weightKg != null) {
                // Convert height from feet to meters
                val heightMeters = heightFeet * 0.3048
                // Calculate BMI
                val bmi = weightKg / heightMeters.pow(2)

                // Determine BMI category and provide health tips
                val (resultMessage, tips) = when {
                    bmi < 18.5 -> "Underweight" to "Consider a nutrient-rich diet with balanced calories."
                    bmi in 18.5..24.9 -> "Normal" to "Maintain your lifestyle to keep a normal BMI."
                    bmi in 25.0..29.9 -> "Overweight" to "Incorporate regular exercise and a balanced diet."
                    else -> "Obese" to "Consult a healthcare provider for personalized guidance."
                }

                // Display the BMI result and tips
                resultText.text = "Your BMI is %.2f, which is classified as $resultMessage.".format(bmi)
                tipsText.text = tips
            } else {
                resultText.text = "Please enter valid values for height and weight."
                tipsText.text = ""
            }
        }
    }
}
