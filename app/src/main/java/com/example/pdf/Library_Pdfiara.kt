package com.example.pdf

import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.StringBuilder

class PdfGenerator {





    companion object {
        @JvmStatic
        fun generatePdf(content_: String, filename: String) {
            val filePath = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString(),
                filename
            )

            try {
                val outputStream = FileOutputStream(filePath)
                outputStream.write("%PDF-1.5\n".toByteArray())

                outputStream.write("1 0 obj\n".toByteArray())
                outputStream.write("<< /Type /Catalog /Pages 2 0 R >>\n".toByteArray())
                outputStream.write("endobj\n".toByteArray())

                outputStream.write("2 0 obj\n".toByteArray())
                outputStream.write("<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n".toByteArray())
                outputStream.write("endobj\n".toByteArray())

                outputStream.write("3 0 obj\n".toByteArray())
                outputStream.write("<< /Type /Page /Parent 2 0 R /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>\n".toByteArray())
                outputStream.write("endobj\n".toByteArray())

                outputStream.write("4 0 obj\n".toByteArray())
                outputStream.write("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\n".toByteArray())
                outputStream.write("endobj\n".toByteArray())





                outputStream.write("5 0 obj\n".toByteArray())
                outputStream.write("<< /Length 44 >>\n".toByteArray())
                outputStream.write("stream\n".toByteArray())

                var content = content_
                content = content.trim()
                var content_temp: String
                var position = 700  // wysokość na jakiej wypisywany jest tekst (liczone od dołu)
                var index = 0

                while (content.length >= 80){
                    loop@ for (i in 80 downTo 0){  // szukamy ostatniej spacji
                        if (content[i].toString().equals(" ")){
                            index = i
                            break@loop
                        }
                    }

                    // wycinamy i wypisujemy tekst o długości 80-odległość do ostatniej spacji
                    content_temp = content.substring(0, index)
                    outputStream.write("BT /F1 12 Tf 70 $position Td ($content_temp) Tj ET \n".toByteArray())
                    content = content.substring(index)
                    position -= 30
                }
                outputStream.write("BT /F1 12 Tf 70 $position Td ($content) Tj ET \n".toByteArray())  // wypisujemy reszte stringa która jest krótsza od 80
                outputStream.write("endstream\n".toByteArray())
                outputStream.write("endobj\n".toByteArray())





                outputStream.write("xref\n".toByteArray())

                outputStream.write("0 6\n".toByteArray())
                outputStream.write("0000000000 65535 f \n".toByteArray())
                outputStream.write("0000000010 00000 n \n".toByteArray())
                outputStream.write("0000000055 00000 n \n".toByteArray())
                outputStream.write("0000000110 00000 n \n".toByteArray())
                outputStream.write("0000000180 00000 n \n".toByteArray())
                outputStream.write("0000000275 00000 n \n".toByteArray())

                outputStream.write("trailer\n".toByteArray())
                outputStream.write("<< /Size 6 /Root 1 0 R >>\n".toByteArray())
                outputStream.write("startxref\n".toByteArray())
                outputStream.write("357\n".toByteArray())
                outputStream.write("%%EOF\n".toByteArray())
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }





}