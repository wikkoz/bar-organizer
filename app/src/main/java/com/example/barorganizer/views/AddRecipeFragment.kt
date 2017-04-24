package com.example.barorganizer.views

import android.app.Activity.RESULT_OK
import android.app.Fragment
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.ButterKnife
import com.example.barorganizer.ModulesConfig
import com.example.barorganizer.R
import com.example.barorganizer.database.DatabaseFacade
import com.example.barorganizer.database.dto.FullRecipeDto
import com.example.barorganizer.database.dto.IngredientDto
import com.example.barorganizer.views.adapters.AddIngredientsListAdapter


class AddRecipeFragment : Fragment() {
    companion object {
        val NAME = R.id.menu_new
        val CAMERA_REQUEST = 100
        val DELTA = 50
    }

    lateinit var recipeName: EditText
    lateinit var recipeDescription: EditText
    lateinit var ingredientName: EditText
    lateinit var ingredientAmount: EditText
    lateinit var db: DatabaseFacade
    lateinit var currentImage: ImageView
    lateinit var button: Button
    lateinit var ingredients: ListView
    lateinit var spinner: Spinner
    lateinit var addIngredientButton: Button
    lateinit var addRecipeButton: Button
    val ingredientsList: MutableList<IngredientDto> = mutableListOf()
    private var historicX: Float = 0.0f
    private var historicY: Float = 0.0f
    private lateinit var adapter: ArrayAdapter<IngredientDto>
    private lateinit var allRecipes: List<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)
        initElements(view)
        db = ModulesConfig.createSql(activity)
        adapter = AddIngredientsListAdapter(activity, ingredientsList)
        ingredients.adapter = adapter
        allRecipes = db.getAllRecipes().map { it.name }
        val spinnerAdapter = ArrayAdapter.createFromResource(activity, R.array.amount_suffix, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        ingredients.setOnTouchListener { v, event -> slideList(v, event) }
        button.setOnClickListener { callCamera() }
        addIngredientButton.setOnClickListener { addIngredient() }
        addRecipeButton.setOnClickListener { addRecipe() }
        return view
    }

    private fun initElements(view: View) {
        currentImage = ButterKnife.findById(view, R.id.add_image)
        button = ButterKnife.findById(view, R.id.btnAdd)
        addRecipeButton = ButterKnife.findById(view, R.id.add_recipe)
        addIngredientButton = ButterKnife.findById(view, R.id.add_ingredient_button)
        ingredients = ButterKnife.findById(view, R.id.add_ingredients_list)
        spinner = ButterKnife.findById(view, R.id.add_suffixes_spinner)
        ingredientName = ButterKnife.findById(view, R.id.add_ingredient_name)
        ingredientAmount = ButterKnife.findById(view, R.id.add_ingredient_amount)
        recipeName = ButterKnife.findById(view, R.id.add_name)
        recipeDescription = ButterKnife.findById(view, R.id.add_description)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            CAMERA_REQUEST -> saveImage(data)
        }
    }

    private fun saveImage(data: Intent?) {
        val bundle = data?.extras
        if (bundle != null) {
            val image: Bitmap = bundle.getParcelable("data")
            currentImage.setImageBitmap(image)
            currentImage.visibility = View.VISIBLE
        }
    }

    private fun callCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST)
        intent.type = "image/*";
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 200);
    }

    private fun slideList(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                historicX = event.x
                historicY = event.y
                return false
            }
            MotionEvent.ACTION_UP -> {
                val heightOfEachItem = ingredients.getChildAt(0).height
                val heightOfFirstItem = -ingredients.getChildAt(0).top + ingredients.firstVisiblePosition * heightOfEachItem
                val firstPosition = Math.ceil((heightOfFirstItem / heightOfEachItem).toDouble()).toInt()
                val wantedPosition = Math.floor((historicY.toDouble() - ingredients.getChildAt(0).top) / heightOfEachItem).toInt() + firstPosition
                val wantedChild = wantedPosition - firstPosition
                if (Math.abs(event.x - historicX) > DELTA) {
                    if (wantedChild < 0 || wantedChild >= ingredientsList.size || wantedChild >= ingredients.childCount) {
                        return true
                    }
                    adapter.remove(ingredientsList[wantedChild])
                    return true
                }
            }
        }
        return false
    }

    private fun addIngredient() {
        adapter.add(IngredientDto(ingredientName.text.toString(),
                ingredientAmount.text.toString() + " " + spinner.selectedItem.toString()))
    }

    private fun addRecipe() {
        if (validateRecipe()) {
            val image: Bitmap? = (currentImage.drawable as? BitmapDrawable)?.bitmap
            val recipe = FullRecipeDto(recipeName.text.toString(),
                    recipeDescription.text.toString(), "url", ingredientsList, image)
            db.addRecipe(recipe)
        }
    }

    private fun validateRecipe(): Boolean {
        if (recipeName.text.toString().isEmpty() || allRecipes.contains(recipeName.text.toString())) {
            recipeName.error="Nie poprawna nazwa"
            return false;
        }
        return true;
    }
}
