package com.example.barorganizer.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.barorganizer.database.dto.FullRecipeDto
import com.example.barorganizer.database.dto.ListRecipeDto

class DatabaseFasade : SQLiteOpenHelper {

    companion object {
        private val DB_NAME = "bar_organizer"
        private val DB_VERSION = 1
    }

    private val context: Context
    private val recipe: RecipeJDBC
    private val ingredient: IngredientJDBC
    private val recipeIngredient: RecipieIngredientJDBC

    constructor(context: Context, recipe: RecipeJDBC, ingredient: IngredientJDBC, recipeIngredient: RecipieIngredientJDBC)
            : super(context, DB_NAME, null, DB_VERSION) {
        this.context = context;
        this.recipe = recipe;
        this.ingredient = ingredient
        this.recipeIngredient = recipeIngredient
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(RecipeJDBC.CREATE_RECIPE)
        db?.execSQL(IngredientJDBC.CREATE_INGREDIENT)
        db?.execSQL(RecipieIngredientJDBC.CREATE_RECIPE_INGREDIENT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + RecipeJDBC.RECIPE)
        db?.execSQL("DROP TABLE IF EXISTS " + IngredientJDBC.INGREDIENT)
        db?.execSQL("DROP TABLE IF EXISTS " + RecipieIngredientJDBC.RECIPE_INGREDIENT)

        onCreate(db)
    }

    fun getAllIngredients(): List<String> {
        return ingredient.getAllIngredients(readableDatabase)
                .map { it.name }
    }

    fun getAllRecipes(): List<ListRecipeDto> {
        return recipe.getAllListRecipes(readableDatabase)
                .map(::ListRecipeDto)
    }

    fun addRecipe(recipeDto: FullRecipeDto) {
        val ingredientsIds = getIngredientsIds(recipeDto)
        val recipeId = recipe.addRecipe(recipeDto, writableDatabase)
        recipeIngredient.addRecipesIngredients(writableDatabase, ingredientsIds, recipeId)
    }

    private fun getIngredientsIds(recipeDto: FullRecipeDto): List<Long> {
        val ingredients = ingredient.getAllIngredients(readableDatabase)
        val ingredientsIds = ingredients
                .filter { (ingredient) -> recipeDto.ingredients.contains(ingredient) }
                .map { it.id }
        val addedIds = ingredient.addIngredients(writableDatabase, recipeDto.ingredients
                .filterNot { recipeName -> ingredients.any { (dbName) -> dbName == recipeName } })
        ingredientsIds.toMutableList().addAll(addedIds)
        return ingredientsIds
    }
}
