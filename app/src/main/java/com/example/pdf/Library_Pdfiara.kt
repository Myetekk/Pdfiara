package com.example.pdf

import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths


class TextBox {
    var text: String = ""  // text textboxa XD
    var x: Int = 70  // współrzędna X
    var y: Int = 700  // współrzędna Y
    var location_in_code: Int = 0  // index textboxa w kodzie pdf
}



class ImageBox {
    lateinit var image_bytes: ByteArray  // obraz w formi bitowej
    lateinit var image_name: String
    var x: Int = 70  // współrzędna X
    var y: Int = 700  // współrzędna Y
    var image_width: Int = 1  // szerokość obrazu
    var image_height: Int = 1  // wysokość obrazu
    var image_scale_x: Int = 100  // skalowanie obrazu w X
    var image_scale_y: Int = 100  // skalowanie obrazu w Y
    var location_in_code: Int = 0  // index textboxa w kodzie pdf
}



class Pages {
    var index: Int = 0  // numeracja stron
    var texts: MutableList<TextBox> = ArrayList()    // lista zawierająca wszystkie teksty na danej stronie
    var images: MutableList<ImageBox> = ArrayList()    // lista zawierająca wszystkie obrazy na danej stronie
    var location_in_code: Int = 0  // index deklaracji strony w kodzie pdf

}




















class PdfGenerator {
    var fileName = ""  // nazwa pliku na którym operujemy

    private var object_counter = 1  // aktualny index obiektu
    private var root_location = 0  // lokalizacja obiektu nadrzędnego
    private var pages: MutableList<Pages> = ArrayList()  // lista stron
    private var page_counter = 1  // zapisywanie indexu strony
    private var image_counter = 1  // zliczanie ilości obrazów (potrzebne jedynie do ich nazewnictwa w pliku pdf)










    fun addPage(): Pages {    // dodanie nowej strony do pliku
        val page = Pages()  // stworzenie strony - obiektu klasy Pages

        page.index = page_counter++  // przypisanie numeru strony danej stronie
        pages.add(page)  // dodanie strony do listy stron

        return page  // zwrócenie utworzonej strony (żeby można było się odnosić do konkretnych stron)
    }










    fun addText(page: Pages, text: String, x: Int, y: Int) {    // dodawanie tekstu do podanej strony
        val newText = TextBox()  // tworzymy nowy obiekt tekstowy - klasy TextBox
        newText.text = text  // ustawiamy jego tekst
        newText.x = x  // ustawiamy jego współrzędną X
        newText.y = y  // ustawiamy jego współrzędną Y

        page.texts.add(newText)  // dodajemy tekstu do listy
    }





    fun addImage(page: Pages, imageFilePath: File, x: Int, y: Int) {    // dodawanie obrazu bez skalowania
        val imageBytes: ByteArray = Files.readAllBytes(Paths.get(imageFilePath.path))  // zapisanie obrazu w formie binarnej
        val options = BitmapFactory.Options();   options.inJustDecodeBounds = true;   BitmapFactory.decodeFile(File(imageFilePath.path).absolutePath, options)  // uzyskanie szerokości i wysokości obrazu

        val imageBox = ImageBox()  // tworzymy nowy obiekt obrazowy - klasy ImageBox
        imageBox.image_bytes = imageBytes  // przypisanie obrazu w formie binarnej
        imageBox.image_name = "Image$image_counter";   image_counter++  // przypisanie nazwy obrazu w kodzie pdf
        imageBox.x = x  // przypisanie współrzędnej X
        imageBox.y = y  // przypisanie współrzędnej Y
        imageBox.image_width = options.outWidth  // przypisanie szerokości obrazu
        imageBox.image_height = options.outHeight  // przypisanie wysokości obrazu


        page.images.add(imageBox)  // dodanie obrazu do listy
    }

