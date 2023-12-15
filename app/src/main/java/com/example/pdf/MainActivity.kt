package com.example.pdf

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.os.Environment
import android.util.AttributeSet
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream


var isText = false

//val libraryPdfiara = Library_Pdfiara()


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    val fragment_paint = Fragment_paint()
    val fragment_text_to_pdf = Fragment_text_to_pdf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_in_main_activity, fragment_paint)
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
