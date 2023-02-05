package com.example.composehelloworld

import androidx.compose.ui.graphics.ImageBitmap

data class MemeData(
    val memeText: String = "Hello World",
    val imageBitmap: ImageBitmap? = null
)
