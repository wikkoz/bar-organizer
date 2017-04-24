package com.example.barorganizer.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import butterknife.ButterKnife
import com.example.barorganizer.R
import com.example.barorganizer.database.dto.IngredientDto


class AddIngredientsListAdapter (context: Context, var elements: MutableList<IngredientDto>) : ArrayAdapter<IngredientDto>(context, -1, elements) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if(view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.add_ingredient_list, parent, false);
            val name = ButterKnife.findById<TextView>(view, R.id.list_ingredient_name)
            val suffix = ButterKnife.findById<TextView>(view, R.id.list_suffix)
            val ingredient = elements[position]
            suffix.text= ingredient.amount
            name.text = ingredient.ingredient
        }
        return view
    }
}