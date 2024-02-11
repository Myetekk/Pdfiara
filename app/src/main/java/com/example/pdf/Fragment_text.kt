package com.example.pdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment


class Fragment_text : Fragment() {
    var first_run = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_text, container, false)
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readPage(view)

        val add_text = view.findViewById<ImageButton>(R.id.add_text)
        add_text?.setOnClickListener{
            readTextProp(view, PagesList[SelectedPage])
            clearTextProp(view)
        }
        val add_page = view.findViewById<ImageButton>(R.id.add_page)
        add_page?.setOnClickListener{
            SuperPDFFile.addPage()
        }
    }





    private fun readTextProp(view: View, page: Pages) {
        val coordinate_x: Int
        val coordinate_y: Int
        val text: String

        val temp_x = view.findViewById<EditText>(R.id.coordinate_x).text.toString()
        val temp_y = view.findViewById<EditText>(R.id.coordinate_y).text.toString()
        val temp_text = view.findViewById<EditText>(R.id.text).text.toString()

        coordinate_x = if (temp_x == "") 100
        else temp_x.toInt()

        coordinate_y = if (temp_y == "") 100
        else 800 - temp_y.toInt()

        text = if (temp_text == "") "XD"
        else temp_text

        SuperPDFFile.addText(page, text, coordinate_x, coordinate_y)
    }





    private fun readPage(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.spinner_pages)
        val adapter = ArrayAdapter(
            view.context,
            android.R.layout.simple_spinner_item,
            PagesListName
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                SelectedPage = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                SelectedPage = 0
            }
        }

    }





    private fun clearTextProp(view: View) {
        view.findViewById<EditText>(R.id.coordinate_x).setText("")
        view.findViewById<EditText>(R.id.coordinate_y).setText("")
        view.findViewById<EditText>(R.id.text).setText("")
    }



}