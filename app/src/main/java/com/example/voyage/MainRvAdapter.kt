package com.example.voyage

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MainRvAdapter (var add_schedule: ArrayList<AddSchedule>) : RecyclerView.Adapter<MainRvAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRvAdapter.CustomViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.main_rv_item, parent, false)
        return CustomViewHolder(view)
    }
    override fun onBindViewHolder(holder: MainRvAdapter.CustomViewHolder, position: Int) {
        holder.content_tv.text = add_schedule[position].content
        holder.memo_tv.text = add_schedule[position].memo
        holder.endAt_tv.text = add_schedule[position].endTime
        //일정 클릭 이벤트
        holder.itemView.setOnClickListener{
            indexOfSchedule = position
            Log.d("OCL", "$indexOfSchedule")
            Log.d("OCL", scheduleId)
            itemClickListener.onClick(it, position)
        }
        //일정 색상 변경
        if (add_schedule[position].color == "RED")
            holder.color_tv.setBackgroundColor(Color.parseColor("#d32f2f"))
        if (add_schedule[position].color == "ORANGE")
            holder.color_tv.setBackgroundColor(Color.parseColor("#f57c00"))
        if (add_schedule[position].color == "YELLOW")
            holder.color_tv.setBackgroundColor(Color.parseColor("#dfbc02"))
        if (add_schedule[position].color == "GREEN")
            holder.color_tv.setBackgroundColor(Color.parseColor("#689f38"))
        if (add_schedule[position].color == "DEEP GREEN")
            holder.color_tv.setBackgroundColor(Color.parseColor("#00796b"))
        if (add_schedule[position].color == "BLUE")
            holder.color_tv.setBackgroundColor(Color.parseColor("#0288d1"))
        if (add_schedule[position].color == "DEEP BLUE")
            holder.color_tv.setBackgroundColor(Color.parseColor("#303f9f"))
        if (add_schedule[position].color == "BROWN")
            holder.color_tv.setBackgroundColor(Color.parseColor("#5d4037"))
        if (add_schedule[position].color == "GRAY")
            holder.color_tv.setBackgroundColor(Color.parseColor("#455a64"))
    }
    override fun getItemCount(): Int {
        return add_schedule.count()
    }
    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var content_tv = itemView.findViewById<TextView>(R.id.tv_content)
        var color_tv = itemView.findViewById<TextView>(R.id.tv_color)
        var memo_tv = itemView.findViewById<TextView>(R.id.tv_memo)
        var endAt_tv = itemView.findViewById<TextView>(R.id.tv_endAt)
    }
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener
}