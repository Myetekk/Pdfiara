package com.example.pdf

import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException







private class TextBox {
    var text: String = ""
    //var x: Int = 0
    //var y: Int = 0
}




















class PdfGenerator {
    var fileName = ""

    private var object_counter = 1
    private var page_counter = 1
    private var position = 700
    private var textBoxes = 0
    private var texts : MutableList<TextBox> = ArrayList()










    fun addText(text: String) {
        val newText = TextBox()
        newText.text = text
        texts.add(newText)
    }

//    fun addText(text: String, x: Int, y: Int) {  // przyszła funkcja dodawania tekstu na podanych współrzędnych
//
//    }










    fun savePDF() {
        // zapisywanie wszystkich tekstów
        // wpisanie wszystkich textboxów do pliku
        for (text in texts){
            addTextToPDF(text.text)
        }
        saveText()


        // zapisywanie wszystkich obrazów


        // zapisywanie wszystkich grafik wektorowych


        // zapisanie stopki
        addTrailer()
    }










    private fun saveText() {  // zakończenie edycji tekstu w pliku
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )


        try {
            val outputStream = FileOutputStream(filePath, true)


            // zamknięcie textu z funkcji 'addText'
            outputStream.write("endstream\n".toByteArray())
            outputStream.write("endobj\n".toByteArray())
            object_counter++

            outputStream.write("$object_counter 0 obj\n".toByteArray())
            outputStream.write("<< /Type /Catalog /Pages ${object_counter+1} 0 R >>\n".toByteArray())
            outputStream.write("endobj\n".toByteArray())
            object_counter++

            var temp_kids = ""
            for (page_number in 1 .. page_counter){
                temp_kids += "${object_counter+page_number} 0 R "
            }
            outputStream.write("$object_counter 0 obj\n".toByteArray())
            outputStream.write("<< /Type /Pages /Kids [$temp_kids] /Count $page_counter >>\n".toByteArray())
            outputStream.write("endobj\n".toByteArray())
            object_counter++

            for (page_number in 1 .. page_counter){
                outputStream.write("$object_counter 0 obj \n".toByteArray())
                outputStream.write("<< /Type /Page /Parent ${object_counter-page_number} 0 R /Contents $page_number 0 R >> \n".toByteArray())
                outputStream.write("endobj \n".toByteArray())
                object_counter++
            }


            outputStream.close()

        }catch (e: IOException) {
            e.printStackTrace()
        }
    }










    private fun addTextToPDF(text_: String) {  // dodanie tekstu do pliku
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )


        try {
            val outputStream = FileOutputStream(filePath, true)

            var text = text_
            text = text.trim()
            var text_temp: String
            var index_space = 0

            if (textBoxes == 0){  // zwinięcie do if żeby można wywoływać funkcję 'addText' wiele razy
                outputStream.write("%PDF-1.7 \n".toByteArray())
                outputStream.write("$object_counter 0 obj\n".toByteArray())
                outputStream.write("<< /Length 44 >>\n".toByteArray())
                outputStream.write("stream\n".toByteArray())
            }
            textBoxes++

            while (text.length >= 80){
                if (position <= 90){  // gdy tekst dojdzie wystarczająco nisko tworzymy nową stronę
                    // zamykamy poprzednią strone
                    outputStream.write("endstream\n".toByteArray())
                    outputStream.write("endobj\n".toByteArray())
                    object_counter++

                    // otwieramy nową strone
                    outputStream.write("$object_counter 0 obj\n".toByteArray())
                    outputStream.write("<< /Length 44 >>\n".toByteArray())
                    outputStream.write("stream\n".toByteArray())

                    position = 700  // ustawiamy pozycję na górę strony
                    page_counter++
                    textBoxes++
                }

                loop@ for (i in 80 downTo 0){  // szukamy ostatniej spacji
                    if (text[i].toString().equals(" ")){
                        index_space = i
                        break@loop
                    }
                }

                // wycinamy i wypisujemy tekst o długości 80-odległość do ostatniej spacji
                text_temp = text.substring(0, index_space)
                outputStream.write("BT /F1 12 Tf 70 $position Td ($text_temp) Tj ET \n".toByteArray())
                text = text.substring(index_space)
                text = text.trim()
                position -= 30
            }

            outputStream.write("BT /F1 12 Tf 70 $position Td ($text) Tj ET \n".toByteArray())  // wypisujemy reszte stringa która jest krótsza od 80
            position -= 30


            outputStream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }










    private fun addTrailer() {
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )

        try {
            val outputStream = FileOutputStream(filePath, true)

            outputStream.write("trailer\n".toByteArray())
            outputStream.write("<< /Size ${object_counter-1} /Root ${page_counter+1} 0 R >>\n".toByteArray())
            outputStream.write("startxref\n".toByteArray())
            outputStream.write("357\n".toByteArray())
            outputStream.write("%%EOF\n".toByteArray())

            outputStream.close()

        }catch (e: IOException) {
            e.printStackTrace()
        }
    }
}




