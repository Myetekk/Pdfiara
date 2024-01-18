package com.example.pdf

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.File


class Fragment_text_to_pdf : Fragment() {
    lateinit var textView_text_to_pdf: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_text_to_pdf, container, false)
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val save = view.findViewById<ImageButton>(R.id.save_text_to_pdf)
        val image = view.findViewById<ImageButton>(R.id.save_image_to_pdf)
        val clear = view.findViewById<ImageButton>(R.id.clear_text_to_pdf)





        save.setOnClickListener{
            val newFile = PdfGenerator()
            newFile.fileName = "PDFiara_text.pdf"

//            textView_text_to_pdf = view.findViewById<TextView>(R.id.textView_text_to_pdf)
//            val content = textView_text_to_pdf.text.toString()
            val content_text = "W tym tygodniu zatwierdzono slawetny aneks nr. 4. Co to oznacza? To ze w tym momencie mamy klepniete uzbrojenie i wyposazenie dla WSZYSTKICH TRZECH fregat rakietowych budowanych w ramach programu Miecznik. Okej, to nie oznacza 100% wyposazenia i uzbrojenia ale cyferke stojaca bardzo blisko tych 100% ktora uzasadnia stwierdzenie ze dla wszystkich 3 przyszlych fregat mamy uzbrojenie i wyposazenie. Koniec obaw o tym ze Miecznik skonczy jak Slazak 2.0 z gotowymi platformami ale bez pelnego uzbrojenia i wyposazenia, koniec obaw o to ze zamawiajac reszte wyposazenia i uzbrojenia okaze sie ze albo jest ono znacznie drozsze albo 'mamy tylko nowy model'. Przed programem stoja kolejne kroki milowe i kolejne wyzwania, zas przed nami dziennikarzami obowiazek dalszego informowania i przekonywania, tak spoleczenstwa jak i nowej ekipy MON, o tym jak potrzebne MW RP i Polsce sa Mieczniki.   "

            newFile.addText(content_text + content_text)
            newFile.addText("Taka sobie druga linia ")
            newFile.addText("Jakas trzecia linia ")
            newFile.addText("A tu czwarta ")
            newFile.addText("tekst podany ze wspolrzednymi", 300, 730)
            newFile.savePDF()
        }





        image.setOnClickListener{
            val newFile = PdfGenerator()

            newFile.fileName = "PDFiara_image.pdf"

            val imageFilePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/Image.jpg" )

            newFile.addImage( imageFilePath, "Image1", 100, 600)
            newFile.savePDF()
        }





        clear.setOnClickListener{
            textView_text_to_pdf = view.findViewById<TextView>(R.id.textView_text_to_pdf)
            textView_text_to_pdf.text = ""
        }

    }



}