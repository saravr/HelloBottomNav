package com.sandymist.hellobottomnav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

data class FragmentItem(
    val label: String,
    val iconRes: Int,
    val fragmentClass: Class<out Fragment>
)

class MainActivity : AppCompatActivity() {
    private val fragmentItemList = mutableListOf<FragmentItem>()
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentItemList.addAll(
            listOf(
                FragmentItem("Home", R.drawable.ic_home, HomeFragment::class.java),
                FragmentItem("Settings", R.drawable.ic_settings, SettingsFragment::class.java)
            )
        )

        loadFragment(0)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNavigationView?.menu?.apply {
            fragmentItemList.forEachIndexed { index, fragmentItem ->
                add(Menu.NONE, index, index, fragmentItem.label)
                    .setIcon(fragmentItem.iconRes)
            }
        }

        bottomNavigationView?.setOnItemSelectedListener {
            fragmentItemList.getOrNull(it.itemId)?.let { fragment ->
                loadFragment(fragmentItemList.indexOf(fragment))
            }
            false
        }
    }

    private fun loadFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        val fragmentClass = fragmentItemList.getOrNull(index)?.fragmentClass
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