package com.example.composehelloworld

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainActivityModel: ViewModel() {

    val client = OkHttpClient()

    fun getMemeArray(): List<String> {
        try {
            val request = Request.Builder()
                .url("https://ronreiter-meme-generator.p.rapidapi.com/images")
                .get()
                .addHeader("X-RapidAPI-Key", "00cf668ed5msh705f726058f3499p1b563fjsn8cd0a05a9234")
                .addHeader("X-RapidAPI-Host", "ronreiter-meme-generator.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val memeNameArray: List<String> = Gson().fromJson(response.body?.string(), Array<String>::class.java).toList()
                println("MEME ARRAY FOR FLOW:")
                println(memeNameArray)
                return memeNameArray
            }
        } catch (e: Exception) {
            println("MEME array Error: ")
            println(e)
            return emptyList()
        }
    }

    // Imitate a flow of events
    private fun events(): Flow<String> = getMemeArray().asFlow().onEach { delay(100) }

    fun main() = runBlocking<Unit> {
        this.launch {
            events()
                .onEach { event -> println("Meme Flow Event: $event") }
                .collect()
            println("Meme Flow Done")
        }
    }

    fun testThis() {
        viewModelScope.launch {
            events()
                .onEach { event -> println("Meme Flow Event: $event") }
                .collect()
            println("Meme Flow Done")
        }
    }

    fun getMeme(): ByteArray? {
        try {
            val request = Request.Builder()
                .url("https://ronreiter-meme-generator.p.rapidapi.com/meme?top=Top%20Text&bottom=Bottom%20Text&meme=Condescending-Wonka&font_size=50&font=Impact")
                .get()
                .addHeader("X-RapidAPI-Key", "00cf668ed5msh705f726058f3499p1b563fjsn8cd0a05a9234")
                .addHeader("X-RapidAPI-Host", "ronreiter-meme-generator.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                println("Response Body: ")
                return response.body!!.bytes()
            }
        } catch (e: NumberFormatException) {
            println("Meme Error: ")
            println(e)
            return null
        }
    }

    fun byteArrayToBitmap(): Bitmap? {
        return try {
            val byteArray = getMeme()
            byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }
        } catch (e: NumberFormatException) {
            println("Bitmap Error: ")
            println(e)
            null
        }
    }

//    fun getRandomMemeName() {
//        val rnds = (0..999).random()
//        val randomMemeName: String = getMemeArray()[rnds]
//        println("RANDOM MEME: ")
//        println(randomMemeName)
//    }
}