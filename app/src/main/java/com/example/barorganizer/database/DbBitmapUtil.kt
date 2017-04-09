package com.example.barorganizer.database

import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import java.io.ByteArrayOutputStream


object DbBitmapUtil {
    fun getBytes(bitmap: Bitmap?): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap?.compress(CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    fun getImage(image: ByteArray?): Bitmap? {
        if (image != null) {
            return BitmapFactory.decodeByteArray(image, 0, image.size)
        }
        return null
    }
}