package com.example.barorganizer.views

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity


object ViewHandler {

    private val fragments: Map<Int, Fragment> = mapOf(
            AddRecipeFragment.NAME to AddRecipeFragment(),
            AllListFragment.NAME to AllListFragment(),
            RecipeFragment.NAME to RecipeFragment(),
            SearchFragment.NAME to SearchFragment()
    )

    fun getFragment(id: Int): Fragment {
        return fragments.getOrElse(id, { fragments.getValue(AllListFragment.NAME) })
    }

    fun runActivity(context: Context, className: Class<out AppCompatActivity>) {
        val myIntent = Intent(context, className)
        context.startActivity(myIntent)
    }
}