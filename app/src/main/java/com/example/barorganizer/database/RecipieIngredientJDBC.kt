package com.example.barorganizer.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

class RecipieIngredientJDBC {
    companion object {
        val RECIPE_INGREDIENT = "RECIPE_INGREDIENT"
        private val ID = "ID"
        private val RECIPE_ID = "RECIPE_ID"
        private val INGREDIENT_ID = "INGREDIENT_ID"

        val CREATE_RECIPE_INGREDIENT = "CREATE TABLE $RECIPE_INGREDIENT ( " +
                " $ID INTEGER PRIMARY KEY autoincrement, " +
                " $RECIPE_ID  INTEGER not null, " +
                " $INGREDIENT_ID  INTEGER not null, " +
                " FOREIGN KEY ( $RECIPE_ID  ) REFERENCES " + RecipeJDBC.RECIPE + " (" + RecipeJDBC.ID + "), " +
                " FOREIGN KEY ( $INGREDIENT_ID  ) REFERENCES " + IngredientJDBC.INGREDIENT + " ( " + IngredientJDBC.ID + " ));"
    }

    fun addRecipesIngredients(db: SQLiteDatabase, ingredients: List<Long>, recipe: Long) {
        db.beginTransaction()
        ingredients.map { mapToContentValues(it, recipe) }
                .forEach { db.insert(RECIPE_INGREDIENT, null, it) }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    private fun mapToContentValues(ingredient: Long, recipe: Long): ContentValues {
        val cv = ContentValues()
        cv.put(INGREDIENT_ID, ingredient)
        cv.put(RECIPE_ID, recipe)
        return cv
    }
}