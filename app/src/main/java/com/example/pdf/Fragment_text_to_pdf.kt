package com.example.pdf

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.File


class Fragment_text_to_pdf : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_text_to_pdf, container, false)
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text_1 = view.findViewById<ImageButton>(R.id.add_text_1)
        val image_1 = view.findViewById<ImageButton>(R.id.add_image_1)
        val vector_graphics_1 = view.findViewById<ImageButton>(R.id.add_vector_graphics_1)

        val text_2 = view.findViewById<ImageButton>(R.id.add_text_2)
        val image_2 = view.findViewById<ImageButton>(R.id.add_image_2)
        val vector_graphics_2 = view.findViewById<ImageButton>(R.id.add_vector_graphics_2)

        val text_3 = view.findViewById<ImageButton>(R.id.add_text_3)
        val image_3 = view.findViewById<ImageButton>(R.id.add_image_3)
        val vector_graphics_3 = view.findViewById<ImageButton>(R.id.add_vector_graphics_3)

        val save = view.findViewById<ImageButton>(R.id.save_pdf)





        val newFile = PdfGenerator()
        newFile.fileName = "PDFiara.pdf"

        val page1 = newFile.addPage()
        val page2 = newFile.addPage()
        val page3 = newFile.addPage()





        text_1.setOnClickListener{
            newFile.addText(page1, "Tekst 1 na stronie 1 ", 370, 650)
        }
        image_1.setOnClickListener{
            val imageFilePath1 = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Image1.jpg" )
            newFile.addImage(page1, imageFilePath1, 50, 150, 300, 300)
        }
        vector_graphics_1.setOnClickListener{
            newFile.addLine(page1, 50, 700, 100, 700)
            newFile.addTriangle(page1, 200, 550, 100)
            newFile.addSquare(page1, 500, 300, 100)
        }



        text_2.setOnClickListener{
            newFile.addText(page2, "Tekst 1 na stronie 2 ", 100, 200)
        }
        image_2.setOnClickListener{
            val imageFilePath2 = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Image2.jpg" )
            newFile.addImage(page2, imageFilePath2, 315, 540)
        }
        vector_graphics_2.setOnClickListener{
            newFile.addLine(page2, 20, 20, 580, 20)
        }



        text_3.setOnClickListener{
            newFile.addText(page3, "Tekst 1 na stronie 3 ", 50, 600)
            newFile.addText(page3, "Tekst 2 na stronie 3 ", 500, 450)
            newFile.addText(page3, "Tekst 3 na stronie 3 ", 350, 350)
            newFile.addText(page3, "Tekst 4 na stronie 3 ", 400, 150)
        }
        image_3.setOnClickListener{
            val imageFilePath3 = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Image3.jpg" )
            newFile.addImage(page3, imageFilePath3, 315, 540, 150, 150)
            val imageFilePath4 = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Image4.jpg" )
            newFile.addImage(page3, imageFilePath4, 50, 50, 250, 250)
        }
        vector_graphics_3.setOnClickListener{
            newFile.addCircle(page3, 130, 600, 100)
        }



        save.setOnClickListener{

//            for (item: Int in 1..200){
//                val x = newFile.addPage()
//                val y = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Image4.jpg" )
//                newFile.addText( x, "Jakis tekst: $item", 50, 600)
//                newFile.addImage(x, y, 315, 540, 150, 150)
//            }

            newFile.savePDF()

        }

    }



}