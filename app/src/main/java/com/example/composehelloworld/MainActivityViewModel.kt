package com.example.composehelloworld

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import okhttp3.OkHttpClient
import java.util.*

class MainActivityModel : ViewModel() {

    val client = OkHttpClient()
    private val _memeData = MutableLiveData(MemeData(imageBitmap = null))
    val memeData: LiveData<MemeData>
        get() = _memeData

    private val memeList: MutableList<String> = ArrayList()

    private suspend fun events(): Flow<String> = MemeService().fetchMemeArray().asFlow().onEach { delay(1) }

    fun fetchInitialData() = runBlocking<Unit> {
        this.launch {
            events()
                .onEach { event ->
                    memeList.add(event)
                }
                .collect()
        }
    }

    fun byteArrayToBitmap(text: String): ImageBitmap {
        val byteArray = MemeService().fetchMeme(top = "", bottom = text, memeName = getRandomMemeName())
        var bitmap = byteArray.size.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }
        return bitmap.asImageBitmap()
    }

    fun onValueChanged(memeText: String) {
        _memeData.value = _memeData.value?.copy(
            memeText = memeText
        )
    }

    fun onImageChanged(imageBitmap: ImageBitmap) {
        _memeData.postValue(
            MemeData(
                imageBitmap = imageBitmap
            )
        )
    }

    private fun getRandomMemeName(): String {
       // return memeList.random()
        return "Condescending-Wonka"
    }
}

