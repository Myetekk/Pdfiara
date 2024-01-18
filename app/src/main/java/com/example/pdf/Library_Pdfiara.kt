package com.example.pdf

import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


private class TextBox {
    var text: String = ""
    var x: Int = 70  // współrzędna X
    var y: Int = 700  // współrzędna Y
    var withCordinates: Boolean = false    // określa czy współrzędne mają się określić automatycznie czy zostały wprowadzone ręcznie
}
private class ImageBox {
    lateinit var image_bytes: ByteArray  // obraz w formi bitowej
    lateinit var image_name: String
    var x: Int = 70  // współrzędna X
    var y: Int = 700  // współrzędna Y
    var image_width: Int = 700  // szerokość obrazu
    var image_height: Int = 700  // wysokość obrazu
    var location_in_code: Int = 0
}




















class PdfGenerator {
    var fileName = ""

    private var object_counter = 1
    private var page_counter = 1
    private var position_x = 70    // pozycja x tekstu na stronie
    private var position_y = 700    // pozycja y tekstu na stronie
    private var textBoxes = 0    // licznik textBoxów, potrzebny tylko aby zacząć plik
    private var texts: MutableList<TextBox> = ArrayList()    // lista textBoxów zawierająca wszystkie textBoxy XD
    private var images: MutableList<ImageBox> = ArrayList()    // lista ImageBoxów zawierająca wszystkie ImageBoxy XD
    private var root = 0  // lokalizacja obiektu nadrzędnego










    fun addText(text: String) {    // funkcja dodawania tekstu bez współrzędnych
        val newText = TextBox()    // tworzymy nowy obiekt klasy textBox
        newText.text = text    // ustawiamy jego text
        newText.withCordinates = false    // ustalamy że współrzędne mają się określić automatycznie

        texts.add(newText)    // dodajemy nowego textBoxa do listy textBoxów
    }
    fun addText(text: String, x: Int, y: Int) {    // funkcja dodawania tekstu z współrzędnymi
        val newText = TextBox()    // tworzymy nowy obiekt klasy textBox
        newText.text = text    // ustawiamy jego text
        newText.x = x    // ustawiamy jego współrzędną X
        newText.y = y    // ustawiamy jego współrzędną Y
        newText.withCordinates = true    // ustalamy że współrzędne zostały określone ręcznie

        texts.add(newText)    // dodajemy nowego textBoxa do listy textBoxów
    }



    fun addImage(imageFilePath: File, imageName: String, x: Int, y: Int) {
        val imageBytes: ByteArray = Files.readAllBytes(Paths.get(imageFilePath.path))  // obraz w formi binarnej

        // Create a Text Box for the image
        val imagetBox = ImageBox()
        imagetBox.image_bytes = imageBytes
        imagetBox.image_name = imageName
        imagetBox.x = x  // przypisanie współrzędnej X
        imagetBox.y = y  // przypisanie współrzędnej Y
//        imagetBox.withCordinates = true
//        imagetBox.type = "image"

        // dobranie się do szerokości i wysokości obrazu
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(imageFilePath.path).absolutePath, options)

        imagetBox.image_width = options.outWidth  // przypisanie szerokości obrazu
        imagetBox.image_height = options.outHeight  // przypisanie wysokości obrazu

        images.add(imagetBox)
    }











    fun savePDF() {
        addHeader()  // nagłówek pliku PDF

        saveThings()    // dodanie wszystkich tekstów, obrazów i grafiki wektorowej, oraz zapisanie roota

        addTrailer()    // dodanie stopki całego pliku PDF
    }










    private fun addHeader() {
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )

        try {
            val outputStream = FileOutputStream(filePath, false)
            outputStream.write("%PDF-1.7 \n".toByteArray())

        }catch (e: IOException) {
            e.printStackTrace()
        }
    }











    private fun addTextToPDF(textBox_: TextBox) {    // dodanie tekstu do pliku
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )


        try {
            val outputStream = FileOutputStream(filePath, true)

            var text = textBox_.text    // przypisujemy text danego textBoxa do zmiennej lokalnej
            position_x = textBox_.x    // przypisujemy współrzędną X danego textBoxa do zmiennej lokalnej
            if (textBox_.withCordinates) position_y = textBox_.y    // jeśli użytkownik określił współrzędne ręcznie to przypisujemy współrzędną X danego textBoxa do zmiennej lokalnej
            text = text.trim()

            var text_temp: String
            var index_space = 0

            if (textBoxes == 0){    // zwinięcie do if żeby można wywoływać funkcję 'addText' wiele razy
//                outputStream.write("%PDF-1.7 \n".toByteArray())
                outputStream.write("$object_counter 0 obj\n".toByteArray())
                outputStream.write("<< /Length 44 >>\n".toByteArray())
                outputStream.write("stream\n".toByteArray())
            }
            textBoxes++

            while (text.length >= 80){    // gdy tekst jest dłuższy niż 80 znaków zawijamy go do nowej lini
                if (position_y <= 90){    // gdy tekst dojdzie wystarczająco nisko tworzymy nową stronę
                    // zamykamy poprzednią strone
                    outputStream.write("endstream \n".toByteArray())
                    outputStream.write("endobj \n\n\n".toByteArray())
                    object_counter++

                    // otwieramy nową strone
                    outputStream.write("$object_counter 0 obj\n".toByteArray())
                    outputStream.write("<< /Length 44 >>\n".toByteArray())
                    outputStream.write("stream\n".toByteArray())

                    position_y = 700    // ustawiamy pozycję na górę strony
                    page_counter++
                    textBoxes++
                }

                loop@ for (i in 80 downTo 0){    // szukamy ostatniej spacji żeby nie ucinać słowa w połowie
                    if (text[i].toString().equals(" ")){
                        index_space = i
                        break@loop
                    }
                }

                // wycinamy i wypisujemy tekst o długości 80-odległość do ostatniej spacji
                text_temp = text.substring(0, index_space)    // wycinamy stringa od początku do indexu ostatniej spacji przed przekroczeniem 80 znaków w linijce
                outputStream.write("BT /F1 12 Tf $position_x $position_y Td ($text_temp) Tj ET \n".toByteArray())    // i wypisujemy go
                text = text.substring(index_space)    // wycinamy reszte stringa i nadpisujemy głównego stringa
                text = text.trim()
                position_y -= 30    // ustawiamy współrzędną Y tak aby dać efekt zejścia liniję niżej
            }

            outputStream.write("BT /F1 12 Tf $position_x $position_y Td ($text) Tj ET \n".toByteArray())    // wypisujemy reszte stringa która jest krótsza od 80
            position_y -= 30    // ustawiamy współrzędną Y tak aby dać efekt zejścia liniję niżej


            outputStream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }










