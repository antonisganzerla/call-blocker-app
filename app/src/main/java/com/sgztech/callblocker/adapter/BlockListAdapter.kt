package com.sgztech.callblocker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.callblocker.R
import com.sgztech.callblocker.core.CoreApplication
import com.sgztech.callblocker.extension.toTelephoneFormated
import com.sgztech.callblocker.model.Contact
import com.sgztech.callblocker.util.AlertDialogUtil
import com.sgztech.callblocker.util.ToastUtil
import kotlinx.android.synthetic.main.block_list_card_view.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BlockListAdapter (
    private val list: MutableList<Contact>
    ) : RecyclerView.Adapter<BlockListAdapter.BlockListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.block_list_card_view, parent, false)
        return BlockListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BlockListViewHolder, position: Int) {
        holder.bind(list[position])
    }


    inner class BlockListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact){
            itemView.tvNumber.text = contact.numberPhone.toTelephoneFormated()
            itemView.tvDate.text = contact.blockedDate
            itemView.btnAddBlockList.setOnClickListener {
                createAlertDialog(contact).show()
            }
        }

        private fun createAlertDialog(contact: Contact): AlertDialog {
            val context = itemView.context
            return AlertDialogUtil.create(
                context,
                R.string.dialog_message_delete_block_list
            ) {
                deleteContact(contact)
                list.remove(contact)
                notifyDataSetChanged()
                ToastUtil.show(context, R.string.message_delete_item_block_list)
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