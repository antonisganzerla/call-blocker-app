package com.sgztech.callblocker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.callblocker.R
import com.sgztech.callblocker.extension.toTelephoneFormated
import com.sgztech.callblocker.model.Contact
import com.sgztech.callblocker.util.AlertDialogUtil
import kotlinx.android.synthetic.main.block_list_card_view.view.*

class BlockListAdapter (
    private val deleteCallback : (contact: Contact) -> Unit
    ) : RecyclerView.Adapter<BlockListAdapter.BlockListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.block_list_card_view, parent, false)
        return BlockListViewHolder(view)
    }

    private var list: List<Contact> = ArrayList()

    fun setContacts(contacts: List<Contact>) {
        this.list = contacts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BlockListViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    inner class BlockListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact, position: Int){
            itemView.tvNumberPhone.text = contact.name.toTelephoneFormated()
            itemView.tvDate.text = contact.blockedDate
            itemView.btnAddBlockList.setOnClickListener {
                createAlertDialog(contact, position).show()
            }
        }

        private fun createAlertDialog(contact: Contact, position: Int): AlertDialog {
            val context = itemView.context
            return AlertDialogUtil.create(
                context,
                R.string.dialog_message_delete_block_list
            ) {
                deleteCallback(contact)
                notifyItemRemoved(position)
            }
        }
    }
}