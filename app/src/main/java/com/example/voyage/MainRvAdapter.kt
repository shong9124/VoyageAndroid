package com.example.voyage

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        holder.itemView.setOnClickListener{
            Log.d("OCL", "schedule[${position}] clicked")
//            val intent = Intent(holder.itemView.context, EditScheduleScreen :: class.java)
            //nullPointerException
//            MainActivity().setActivityResult(intent, MainActivity().REQUEST_CODE_EDIT)
//            startActivityForResult(MainActivity(), intent, MainActivity().REQUEST_CODE_EDIT, null)
            //실행은 되는데 데이터를 가져오지 못함
//            ContextCompat.startActivity(holder.itemView.context, intent, null)

            val api = MainActivity().api
            val data = PostModel("testing PUT", "RED", "for testing PUT", s_day, "17:30")
            //put(보류)
            api.editSchedule("64240d2c20a07443f9de31fc", "642bde7f1e337972b4b3d734", data).enqueue(object : Callback<GetResponse> {
                override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                    Log.d("API_PUT", "put: " + response.toString())
                    Log.d("API_PUT", "put: " + response.body().toString())
                    Log.d("API_PUT", "put: schedule edited successfully")
                }
                override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                    Log.d("API_PUT", "put: fail to edit")
                }
            })
            //delete(보류)
//            api.deleteSchedule("64240d2c20a07443f9de31fc", "642bde7f1e337972b4b3d734")
//                .enqueue(object: Callback<Void> {
//                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                        Log.d("DELETE", "delete: " + response.toString())
//                        Log.d("DELETE", "delete: " + response.body().toString())
//                        Log.d("DELETE", "delete: successfully deleted")
//                    }
//                    override fun onFailure(call: Call<Void>, t: Throwable) {
//                        Log.d("DELETE", "delete: fail to delete")
//                    }
//                })
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
}