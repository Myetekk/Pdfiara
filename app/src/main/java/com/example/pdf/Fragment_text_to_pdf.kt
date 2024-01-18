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

        val text = view.findViewById<ImageButton>(R.id.add_text)
        val image = view.findViewById<ImageButton>(R.id.add_image)
        val save = view.findViewById<ImageButton>(R.id.save_pdf)


        val newFile = PdfGenerator()
        newFile.fileName = "PDFiara.pdf"

        val page1 = newFile.addPage()
        val page2 = newFile.addPage()
        val page3 = newFile.addPage()





        text.setOnClickListener{
            newFile.addText(page1, "Gapisz mi sie na bebech ", 300, 500)
            newFile.addText(page2, "Wszyscy sie gapia na bebech bo jest kurwa gigantyczny ", 100, 200)
            newFile.addText(page3, "Wszyscy teraz sie popatrzymy ", 50, 600)
            newFile.addText(page3, "I co ", 500, 450)
            newFile.addText(page3, "Gowno ", 250, 300)
            newFile.addText(page3, "Z czym masz problem? ", 400, 150)
        }



        image.setOnClickListener{
            val imageFilePath1 = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Image1.jpg" )
            newFile.addImage(page1, imageFilePath1, 315, 540, 150, 150)

            val imageFilePath2 = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Image2.jpg" )
            newFile.addImage(page2, imageFilePath2, 205, 240)
        }



        save.setOnClickListener{
            newFile.savePDF()
        }

    }



}