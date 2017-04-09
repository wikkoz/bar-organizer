package com.example.barorganizer.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class IngredientJDBC {
    companion object {
        val INGREDIENT = "INGREDIENT"
        val ID = "ID"
        private val NAME = "NAME"

        val CREATE_INGREDIENT = "CREATE TABLE $INGREDIENT ( " +
                "$ID  INTEGER PRIMARY KEY autoincrement, " +
                "$NAME varchar(50) unique);"

        private val SELECT_ALL_SQL = "SELECT $ID, $NAME FROM $INGREDIENT"
    }

    fun addIngredients(db: SQLiteDatabase, ingredients: List<String>) :List<Long> {
        db.beginTransaction()
        val ids = ingredients.map { mapToContentValues(it) }
                .map { db.insert(RecipieIngredientJDBC.RECIPE_INGREDIENT, null, it) }
        db.setTransactionSuccessful()
        db.endTransaction()
        return ids
    }

    private fun mapToContentValues(ingredient: String): ContentValues {
        val cv = ContentValues()
        cv.put(NAME, ingredient)
        return cv
    }

    fun getAllIngredients(db: SQLiteDatabase): List<IngredientDbo> {
        db.beginTransaction()
        val cursor = db.rawQuery(SELECT_ALL_SQL, arrayOf<String>())
        val nameIndex = cursor.getColumnIndex(NAME)
        val idIndex = cursor.getColumnIndex(ID)
        val ingredients = generateSequence { if (cursor.moveToNext()) cursor else null }
                .map { c -> mapToDbo(c, nameIndex, idIndex) }
                .toList()
        cursor.close()
        db.setTransactionSuccessful()
        db.endTransaction()
        return ingredients
    }

    fun mapToDbo(cursor: Cursor, nameIndex: Int, idIndex: Int): IngredientDbo {
        return IngredientDbo(cursor.getString(nameIndex), cursor.getLong(idIndex))
    }
}