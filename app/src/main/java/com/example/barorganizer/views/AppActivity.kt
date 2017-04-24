package com.example.barorganizer.views

import android.app.Fragment
import android.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import butterknife.ButterKnife

import com.example.barorganizer.R

class AppActivity : AppCompatActivity() {

    companion object {
    }

    val INIT_FRAGMENT = AllListFragment.NAME

    lateinit var navigation: BottomNavigationView

    fun changeView(menuItem: MenuItem): Boolean {
        val id = menuItem.itemId
        if (id != navigation.selectedItemId) {
            ViewHandler.replaceFragment(fragmentManager, ViewHandler.getFragment(id))
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        navigation = ButterKnife.findById<BottomNavigationView>(this, R.id.list_navigation)
        navigation.setOnNavigationItemSelectedListener { m -> changeView(m) }
        navigation.selectedItemId = INIT_FRAGMENT
        ViewHandler.replaceFragment(fragmentManager, ViewHandler.getFragment(INIT_FRAGMENT))
        ButterKnife.bind(this)
    }

}
