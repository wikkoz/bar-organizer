package com.example.barorganizer.database.dto

import android.graphics.Bitmap
import com.example.barorganizer.database.DbBitmapUtil
import com.example.barorganizer.database.RecipeDbo

data class ListRecipeDto(val name: String, val image: Bitmap?) {
    constructor(recipeDbo: RecipeDbo):this(recipeDbo.name, DbBitmapUtil.getImage(recipeDbo.imgae))
}