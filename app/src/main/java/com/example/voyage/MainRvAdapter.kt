package com.example.voyage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.recyclerview.widget.RecyclerView

class MainRvAdapter (var add_schedule: ArrayList<AddSchedule>) : RecyclerView.Adapter<MainRvAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRvAdapter.CustomViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.main_rv_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainRvAdapter.CustomViewHolder, position: Int) {
        holder.title.text = add_schedule[position].title
        holder.content.text = add_schedule[position].content
        holder.memo.text = add_schedule[position].memo
//        holder.endAt = add_schedule.get(position).endAt
    }

    override fun getItemCount(): Int {
        return add_schedule.count()
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.tv_title)
        var content = itemView.findViewById<TextView>(R.id.tv_content)
        var memo = itemView.findViewById<TextView>(R.id.tv_memo)
//        var endAt = itemView.findViewById<TimePicker>(R.id.tp_endAt)
    }

}