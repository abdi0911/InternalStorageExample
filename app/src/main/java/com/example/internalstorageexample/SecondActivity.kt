package com.example.internalstorageexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.internalstorageexample.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the back button listener to finish the activity
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Load data from file when the activity is resumed
        loadData()
    }

    // Function to load data from the internal storage file
    private fun loadData() {
        Thread {
            try {
                // Open the file in private mode
                val input = openFileInput(MainActivity.FILE_NAME)
                input.use {
                    val buffer = StringBuilder()
                    var bytesRead = input.read()

                    // Read the file byte by byte
                    while (bytesRead != -1) {
                        buffer.append(bytesRead.toChar())
                        bytesRead = input.read()
                    }

                    // Update the UI with the loaded text
                    runOnUiThread {
                        binding.tvOutputText.text = buffer.toString()
                    }
                }
            } catch (e: Exception) {
                // Handle any exceptions (e.g., file not found or IO errors)
                e.printStackTrace()
            }
        }.start()
    }
}
