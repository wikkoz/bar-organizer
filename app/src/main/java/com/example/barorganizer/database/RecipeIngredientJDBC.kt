package com.example.barorganizer.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import com.example.barorganizer.database.dbo.IngredientDbo
import com.example.barorganizer.database.dbo.RecipeIngredientDbo

class RecipeIngredientJDBC {
    companion object {
        val RECIPE_INGREDIENT = "RECIPE_INGREDIENT"
        private val ID = "ID"
        private val RECIPE_ID = "RECIPE_ID"
        private val INGREDIENT_ID = "INGREDIENT_ID"
        private val AMOUNT = "AMOUNT"

        val CREATE_RECIPE_INGREDIENT = "CREATE TABLE $RECIPE_INGREDIENT ( " +
                " $ID INTEGER PRIMARY KEY autoincrement, " +
                " $RECIPE_ID  INTEGER not null, " +
                " $INGREDIENT_ID  INTEGER not null, " +
                " $AMOUNT  varchar(20) not null, " +
                " FOREIGN KEY ( $RECIPE_ID  ) REFERENCES " + RecipeJDBC.RECIPE + " (" + RecipeJDBC.ID + "), " +
                " FOREIGN KEY ( $INGREDIENT_ID  ) REFERENCES " + IngredientJDBC.INGREDIENT + " ( " + IngredientJDBC.ID + " ));"

        val SELECT_ID = "SELECT $RECIPE_ID, $INGREDIENT_ID, $AMOUNT FROM $RECIPE_INGREDIENT WHERE $RECIPE_ID = ?"
    }

    fun addRecipesIngredients(db: SQLiteDatabase, ingredients: List<Pair<Long, String>>, recipe: Long) {
        db.beginTransaction()
        ingredients.map { mapToContentValues(it, recipe) }
                .forEach { db.insert(RECIPE_INGREDIENT, null, it) }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    private fun mapToContentValues(ingredient: Pair<Long, String>, recipe: Long): ContentValues {
        val cv = ContentValues()
        cv.put(AMOUNT, ingredient.second)
        cv.put(INGREDIENT_ID, ingredient.first)
        cv.put(RECIPE_ID, recipe)
        return cv
    }

    fun getRecipiesIngredients(id: Long, db: SQLiteDatabase): List<RecipeIngredientDbo> {
        db.beginTransaction()
        val cursor = db.rawQuery(SELECT_ID, arrayOf(id.toString()))
        val elements = generateSequence { if (cursor.moveToNext()) cursor else null }
                .map { mapToDbo(it) }
                .toList()
        cursor.close()
        db.setTransactionSuccessful()
        db.endTransaction()
        return elements
    }

    private fun mapToDbo(cursor: Cursor): RecipeIngredientDbo {
        val recipe = cursor.getColumnIndex(RECIPE_ID)
        val ingredient = cursor.getColumnIndex(INGREDIENT_ID)
        val amount = cursor.getColumnIndex(AMOUNT)
        return RecipeIngredientDbo(cursor.getLong(recipe), cursor.getLong(ingredient), cursor.getString(amount))
    }
}