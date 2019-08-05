package com.sgztech.blacklist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.blacklist.R
import com.sgztech.blacklist.model.Contact
import kotlinx.android.synthetic.main.black_list_card_view.view.*

class BlackListAdapter (
    private val list: MutableList<Contact>
    ) : RecyclerView.Adapter<BlackListAdapter.BlackListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlackListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.black_list_card_view, parent, false)
        return BlackListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BlackListViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class BlackListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact){
            itemView.tvNumber.text = contact.numberPhone
            itemView.tvDate.text = contact.blockedDate
        }
    }
}