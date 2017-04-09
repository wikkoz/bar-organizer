package com.example.barorganizer.database.dto

import android.graphics.Bitmap
import com.example.barorganizer.database.DbBitmapUtil
import com.example.barorganizer.database.RecipeDbo

data class FullRecipeDto(val name: String, val description: String?, val url: String?,
                         val ingredients: List<String>, val image:Bitmap?) {
    constructor(recipeDbo: RecipeDbo, ingredients: List<String>): this(recipeDbo.name, recipeDbo.description,
            recipeDbo.url, ingredients, DbBitmapUtil.getImage(recipeDbo.imgae))
}