package com.example.barorganizer.views

import android.app.Fragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.barorganizer.ModulesConfig
import com.example.barorganizer.R
import com.example.barorganizer.database.DatabaseFacade
import com.example.barorganizer.database.dto.ListRecipeDto
import com.example.barorganizer.views.adapters.RecipeListAdapter


class AllListFragment : Fragment(){
    companion object {
        val NAME = R.id.menu_list
    }

    lateinit var db: DatabaseFacade

    lateinit var listView: ListView

    lateinit var recipes: List<ListRecipeDto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_list, container, false)
        ButterKnife.bind(activity)
        listView = ButterKnife.findById<ListView>(view, R.id.recipe_list)
        db = ModulesConfig.createSql(activity)
        recipes = db.getAllRecipes()
        val adapter = RecipeListAdapter(activity, recipes)
        listView.adapter = adapter
        listView.setOnItemClickListener({ _, _, position, _ -> onItemClick(position)})
        return view
    }

    private fun onItemClick(position: Int) {
        val recipe = recipes[position]
        val bundle = Bundle()
        bundle.putSerializable(RecipeFragment.KEY, recipe.id)
        val fragment = ViewHandler.getFragment(RecipeFragment.NAME)
        fragment.arguments = bundle
        ViewHandler.replaceFragment(fragmentManager, fragment)
    }
}
