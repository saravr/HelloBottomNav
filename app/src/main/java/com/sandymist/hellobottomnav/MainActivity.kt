package com.sandymist.hellobottomnav

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

data class FragmentItem(
    val id: Int,
    val label: String,
    val iconRes: Int,
    val fragmentClass: Class<out Fragment>,
    val scrollToTopOnTap: Boolean = false
)

class MainActivity : AppCompatActivity() {
    private val fragmentItemList = mutableListOf<FragmentItem>()
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentItemList.addAll(
            listOf(
                FragmentItem(0, "Home", R.drawable.ic_home_selector, HomeFragment::class.java),
                FragmentItem(1, "Settings", R.drawable.ic_settings_selector, SettingsFragment::class.java),
            )
        )

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        //bottomNavigationView?.itemIconTintList = null
        bottomNavigationView?.menu?.apply {
            fragmentItemList.forEachIndexed { index, fragmentItem ->
                add(Menu.NONE, fragmentItem.id, index, fragmentItem.label)
                    .setIcon(fragmentItem.iconRes)
            }
        }

        bottomNavigationView?.setOnItemSelectedListener {
            fragmentItemList.getOrNull(it.itemId)?.let { fragment ->
                Log.e(TAG, "++++ LOADING FRAG: $fragment")
                loadFragment(fragmentItemList.indexOf(fragment))
            }
            true
        }

        bottomNavigationView?.menu?.get(1)?.itemId?.let {
            bottomNavigationView.selectedItemId = it
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

        if (currentFragment == nextFragment) {
            return
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

    companion object {
        private const val TAG = "MainActivity"
    }
}