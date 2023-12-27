package com.example.moviesapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.IOException
import java.io.InputStream

object ImageExtractor {

    fun getImagesFromAssets(imageName: String, context: Context): Bitmap {
        Log.i("TAG", "getImagesFromAssets: called on $imageName")
        val assetsManager = context.assets
        var inputStream: InputStream? = null
        try {
            inputStream = assetsManager.open("resourcesnamed/$imageName.png")

            return BitmapFactory.decodeStream(inputStream)

        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        } finally {
            inputStream?.close()
        }
    }
}