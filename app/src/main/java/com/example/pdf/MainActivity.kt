package com.example.pdf

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


val SuperPDFFile = PdfGenerator()
var PagesListName: MutableList<String> = ArrayList()
var PagesList: MutableList<Pages> = ArrayList()
var SelectedPage: Int = 0


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    val fragment_text = Fragment_text()
    val fragment_image = Fragment_image()
    val fragment_vector = Fragment_vector()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_frame_window, fragment_text)
            addToBackStack(null)
            commit()
        }





//        // sterowanie sidebarem
        drawerLayout = findViewById(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()






        val clear = findViewById<ImageButton>(R.id.clear_pdf)
        val save = findViewById<ImageButton>(R.id.save_pdf)

        SuperPDFFile.fileName = "PDFowicz.pdf"
        SuperPDFFile.addPage()


        clear.setOnClickListener{
            SuperPDFFile.clearPDF()
        }
        save.setOnClickListener{
            SuperPDFFile.savePDF()
        }

    }










    // sterowanie itemami w sidebarze
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId){  // instrukcja do przechodzenia pomiędzy stronami
            R.id.choose_text -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.main_frame_window, fragment_text)
                    commit()
                }
            }

            R.id.choose_image -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.main_frame_window, fragment_image)
                    commit()
                }
            }

            R.id.choose_vector -> {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.main_frame_window, fragment_vector)
                    commit()
                }
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

}
