package com.example.barorganizer.views

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.view.MenuItem
import butterknife.ButterKnife

import com.example.barorganizer.R

class AppActivity : AppCompatActivity() {

    val INIT_FRAGMENT = AllListFragment.NAME

    lateinit var navigation: BottomNavigationView

    fun changeView(menuItem: MenuItem): Boolean {
        val id = menuItem.itemId
        if (id != navigation.selectedItemId) {
            replaceFragment(ViewHandler.getFragment(id))
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
        navigation = ButterKnife.findById<BottomNavigationView>(this, R.id.list_navigation)
        navigation.setOnNavigationItemSelectedListener { m -> changeView(m) }
        navigation.selectedItemId = INIT_FRAGMENT
        replaceFragment(ViewHandler.getFragment(INIT_FRAGMENT))
        ButterKnife.bind(this);
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }
}
