package com.example.pdf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class Fragment_paint : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_paint, container, false)
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val canvasView = view.findViewById<CanvasView>(R.id.canvasView)

        val addText = view.findViewById<ImageButton>(R.id.addText)
        val clear = view.findViewById<ImageButton>(R.id.clear)
        val save = view.findViewById<ImageButton>(R.id.save)

        addText.setOnClickListener {
            isText = true
        }
        save.setOnClickListener {
            canvasView.createPDF()
        }
        clear.setOnClickListener {
            canvasView.clearCanvas()
        }
    }

}