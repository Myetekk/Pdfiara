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
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.Version.getInstance
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter


var isText = false
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        val canvasView = findViewById<CanvasView>(R.id.canvasView)
        val addText = findViewById<Button>(R.id.addText)
        val clear = findViewById<Button>(R.id.clear)
        val save = findViewById<Button>(R.id.save)
        addText.setOnClickListener {
            isText = true
        }
        clear.setOnClickListener {
            canvasView.clearCanvas()
        }
        save.setOnClickListener {
            canvasView.createPDF()
        }





        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val canvasView = findViewById<CanvasView>(R.id.canvasView)

        when(item.itemId){
//            R.id.addText -> isText = true
//            R.id.clear -> canvasView.clearCanvas()
//            R.id.save -> canvasView.createPDF()
        }
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }
}





class CanvasView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private val textFields = ArrayList<TextField>()
    private val path: Path = Path()
    private val paint = Paint()
    private var selectedTextField: TextField? = null
    private var offsetX = 0f
    private var offsetY = 0f
    private var lastClickTime: Long = 0
    private val DOUBLE_CLICK_TIME = 300
    init {
        isFocusable = true
        isFocusableInTouchMode = true
        paintSettings()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
        for (textField in textFields) {
            val paint = Paint().apply {
                color = textField.textColor
                textSize = textField.textSize
            }
            canvas.drawText(textField.text, textField.x, textField.y, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                selectedTextField = findTextFieldAtPosition(x, y)
                val clickTime = System.currentTimeMillis()
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME) {
                    val x = event.x
                    val y = event.y
                    selectedTextField = findTextFieldAtPosition(x, y)
                    if (selectedTextField != null) {
                        startEditingText(selectedTextField!!)
                    }
                }
                lastClickTime = clickTime
            }
        }
        if(isText){
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x
                    val y = event.y
                    val textField = TextField("Nowy tekst", x, y, 100f, Color.BLACK)
                    textFields.add(textField)
                    selectedTextField = textField

                }
            }
            isText = false
        }
        else if(selectedTextField == null){
            val pointX = event.x
            val pointY = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN ->
                    path.moveTo(pointX, pointY)
                MotionEvent.ACTION_MOVE ->
                    path.lineTo(pointX, pointY)
                else -> return false
            }
        }
        else {
            if (selectedTextField != null) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val x = event.x
                        val y = event.y
                        selectedTextField = findTextFieldAtPosition(x, y)
                        if (selectedTextField != null) {
                            offsetX = selectedTextField!!.x - x
                            offsetY = selectedTextField!!.y - y
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        selectedTextField?.let { textField ->
                            textField.x = event.x + offsetX
                            textField.y = event.y + offsetY
                            invalidate()
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        selectedTextField = null
                    }
                }
            }
        }
        invalidate()
        return true
    }

    private fun paintSettings() {
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLUE
        paint.strokeWidth = 10f
    }
    private fun findTextFieldAtPosition(x: Float, y: Float): TextField? {
        for (textField in textFields) {
            if (x >= textField.x && x <= textField.x + measureTextWidth(textField) &&
                y >= textField.y - textField.textSize && y <= textField.y
            ) {
                return textField
            }
        }
        return null
    }
    private fun measureTextWidth(textField: TextField): Float {
        val paint = Paint().apply {
            textSize = textField.textSize
        }
        return paint.measureText(textField.text)
    }
    private fun startEditingText(textField: TextField) {
        val alertDialog = AlertDialog.Builder(context)
        val editText = EditText(context)
        alertDialog.setView(editText)
        alertDialog.setPositiveButton("Zapisz") { _, _ ->
            val newText = editText.text.toString()
            textField.text = newText
            invalidate()
        }
        alertDialog.setNegativeButton("Anuluj") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
        editText.setText(textField.text)
    }
    fun clearCanvas() {
        path.reset()
        textFields.clear()
        invalidate()

    }
    fun createPDF() {
        val pageWidth = 612f
        val pageHeight = 792f


        val document = Document()


        val pdfFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/" + "test3" +".pdf"

        try {
            val writer = PdfWriter.getInstance(document, FileOutputStream(pdfFile))

            document.open()

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            draw(canvas)

            val scale = minOf(pageWidth / width, pageHeight / height)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, (width * scale).toInt(), (height * scale).toInt(), false)

            val image = Image.getInstance(scaledBitmap.convertToByteArray(), true)
            document.add(image)

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            document.close()
        }
    }

}

fun Bitmap.convertToByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}


class TextField(
    var text: String,
    var x: Float,
    var y: Float,
    var textSize: Float,
    var textColor: Int
)