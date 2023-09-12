package com.example.voyage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainRvAdapter (var add_schedule: ArrayList<AddSchedule>) : RecyclerView.Adapter<MainRvAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRvAdapter.CustomViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.main_rv_item, parent, false)
        return CustomViewHolder(view)
    }
    override fun onBindViewHolder(holder: MainRvAdapter.CustomViewHolder, position: Int) {
        holder.content_tv.text = add_schedule[position].content
        holder.color_tv.text = add_schedule[position].color
        holder.memo_tv.text = add_schedule[position].memo
        holder.endAt_tv.text = add_schedule[position].endTime
        //일정 클릭 이벤트
        holder.itemView.setOnClickListener{
            indexOfSchedule = position
            Log.d("OCL", "${indexOfSchedule}")
            val scheduleId = scheduleId
            Log.d("OCL", "${scheduleId}")
            itemClickListener.onClick(it, position)
        }
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