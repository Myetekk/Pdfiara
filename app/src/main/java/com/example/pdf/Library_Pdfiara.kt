package com.example.pdf

import android.graphics.BitmapFactory
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths



class Pages {
    var index: Int = 0  // numeracja stron
    var texts: MutableList<TextBox> = ArrayList()    // lista zawierająca wszystkie teksty na danej stronie
    var images: MutableList<ImageBox> = ArrayList()    // lista zawierająca wszystkie obrazy na danej stronie
    var vectors: MutableList<VectorGraphicBox> = ArrayList()    // lista zawierająca wszystkie obrazy na danej stronie
    var location_in_code: Int = 0  // index deklaracji strony w kodzie pdf
}



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
    var location_in_code: Int = 0  // index imageboxa w kodzie pdf
}



class VectorGraphicBox {
    var type: String = ""  // typ grafiki: line, circle
    var x1: Int = 200  // współrzędna X1
    var y1: Int = 600  // współrzędna Y1
    var x2: Int = 200  // współrzędna X2
    var y2: Int = 600  // współrzędna Y2
    var a: Int = 100  // dla trójkąta i kwadratu długość boku, a dla koła promień
    var location_in_code: Int = 0  // index vectorboxa w kodzie pdf
}




















class PdfGenerator {
    var fileName = ""  // nazwa pliku na którym operujemy

    private var object_counter = 1  // aktualny index obiektu
    private var root_location = 0  // lokalizacja obiektu nadrzędnego
    private var pages: MutableList<Pages> = ArrayList()  // lista stron
    private var page_counter = 1  // zapisywanie indexu strony
    private var image_counter = 1  // zliczanie ilości obrazów (potrzebne jedynie do ich nazewnictwa w pliku pdf)










