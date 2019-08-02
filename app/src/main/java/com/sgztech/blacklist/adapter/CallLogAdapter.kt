package com.sgztech.blacklist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.blacklist.R
import com.sgztech.blacklist.loader.CallLogApp
import kotlinx.android.synthetic.main.main_line_view.view.*

class CallLogAdapter(
    private val list: MutableList<CallLogApp>
) : RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_line_view, parent, false)
        if(viewType == VIEW_TYPE_EMPTY){
            //load msg vazia
        }else{
            //load list
        }
        return CallLogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        if(list.isEmpty()){
            return VIEW_TYPE_EMPTY
        }else{
            return VIEW_TYPE_NORMAL
        }
    }

    inner class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(callLog: CallLogApp){
            itemView.tv_title.text = callLog.number
            itemView.newsTitle.text = callLog.date
            itemView.newsInfo.text = callLog.direction
            Log.w("CallLogViewHolder", "onBindViewHolder $callLog")
        }
    }

    companion object{
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 0
    }
}