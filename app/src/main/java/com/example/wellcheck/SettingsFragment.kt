import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.wellcheck.R
import kotlin.math.pow

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Connect XML views to Kotlin code
        val heightInput = view.findViewById<EditText>(R.id.heightInput)
        val weightInput = view.findViewById<EditText>(R.id.weightInput)
        val calculateButton = view.findViewById<Button>(R.id.calculateButton)
        val resultText = view.findViewById<TextView>(R.id.resultText)
        val tipsText = view.findViewById<TextView>(R.id.tipsText)

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

        return view
    }
}
