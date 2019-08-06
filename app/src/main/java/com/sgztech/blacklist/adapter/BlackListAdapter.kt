package com.sgztech.blacklist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.blacklist.R
import com.sgztech.blacklist.core.CoreApplication
import com.sgztech.blacklist.extension.toTelephoneFormated
import com.sgztech.blacklist.model.Contact
import com.sgztech.blacklist.util.AlertDialogUtil
import com.sgztech.blacklist.util.ToastUtil
import kotlinx.android.synthetic.main.black_list_card_view.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
            itemView.tvNumber.text = contact.numberPhone.toTelephoneFormated()
            itemView.tvDate.text = contact.blockedDate
            itemView.btnAddBlackList.setOnClickListener {
                createAlertDialog(contact).show()
            }
        }

        private fun createAlertDialog(contact: Contact): AlertDialog {
            val context = itemView.context
            return AlertDialogUtil.create(
                context,
                R.string.dialog_message_delete_black_list
            ) {
                deleteContact(contact)
                list.remove(contact)
                notifyDataSetChanged()
                ToastUtil.show(context, R.string.message_delete_item_black_list)
            }
        }

        private fun deleteContact(contact: Contact){
            GlobalScope.launch {
                val dao = CoreApplication.database?.contactDao()
                dao?.delete(contact)
            }
        }
    }
}