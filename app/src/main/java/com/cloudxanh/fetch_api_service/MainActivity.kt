package com.cloudxanh.fetch_api_service

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cloudxanh.fetch_api_service.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchCurrencyData().start()

//        val threadName = Thread.currentThread().name
//        val threadId = Thread.currentThread().id
    }

    private fun fetchCurrencyData(): Thread {
        return Thread {
            val threadName = Thread.currentThread().name
            Log.e("name", "fetchCurrencyData: $threadName") //  Thread-work

            val url = URL("https://open.er-api.com/v6/latest/aud")
            val connection = url.openConnection() as HttpsURLConnection

            if (connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, Request::class.java)
                updateUI(request)
                inputStreamReader.close()
                inputSystem.close()
            } else {
                binding.baseCurrency.text = "Failed Connection"
            }
        }
    }

    private fun updateUI(request: Request) {
        runOnUiThread {
            val threadName = Thread.currentThread().name
            Log.e("name", "updateUI: $threadName") // Main-Thread

            kotlin.run {
                binding.lastUpdated.text = request.time_last_update_utc
                binding.nzd.text = String.format("EUR: %.2f", request.rates.EUR)
                binding.usd.text = String.format("USD: %.2f", request.rates.USD)
                binding.gbp.text = String.format("VND: %.2f", request.rates.VND)
            }
        }
    }
}