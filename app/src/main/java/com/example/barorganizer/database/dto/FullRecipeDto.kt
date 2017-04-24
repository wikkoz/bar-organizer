package com.example.barorganizer.database.dto

import android.graphics.Bitmap
import com.example.barorganizer.database.DbBitmapUtil
import com.example.barorganizer.database.dbo.RecipeDbo

data class FullRecipeDto(val name: String, val description: String?, val url: String?,
                         val ingredients: List<IngredientDto>, val image:Bitmap?) {
    constructor(recipeDbo: RecipeDbo, ingredients: List<IngredientDto>): this(recipeDbo.name, recipeDbo.description,
            recipeDbo.url, ingredients, DbBitmapUtil.getImage(recipeDbo.image))
}