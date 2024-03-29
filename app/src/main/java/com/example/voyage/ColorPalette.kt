package com.example.voyage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ColorPalette : BottomSheetDialogFragment() {
    lateinit var sendColor: CallBack

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //초기화
        sendColor = context as CallBack
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_color_palette, container, false)

        //Color Palette 클릭 이벤트
        val redBtn : Button = view.findViewById(R.id.redBtn)
        val orangeBtn : Button = view.findViewById(R.id.orangeBtn)
        val yellowBtn : Button = view.findViewById(R.id.yellowBtn)
        val greenBtn : Button = view.findViewById(R.id.greenBtn)
        val deepGreenBtn : Button = view.findViewById(R.id.deepGreenBtn)
        val blueBtn: Button = view.findViewById(R.id.blueBtn)
        val deepBlueBtn : Button = view.findViewById(R.id.deepBlueBtn)
        val brownBtn : Button = view.findViewById(R.id.brownBtn)
        val grayBtn : Button = view.findViewById(R.id.grayBtn)

        redBtn.setOnClickListener {
            color = "RED"
            sendColor.callBackExample("RED")
            dismiss()
        }
        orangeBtn.setOnClickListener {
            color = "ORANGE"
            sendColor.callBackExample("ORANGE")
            dismiss()
        }
        yellowBtn.setOnClickListener {
            color = "YELLOW"
            sendColor.callBackExample("YELLOW")
            dismiss()
        }
        greenBtn.setOnClickListener {
            color = "GREEN"
            sendColor.callBackExample("GREEN")
            dismiss()
        }
        deepGreenBtn.setOnClickListener {
            color = "DEEP GREEN"
            sendColor.callBackExample("DEEP GREEN")
            dismiss()
        }
        blueBtn.setOnClickListener {
            color = "BLUE"
            sendColor.callBackExample("BLUE")
            dismiss()
        }
        deepBlueBtn.setOnClickListener {
            color = "DEEP BLUE"
            sendColor.callBackExample("DEEP BLUE")
            dismiss()
        }
        brownBtn.setOnClickListener {
            color = "BROWN"
            sendColor.callBackExample("BROWN")
            dismiss()
        }
        grayBtn.setOnClickListener {
            color = "GRAY"
            sendColor.callBackExample("GRAY")
            dismiss()
        }

        return view
    }
}