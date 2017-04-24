package com.example.barorganizer.database.dto

import android.graphics.Bitmap
import com.example.barorganizer.database.DbBitmapUtil
import com.example.barorganizer.database.dbo.RecipeDbo

data class ListRecipeDto(val id: Long, val name: String, val image: Bitmap?) {
    constructor(recipeDbo: RecipeDbo) : this(recipeDbo.id, recipeDbo.name, DbBitmapUtil.getImage(recipeDbo.image))
}