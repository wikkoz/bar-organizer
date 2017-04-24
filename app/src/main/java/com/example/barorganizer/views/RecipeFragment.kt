package com.example.barorganizer.views

import android.app.Fragment
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.barorganizer.ModulesConfig
import com.example.barorganizer.R
import com.example.barorganizer.database.DatabaseFacade

class RecipeFragment : Fragment() {
    companion object {
        val NAME = R.id.menu_recent
        val KEY = "element"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var db: DatabaseFacade
    private var elementId: Long? = null

    @Nullable
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_recipe, container, false)
        db = ModulesConfig.createSql(activity)
        elementId = savedInstanceState?.getLong(KEY)
        return view
    }
}
