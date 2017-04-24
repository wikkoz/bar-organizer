package com.example.barorganizer.database.dbo

data class IngredientDbo(val name: String, val id: Long) {
    constructor(ingredientDbo: IngredientDbo): this(ingredientDbo.name, ingredientDbo.id)
}