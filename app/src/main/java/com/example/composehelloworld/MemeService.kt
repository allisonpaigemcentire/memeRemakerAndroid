package com.example.composehelloworld

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlin.coroutines.suspendCoroutine

class MemeService(
    private val client: OkHttpClient = OkHttpClient()
) {

    suspend fun fetchMemeArray(): List<String> {
        return suspendCoroutine { continuation ->
            val request = Request.Builder()
                .url("https://ronreiter-meme-generator.p.rapidapi.com/images")
                .get()
                .addHeader("X-RapidAPI-Key", "00cf668ed5msh705f726058f3499p1b563fjsn8cd0a05a9234")
                .addHeader("X-RapidAPI-Host", "ronreiter-meme-generator.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val memeNameArray: List<String> = Gson().fromJson(response.body?.string(), Array<String>::class.java).toList()
                println("MEME ARRAY FOR FLOW:")
                println(memeNameArray)
                continuation.resumeWith(Result.success(memeNameArray))
            } else {
                continuation.resumeWith(Result.failure(Exception("No meme array found")))
            }
        }
    }

    fun fetchMeme(top: String, bottom: String, memeName: String): ByteArray {
        val request = Request.Builder()
            .url("https://ronreiter-meme-generator.p.rapidapi.com/meme?top=" + top + "&bottom=" + bottom + "&meme=" + memeName + "&font_size=50&font=Impact")
            .get()
            .addHeader("X-RapidAPI-Key", "00cf668ed5msh705f726058f3499p1b563fjsn8cd0a05a9234")
            .addHeader("X-RapidAPI-Host", "ronreiter-meme-generator.p.rapidapi.com")
            .build()
        val response = client.newCall(request).execute()
        return response.body!!.bytes()
    }

}