//    private fun addImageToPDF(imageBytes: ByteArray, imageName: String, width: Int, height:Int){
    private fun addImageToPDF(image: ImageBox){
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )

        try {
            val outputStream = FileOutputStream(filePath, true)

            outputStream.write("$object_counter 0 obj\n".toByteArray())
            outputStream.write("<< /Type /XObject /Subtype /Image /Name /${image.image_name} \n".toByteArray())
            outputStream.write("/Width ${image.image_width} /Height ${image.image_height} /ColorSpace /DeviceRGB /BitsPerComponent 8 /Filter /DCTDecode /Length ${image.image_bytes.size} >>\n".toByteArray())
            outputStream.write("stream\n".toByteArray())
            outputStream.write(image.image_bytes)
            outputStream.write("endstream \n".toByteArray())
            outputStream.write("endobj \n\n\n".toByteArray())
            image.location_in_code = object_counter
            object_counter++

            outputStream.write("$object_counter 0 obj \n".toByteArray())
            outputStream.write("<< /Length 989 >> \n".toByteArray())
            outputStream.write("stream \n".toByteArray())
            outputStream.write("q \n".toByteArray())
            outputStream.write("100 0 0 100 ${image.x} ${image.y} cm \n".toByteArray())
            outputStream.write("1 0 0 1 0 0 cm \n".toByteArray())
            outputStream.write("/${image.image_name} Do \n".toByteArray())
            outputStream.write("Q \n".toByteArray())
            outputStream.write("endstream \n".toByteArray())
            outputStream.write("endobj \n\n\n".toByteArray())
            object_counter++


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }










    private fun saveThings() {    // zakończenie edycji tekstu w pliku
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )


        try {
            val outputStream = FileOutputStream(filePath, true)

            for (image in images){    // wpisanie wszystkich imageboxów do pliku
//                addImageToPDF(image.image_bytes, image.image_name, image.image_width, image.image_height)
                addImageToPDF(image)
            }

            for (text in texts){    // wpisanie wszystkich textboxów do pliku
                addTextToPDF(text)

                // zamknięcie textu z funkcji 'addText'
                outputStream.write("endstream \n".toByteArray())
                outputStream.write("endobj \n\n\n".toByteArray())
                object_counter++
            }



            // deklarujemy gdzie znajdują się deklaracje stron
            root = object_counter
            outputStream.write("$object_counter 0 obj\n".toByteArray())
            outputStream.write("<< /Type /Catalog /Pages ${object_counter+1} 0 R >>\n".toByteArray())
            outputStream.write("endobj \n\n\n".toByteArray())
            object_counter++

            // deklarujemy gdzie znajdują się deklaracje zawartości stron
            var temp_kids = ""
            for (page_number in 1 .. page_counter){
                temp_kids += "${object_counter+page_number} 0 R "
            }
            outputStream.write("$object_counter 0 obj\n".toByteArray())
            outputStream.write("<< /Type /Pages /Kids [$temp_kids] /Count $page_counter >>\n".toByteArray())
            outputStream.write("endobj \n\n\n".toByteArray())
            object_counter++

            // deklaracje zawartości stron
            var temp_xObject = ""
            var temp_contents = ""
            for (image in images){
                temp_xObject += "${image.image_name} ${image.location_in_code} 0 R "
                temp_contents += "${image.location_in_code + 1} 0 R "
            }
            for (page_number in 1 .. page_counter){
                outputStream.write("$object_counter 0 obj \n".toByteArray())
//                outputStream.write("<< /Type /Page /Resources <</XObject <</$temp_xObject>> >> /Parent ${object_counter-page_number} 0 R /Contents [${page_number} 0 R] >> \n".toByteArray())  // dla tekstu
                outputStream.write("<< /Type /Page /Resources <</XObject <</$temp_xObject>> >> /Parent ${object_counter-page_number} 0 R /Contents [$temp_contents 0 R] >> \n".toByteArray())  // dla obrazu
                outputStream.write("endobj \n\n\n".toByteArray())
                object_counter++
            }


            outputStream.close()

        }catch (e: IOException) {
            e.printStackTrace()
        }
    }










    private fun addTrailer() {    // zakończenie edycji pliku
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )

        try {
            val outputStream = FileOutputStream(filePath, true)

            // deklarujemy ilość obiektów, lokaizacje głównego obiektu i zamykamy plik
            outputStream.write("trailer\n".toByteArray())
            outputStream.write("<< /Size ${object_counter-1} /Root $root 0 R >>\n".toByteArray())
            outputStream.write("startxref\n".toByteArray())
            outputStream.write("357\n".toByteArray())
            outputStream.write("%%EOF\n".toByteArray())

            outputStream.close()

        }catch (e: IOException) {
            e.printStackTrace()
        }
    }
}




