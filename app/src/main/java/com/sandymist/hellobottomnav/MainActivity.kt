package com.sandymist.hellobottomnav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val fragmentList = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentList.addAll(listOf(HomeFragment(), SettingsFragment()))

        loadFragment(fragmentList.first())

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView?.menu?.apply {
            add(Menu.NONE, 0, 0, "Home")
                .setIcon(R.drawable.ic_home)
            add(Menu.NONE, 1, 1, "Settings")
                .setIcon(R.drawable.ic_settings)
        }

        bottomNavigationView?.setOnItemSelectedListener {
            fragmentList.getOrNull(it.itemId)?.let { fragment ->
                loadFragment(fragment)
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container_view_tag, fragment)
            commit()
        }
    }
}