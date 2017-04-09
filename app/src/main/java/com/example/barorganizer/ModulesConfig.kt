package com.example.barorganizer

import android.content.Context
import com.example.barorganizer.views.*
import com.example.barorganizer.database.*


object ModulesConfig {
    private val recipeJdbc = RecipeJDBC()
    private val ingredientJdbc = IngredientJDBC()
    private val recipieIngredientJdbc = RecipieIngredientJDBC()

    fun start(context: Context) {
        ViewHandler.runActivity(context, AppActivity::class.java)

    }

    fun createSql(context: Context): DatabaseFasade {
        return DatabaseFasade(context, recipeJdbc, ingredientJdbc, recipieIngredientJdbc)
    }
}