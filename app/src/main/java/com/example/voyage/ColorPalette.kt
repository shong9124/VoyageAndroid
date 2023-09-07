package com.example.voyage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ColorPalette : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_color_palette, container, false)
        val tvFragment: TextView = view.findViewById(R.id.tv_fragment)
        val message: String? = this.arguments?.getString("message")

        tvFragment.text = message
        return view
    }

}