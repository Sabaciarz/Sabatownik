package sabat.sabatownik.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import sabat.sabatownik.R
import android.widget.EditText
import android.widget.Button
import android.widget.Spinner
import sabat.sabatownik.logic.AssetsBaseProvider


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.baseSpinner)
        val limitInput = findViewById<EditText>(R.id.repeatLimitInput)
        val startBtn = findViewById<Button>(R.id.startButton)

        val baseProvider = AssetsBaseProvider(this)
        val bases = baseProvider.getAvailableBases()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            bases
        )
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinner.adapter = adapter

        startBtn.setOnClickListener {
            val limit = limitInput.text.toString().toIntOrNull() ?: 1
            val selectedBase = spinner.selectedItem.toString()

            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("BASE_NAME", selectedBase)
            intent.putExtra("LIMIT", limit)
            startActivity(intent)
        }
    }
}