    fun addImage(page: Pages, imageFilePath: File, x: Int, y: Int, scale_x: Int, scale_y: Int) {    // dodawanie obrazu ze skalowaniem
        val imageBytes: ByteArray = Files.readAllBytes(Paths.get(imageFilePath.path))  // zapisanie obrazu w formie binarnej
        val options = BitmapFactory.Options(); options.inJustDecodeBounds = true; BitmapFactory.decodeFile(File(imageFilePath.path).absolutePath, options)  // uzyskanie szerokości i wysokości obrazu

        val imageBox = ImageBox()  // tworzymy nowy obiekt obrazowy - klasy ImageBox
        imageBox.image_bytes = imageBytes  // przypisanie obrazu w formie binarnej
        imageBox.image_name = "Image$image_counter";   image_counter++  // przypisanie nazwy obrazu w kodzie pdf
        imageBox.x = x  // przypisanie współrzędnej X
        imageBox.y = y  // przypisanie współrzędnej Y
        imageBox.image_width = options.outWidth  // przypisanie szerokości obrazu
        imageBox.image_height = options.outHeight  // przypisanie wysokości obrazu
        imageBox.image_scale_x = scale_x  // przypisanie skali obrazu w X
        imageBox.image_scale_y = scale_y  // przypisanie skali obrazu w Y


        page.images.add(imageBox)  // dodanie obrazu do listy
    }










    fun savePDF() {
        addHeader()  // dopisanie nagłówek pliku PDF

        saveThings()    // dopisanie wszystkich tekstów, obrazów i grafiki wektorowej, oraz zapisanie roota

        addTrailer()    // dopisanie stopki  pliku PDF
    }










    private fun addHeader() {    // dodanie nagłówka pliku PDF
        val filePath = File(  // lokalizacja pliku PDF
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )

        try {
            val outputStream = FileOutputStream(filePath, false)
            outputStream.write("%PDF-1.7 \n\n\n".toByteArray())  // nagłówek pliku PDF

            outputStream.close()

        }catch (e: IOException) {
            e.printStackTrace()
        }
    }