    fun addPage() {    // dodanie nowej strony do pliku
        val page = Pages()  // stworzenie strony - obiektu klasy Pages

        page.index = page_counter++  // przypisanie numeru strony danej stronie
        pages.add(page)  // dodanie strony do listy stron

        PagesListName.add("Page ${page.index}")
        PagesList.add(page)

//        return page  // zwrócenie utworzonej strony (żeby można było się odnosić do konkretnych stron)
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





    fun addLine(page: Pages, x1: Int, y1: Int, x2: Int, y2: Int) {    // dodawanie lini
        val vectorGraphicBox = VectorGraphicBox()  // tworzymy nowy obiekt lini - klasy VectorGraphicBox
        vectorGraphicBox.type = "line"  // ustawiamy typ grafiki
        vectorGraphicBox.x1 = x1  // ustawiamy współrzędną x1
        vectorGraphicBox.y1 = y1  // ustawiamy współrzędną y1
        vectorGraphicBox.x2 = x2  // ustawiamy współrzędną x2
        vectorGraphicBox.y2 = y2  // ustawiamy współrzędną y2

        page.vectors.add(vectorGraphicBox)  // dodanie obrazu do listy
    }

    fun addTriangle(page: Pages, x: Int, y: Int, a: Int) {    // dodawanie trójkąta
        val vectorGraphicBox = VectorGraphicBox()  // tworzymy nowy obiekt trójkąta - klasy VectorGraphicBox
        vectorGraphicBox.type = "triangle"  // ustawiamy typ grafiki
        vectorGraphicBox.x1 = x  // ustawiamy współrzędną x1
        vectorGraphicBox.y1 = y  // ustawiamy współrzędną y1
        vectorGraphicBox.a = a  // ustawiamy bok trójkąta

        page.vectors.add(vectorGraphicBox)  // dodanie obrazu do listy
    }

    fun addSquare(page: Pages, x: Int, y: Int, a: Int) {    // dodawanie kwadratu
        val vectorGraphicBox = VectorGraphicBox()  // tworzymy nowy obiekt kwadratu - klasy VectorGraphicBox
        vectorGraphicBox.type = "square"  // ustawiamy typ grafiki
        vectorGraphicBox.x1 = x  // ustawiamy współrzędną x1
        vectorGraphicBox.y1 = y  // ustawiamy współrzędną y1
        vectorGraphicBox.a = a  // ustawiamy bok kwadratu

        page.vectors.add(vectorGraphicBox)  // dodanie obrazu do listy
    }

    fun addCircle(page: Pages, x: Int, y: Int, r: Int ) {    // dodawanie koła
        val vectorGraphicBox = VectorGraphicBox()  // tworzymy nowy obiekt koła - klasy VectorGraphicBox
        vectorGraphicBox.type = "circle"  // ustawiamy typ grafiki
        vectorGraphicBox.x1 = x  // ustawiamy współrzędną x1
        vectorGraphicBox.y1 = y  // ustawiamy współrzędną y1
        vectorGraphicBox.a = r  // ustawiamy promień koła

        page.vectors.add(vectorGraphicBox)  // dodanie obrazu do listy
    }










    fun savePDF() {
        addHeader()  // dopisanie nagłówek pliku PDF

        saveThings()    // dopisanie wszystkich tekstów, obrazów i grafiki wektorowej, oraz zapisanie roota

        addTrailer()    // dopisanie stopki  pliku PDF
    }










    fun clearPDF(){
        pages.clear()    // usunięcie zawartości listy po zapisaniu, żeby po zapisaniu nie zapisywać wielokrotnie tego samego
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





    private fun addVectorToPDF(vector: VectorGraphicBox){
        val filePath = File(  // lokalizacja pliku PDF
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
            fileName
        )

        try {
            val outputStream = FileOutputStream(filePath, true)

            var drawing_code = ""  // tymczasowa zmienna zawierająca deklarację figury graficznej

            when (vector.type){  // zapisanie odpowiednich danych do pliku PDF w zależności od typu grafiki
                "line" -> drawing_code = "${vector.x1} ${vector.y1}  m  ${vector.x2} ${vector.y2}  l S"
                "triangle" -> drawing_code = "${vector.x1 - (vector.a / 2)} ${vector.y1 - (vector.a * 0.433)}  m  ${vector.x1 + (vector.a / 2)} ${vector.y1 - (vector.a * 0.433)}  l  ${vector.x1} ${vector.y1 + (vector.a * 0.433)}  l  h S"
                "square" -> drawing_code = "${vector.x1 - (vector.a / 2)} ${vector.y1 - (vector.a / 2)}   m   ${vector.x1 + (vector.a / 2)} ${vector.y1 - (vector.a / 2)}   l   ${vector.x1 + (vector.a / 2)} ${vector.y1 + (vector.a / 2)}   l   ${vector.x1 - (vector.a / 2)} ${vector.y1 + (vector.a / 2)}   l   h S"
                "circle" -> drawing_code = "${vector.x1} ${vector.y1 - vector.a}     m     ${vector.x1 - (vector.a * 0.5523)} ${vector.y1 - vector.a}   ${vector.x1 - vector.a} ${vector.y1 - (vector.a * 0.5523)}   ${vector.x1 - vector.a} ${vector.y1}     c     ${vector.x1 - vector.a} ${vector.y1 + (vector.a * 0.5523)}   ${vector.x1 - (vector.a * 0.5523)} ${vector.y1 + vector.a}   ${vector.x1} ${vector.y1 + vector.a}     c     ${vector.x1 + (vector.a * 0.5523)} ${vector.y1 + vector.a}   ${vector.x1 + vector.a} ${vector.y1 + (vector.a * 0.5523)}   ${vector.x1 + vector.a} ${vector.y1}     c     ${vector.x1 + vector.a} ${vector.y1 - (vector.a * 0.5523)}   ${vector.x1 + (vector.a * 0.5523)} ${vector.y1 - vector.a}   ${vector.x1} ${vector.y1 - vector.a}     c  S "
            }

            // dopisanie grafiki
            outputStream.write("$object_counter 0 obj\n".toByteArray())
            outputStream.write("<< /Length 44 >>\n".toByteArray())
            outputStream.write("stream\n".toByteArray())
            outputStream.write("$drawing_code \n".toByteArray())
            outputStream.write("endstream \n".toByteArray())
            outputStream.write("endobj \n\n\n".toByteArray())
            vector.location_in_code = object_counter  // zapisanie indexu tej grafiki
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

                for (vector in page.vectors) {    // wpisanie wszystkich grafik wektorowych do pliku
                    addVectorToPDF(vector)
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
                var temp_xObject = ""  // deklaracja xObjectu strony (dla obrazów)
                var temp_contents = ""  // deklaracja contentu strony

                for (text in page.texts) {  // dopisanie deklaracji dla wszystkich tekstów na danej stronie
                    temp_contents += "${text.location_in_code} 0 R  "
                }

                for (image in page.images) {  // dopisanie deklaracji dla wszystkich obrazów na danej stronie
                    temp_xObject += "/${image.image_name} ${image.location_in_code} 0 R "
                    temp_contents += "${image.location_in_code + 1} 0 R  "
                }

                for (vector in page.vectors) {  // dopisanie deklaracji dla wszystkich grafik wektorowych na danej stronie
                    temp_contents += "${vector.location_in_code} 0 R  "
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




