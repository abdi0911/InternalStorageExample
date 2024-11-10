package com.example.internalstorageexample

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.internalstorageexample.databinding.ActivityMainBinding
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val FILE_NAME = "rpl.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up button click listener
        binding.btnSaveText.setOnClickListener {
            // Open SecondActivity when the button is clicked
            startActivity(Intent(this, SecondActivity::class.java))
        }
    }

    // Save data to internal storage
    private fun saveData() {
        Thread {
            try {
                // Open the file in private mode
                val out = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
                out.use {
                    // Write the text to the file
                    it.write(binding.etInputText.text.toString().toByteArray())
                }

                // Show toast when saved
                runOnUiThread {
                    Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
                }
            } catch (ioe: IOException) {
                // Log any error that occurs while saving
                Log.w(TAG, "Error while saving $FILE_NAME : $ioe")
            }
        }.start()
    }

    override fun onPause() {
        super.onPause()
        // Save data when the activity goes into the background
        saveData()
    }

    override fun onResume() {
        super.onResume()
        // Load data when the activity is resumed
        loadData()
    }

    // Load data from internal storage
    private fun loadData() {
        Thread {
            try {
                // Open the file for reading
                val input = openFileInput(FILE_NAME)
                input.use {
                    val buffer = StringBuilder()
                    var bytesRead = input.read()

                    // Read the file byte by byte
                    while (bytesRead != -1) {
                        buffer.append(bytesRead.toChar())
                        bytesRead = input.read()
                    }

                    // Set the content of the EditText with the loaded text
                    runOnUiThread {
                        binding.etInputText.setText(buffer.toString())
                    }
                }
            } catch (fnf: FileNotFoundException) {
                // Log if the file is not found (first time opening the app)
                Log.w(TAG, "File not found, occurs only once")
            } catch (ioe: IOException) {
                // Log any IOException
                Log.w(TAG, "IOException : $ioe")
            }
        }.start()
    }
}