    private fun addTextToPDF(textBox: TextBox) {    // dodanie tekstu do pliku
        val filePath = File(  // lokalizacja pliku PDF
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )

        try {
            val outputStream = FileOutputStream(filePath, true)

            // dopisanie tekstu
            outputStream.write("$object_counter 0 obj\n".toByteArray())
            outputStream.write("<< /Length 44 >>\n".toByteArray())
            outputStream.write("stream\n".toByteArray())
            outputStream.write("BT /F1 12 Tf ${textBox.x} ${textBox.y} Td (${textBox.text}) Tj ET \n".toByteArray())
            outputStream.write("endstream \n".toByteArray())
            outputStream.write("endobj \n\n\n".toByteArray())
            textBox.location_in_code = object_counter  // zapisanie indexu tego tekstu
            object_counter++

            outputStream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }





    private fun addImageToPDF(image: ImageBox){
        val filePath = File(  // lokalizacja pliku PDF
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )

        try {
            val outputStream = FileOutputStream(filePath, true)

            // dopisanie obrazu
            outputStream.write("$object_counter 0 obj\n".toByteArray())
            outputStream.write("<< /Type /XObject /Subtype /Image /Name /${image.image_name} \n".toByteArray())
            outputStream.write("/Width ${image.image_width} /Height ${image.image_height} /ColorSpace /DeviceRGB /BitsPerComponent 8 /Filter /DCTDecode /Length ${image.image_bytes.size} >>\n".toByteArray())
            outputStream.write("stream\n".toByteArray())
            outputStream.write(image.image_bytes)
            outputStream.write("endstream \n".toByteArray())
            outputStream.write("endobj \n\n\n".toByteArray())
            image.location_in_code = object_counter  // zapisanie indexu tego obrazu
            object_counter++

            // dopisanie właściwości obrazu
            outputStream.write("$object_counter 0 obj \n".toByteArray())
            outputStream.write("<< /Length 989 >> \n".toByteArray())
            outputStream.write("stream \n".toByteArray())
            outputStream.write("q \n".toByteArray())
            outputStream.write("${image.image_scale_x} 0 0 ${image.image_scale_y} ${image.x} ${image.y} cm \n".toByteArray())
            outputStream.write("1 0 0 1 0 0 cm \n".toByteArray())
            outputStream.write("/${image.image_name} Do \n".toByteArray())
            outputStream.write("Q \n".toByteArray())
            outputStream.write("endstream \n".toByteArray())
            outputStream.write("endobj \n\n\n".toByteArray())
            object_counter++

            outputStream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }










    private fun saveThings() {    // zakończenie edycji pliku i zapisanie
        val filePath = File(  // lokalizacja pliku PDF
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )

        try {
            val outputStream = FileOutputStream(filePath, true)

            for (page in pages) {  // zapisanie wszystkich textów, obrazów do pliku
                for (text in page.texts) {    // wpisanie wszystkich tekstów do pliku
                    addTextToPDF(text)
                }

                for (image in page.images) {    // wpisanie wszystkich obrazów do pliku
                    addImageToPDF(image)
                }

                outputStream.write("\n\n\n".toByteArray())  // dla przejrzystości kodu PDF
            }





            // deklaracja roota
            outputStream.write("\n\n\n\n\n".toByteArray())  // dla przejrzystości kodu PDF
            outputStream.write("$object_counter 0 obj\n".toByteArray())
            outputStream.write("<< /Type /Catalog /Pages ${object_counter+1} 0 R >>\n".toByteArray())
            outputStream.write("endobj \n\n\n".toByteArray())
            root_location = object_counter  // zapisanie indexu roota
            object_counter++





            // deklaracja listy stron
            var pages_in_code_index = ""
            for (page in pages) {
                page.location_in_code = object_counter + page.index  // lokalizacja deklaracji danej strony
                pages_in_code_index += "${page.location_in_code} 0 R  "  // dopisanie lokalizacji deklaracji danej strony
            }
            outputStream.write("$object_counter 0 obj\n".toByteArray())
            outputStream.write("<< /Type /Pages /Kids [$pages_in_code_index] /Count ${pages.size} >>\n".toByteArray())
            outputStream.write("endobj \n\n\n".toByteArray())
            object_counter++





            // deklaracje zawartości stron
            for (page in pages) {
                var temp_xObject = "/"  // deklaracja xObjectu strony (dla obrazów)
                var temp_contents = ""  // deklaracja contentu strony

                for (image in page.images) {  // dopisanie deklaracji dla wszystkich obrazów na danej stronie
                    temp_xObject += "${image.image_name} ${image.location_in_code} 0 R "
                    temp_contents += "${image.location_in_code + 1} 0 R  "
                }

                for (text in page.texts) {  // dopisanie deklaracji dla wszystkich tekstów na danej stronie
                    temp_contents += "${text.location_in_code} 0 R  "
                }

                outputStream.write("${page.location_in_code} 0 obj \n".toByteArray())
                outputStream.write("<< /Type /Page /Resources <</XObject <<$temp_xObject>> >> /Parent ${object_counter-page.index} 0 R /Contents [$temp_contents] >> \n".toByteArray())  // dla tekstu
                outputStream.write("endobj \n\n\n".toByteArray())
                object_counter++
            }

            outputStream.close()

        }catch (e: IOException) {
            e.printStackTrace()
        }
    }










    private fun addTrailer() {    // zakończenie edycji pliku
        val filePath = File(  // lokalizacja pliku PDF
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )

        try {
            val outputStream = FileOutputStream(filePath, true)

            outputStream.write("\n\n\n".toByteArray())  // dla przejrzystości kodu PDF

            // deklarujemy ilość obiektów, lokaizacje głównego obiektu i zamykamy plik
            outputStream.write("trailer\n".toByteArray())
            outputStream.write("<< /Size ${object_counter-1} /Root $root_location 0 R >>\n".toByteArray())
            outputStream.write("startxref\n".toByteArray())
            outputStream.write("357\n".toByteArray())
            outputStream.write("%%EOF\n".toByteArray())

            outputStream.close()

        }catch (e: IOException) {
            e.printStackTrace()
        }
    }
}




