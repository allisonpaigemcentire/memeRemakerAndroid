package com.example.composehelloworld

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient

class MainActivityModel: ViewModel() {

    val client = OkHttpClient()
    suspend fun events(): Flow<String> = MemeService().fetchMemeArray().asFlow().onEach { delay(100) }
    suspend fun getImage(): ImageBitmap = byteArrayToBitmap()

    fun main() = runBlocking<Unit> {
        this.launch {
            events()
                .onEach { event -> println("Meme Flow Event: $event") }
                .collect()
            println("Meme Flow Done")
        }
    }

    suspend fun byteArrayToBitmap(): ImageBitmap {

        val byteArray = MemeService().fetchMeme()
        var bitmap = byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }
        println("returning imagebitmap meme image")
        return bitmap.asImageBitmap()
    }

//    fun getRandomMemeName() {
//        val rnds = (0..999).random()
//        val randomMemeName: String = events()[rnds]
//        println("RANDOM MEME: ")
//        println(randomMemeName)
//    }
}

