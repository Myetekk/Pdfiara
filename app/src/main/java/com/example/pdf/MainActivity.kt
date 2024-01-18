package com.example.pdf

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


var isText = false


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    val fragment_paint = Fragment_paint()
    val fragment_text_to_pdf = Fragment_text_to_pdf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_in_main_activity, fragment_text_to_pdf)
            addToBackStack(null)
            commit()
        }





        // sterowanie sidebarem
        drawerLayout = findViewById(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }





    // sterowanie itemami w sidebarze
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){  // instrukcja do przechodzenia pomiędzy stronami
            R.id.choose_text_pdf -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_in_main_activity, fragment_text_to_pdf)
                    commit()
                }
            }

            R.id.choose_paint -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_in_main_activity, fragment_paint)
                    commit()
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

}
