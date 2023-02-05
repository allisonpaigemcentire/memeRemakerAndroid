package com.example.composehelloworld

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import java.util.*

class MainActivityModel : ViewModel() {

    val client = OkHttpClient()
    private val _memeData = MutableLiveData(MemeData(imageBitmap = null))
    val memeData: LiveData<MemeData>
        get() = _memeData



    suspend fun events(): Flow<String> = MemeService().fetchMemeArray().asFlow().onEach { delay(100) }
    fun getImage(): ImageBitmap = byteArrayToBitmap()

    fun byteArrayToBitmap(): ImageBitmap {
        val byteArray = MemeService().fetchMeme(top = memeData.value?.memeText ?: "fail", bottom = "")
        var bitmap = byteArray.size.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }
        return bitmap.asImageBitmap()
    }

    fun onValueChanged(memeText: String) {
        _memeData.value = _memeData.value?.copy(
            memeText = memeText
        )
    }

    fun onImageChanged(imageBitmap: ImageBitmap) {
        println("just setting this value")
        print(imageBitmap)
        _memeData.value
    }


    fun getRandomMemeText(): String {
        val random = Random()
        return random.toString()
    }


//    fun getRandomMemeName() {
//        val rnds = (0..999).random()
//        val randomMemeName: String = events()[rnds]
//        println("RANDOM MEME: ")
//        println(randomMemeName)
//    }
}

