package com.example.barorganizer.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.barorganizer.database.dbo.RecipeDbo
import com.example.barorganizer.database.dto.FullRecipeDto
import com.example.barorganizer.database.dto.ListRecipeDto

class RecipeJDBC {
    companion object {
        val RECIPE = "RECIPE"
        val ID = "ID"
        val DESCRIPTION = "description"
        val URL = "url"
        val IMAGE = "image"
        val NAME = "name"

        val CREATE_RECIPE = "CREATE TABLE $RECIPE ( " +
                " $ID  INTEGER PRIMARY KEY autoincrement, " +
                " $DESCRIPTION  varchar(400), " +
                " $URL  varchar(100), " +
                " $IMAGE blob, " +
                " $NAME varchar(100) unique not null);"

        val SELECT_ALL_SQL = "SELECT $ID, $DESCRIPTION, $URL, $IMAGE, $NAME FROM $RECIPE"
        val SELECT_WITH_ID = "SELECT $ID, $DESCRIPTION, $URL, $IMAGE, $NAME FROM $RECIPE WHERE $ID = ?"

    }

    fun addRecipe(fullRecipeDto: FullRecipeDto, db: SQLiteDatabase): Long {
        val cv = ContentValues()
        cv.put(DESCRIPTION, fullRecipeDto.description)
        cv.put(URL, fullRecipeDto.url)
        cv.put(IMAGE, DbBitmapUtil.getBytes(fullRecipeDto.image))
        cv.put(NAME, fullRecipeDto.name)

        return db.insert(RECIPE, null, cv)
    }

    fun getAllListRecipes(db: SQLiteDatabase) : List<RecipeDbo> {
        db.beginTransaction()
        val cursor = db.rawQuery(SELECT_ALL_SQL, arrayOf<String>())
        val recipes = generateSequence { if (cursor.moveToNext()) cursor else null }
                .map { c -> mapToDbo(c) }
                .toList()
        cursor.close()
        db.setTransactionSuccessful()
        db.endTransaction()
        return recipes
    }

    fun getRecipe(db: SQLiteDatabase, id: Long) : RecipeDbo {
        db.beginTransaction()
        val cursor = db.rawQuery(SELECT_WITH_ID, arrayOf(id.toString()))
        val recipe =  mapToDbo(cursor)
        cursor.close()
        db.setTransactionSuccessful()
        db.endTransaction()
        return recipe
    }

    fun mapToDbo(cursor: Cursor): RecipeDbo {
        val nameIndex = cursor.getColumnIndex(NAME)
        val idIndex = cursor.getColumnIndex(ID)
        val descriptionIndex = cursor.getColumnIndex(DESCRIPTION)
        val urlIndex = cursor.getColumnIndex(URL)
        val imageIndex = cursor.getColumnIndex(IMAGE)
        return RecipeDbo(cursor.getLong(idIndex), cursor.getString(nameIndex), cursor.getString(descriptionIndex),
                cursor.getString(urlIndex), cursor.getBlob(imageIndex))
    }
}