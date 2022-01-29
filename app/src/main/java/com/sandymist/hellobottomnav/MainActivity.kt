package com.sandymist.hellobottomnav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val fragmentClassList = mutableListOf<Class<out Fragment>>()
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentClassList.addAll(listOf(HomeFragment::class.java, SettingsFragment::class.java))

        loadFragment(0)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView?.menu?.apply {
            add(Menu.NONE, 0, 0, "Home")
                .setIcon(R.drawable.ic_home)
            add(Menu.NONE, 1, 1, "Settings")
                .setIcon(R.drawable.ic_settings)
        }

        bottomNavigationView?.setOnItemSelectedListener {
            fragmentClassList.getOrNull(it.itemId)?.let { fragment ->
                loadFragment(fragmentClassList.indexOf(fragment))
            }
            false
        }
    }

    private fun loadFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        val fragmentClass = fragmentClassList.getOrNull(index)
        val tag = fragmentClass?.simpleName

        var nextFragment = supportFragmentManager.findFragmentByTag(tag)
        if (nextFragment == null) {
            nextFragment = fragmentClass?.newInstance()?.apply {
                transaction.add(R.id.fragment_container_view_tag, this, tag)
            }
        }

        currentFragment?.let {
            transaction.hide(it)
        }

        currentFragment = nextFragment

        nextFragment?.let {
            transaction.show(it)
        }

        transaction.commit()
    }
}