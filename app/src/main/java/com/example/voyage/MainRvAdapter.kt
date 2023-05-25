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
        holder.title.text = add_schedule.get(position).title.toString()
        holder.content.text = add_schedule.get(position).content.toString()
        holder.memo.text = add_schedule.get(position).memo.toString()

    }

    override fun getItemCount(): Int {
        return add_schedule.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.title_edt)
        var content = itemView.findViewById<TextView>(R.id.content_edt)
        var memo = itemView.findViewById<TextView>(R.id.memo_edt)
    }

}