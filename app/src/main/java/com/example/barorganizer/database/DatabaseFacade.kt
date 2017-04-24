package com.example.barorganizer.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.barorganizer.database.dto.FullRecipeDto
import com.example.barorganizer.database.dto.IngredientDto
import com.example.barorganizer.database.dto.ListRecipeDto

class DatabaseFacade(context: Context, private val recipe: RecipeJDBC,
                     private val ingredient: IngredientJDBC, private val recipeIngredient: RecipeIngredientJDBC) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private val DB_NAME = "bar_organizer"
        private val DB_VERSION = 1
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(RecipeJDBC.CREATE_RECIPE)
        db?.execSQL(IngredientJDBC.CREATE_INGREDIENT)
        db?.execSQL(RecipeIngredientJDBC.CREATE_RECIPE_INGREDIENT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + RecipeJDBC.RECIPE)
        db?.execSQL("DROP TABLE IF EXISTS " + IngredientJDBC.INGREDIENT)
        db?.execSQL("DROP TABLE IF EXISTS " + RecipeIngredientJDBC.RECIPE_INGREDIENT)

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

    fun getFullRecipe(id: Long) : FullRecipeDto {
        val recipe = recipe.getRecipe(readableDatabase, id)
        return FullRecipeDto(recipe, getIngredients(recipe.id))
    }

    fun addRecipe(recipeDto: FullRecipeDto) {
        val ingredientsIdsWithAmount = getIngredientsIdsWithAmount(recipeDto)
        val recipeId = recipe.addRecipe(recipeDto, writableDatabase)
        recipeIngredient.addRecipesIngredients(writableDatabase, ingredientsIdsWithAmount, recipeId)
    }

    private fun getIngredientsIdsWithAmount(recipeDto: FullRecipeDto): List<Pair<Long, String>> {
        val ingredients = ingredient.getAllIngredients(readableDatabase)
        val ingredientsIds = ingredients
                .filter { (name) ->
                    recipeDto.ingredients
                            .map { it.ingredient }
                            .contains(name)
                }
        val ingredientsWithAmount = recipeDto.ingredients
                .filter { (ingredient1) -> ingredientsIds.any { (name) -> ingredient1 == name } }
                .map { (ingredient1, amount) ->
                    Pair(ingredientsIds.find { (name) -> ingredient1 == name }!!.id, amount)
                }

        val addedIdsWithAmount = ingredient.addIngredients(writableDatabase, recipeDto.ingredients
                .map { it.ingredient }
                .filterNot { recipeName -> ingredients.any { (dbName) -> dbName == recipeName } })
                .map { it -> Pair(it.first, recipeDto.ingredients.find { (ing) -> ing == it.second }!!.amount) }
        ingredientsWithAmount.toMutableList().addAll(addedIdsWithAmount)
        return ingredientsWithAmount
    }

    private fun getIngredients(recipeId: Long): List<IngredientDto> {
        val recipeIngredients = recipeIngredient.getRecipiesIngredients(recipeId, readableDatabase)
        val ids = recipeIngredients.map { it.ingredientId }
        val ingredients = ingredient.getIngredients(ids, readableDatabase)
        return ingredients
                .map { (name, id) -> IngredientDto(name,
                        recipeIngredients.find { it.ingredientId == id }!!.amount) }
    }
}
