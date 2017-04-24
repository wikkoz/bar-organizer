package com.example.barorganizer.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.ButterKnife
import com.example.barorganizer.R
import com.example.barorganizer.database.dto.ListRecipeDto

class RecipeListAdapter(context: Context,val elements: List<ListRecipeDto>) : ArrayAdapter<ListRecipeDto>(context, -1, elements) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if(view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_element, parent, false);
            val label = ButterKnife.findById<TextView>(view, R.id.element_label)
            val image = ButterKnife.findById<ImageView>(view, R.id.element_image)
            val recipe = elements[position]
            image.setImageBitmap(recipe.image)
            label.text = recipe.name
        }
        return view
    }
}