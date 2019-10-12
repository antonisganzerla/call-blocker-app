package com.sgztech.callblocker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.callblocker.R
import com.sgztech.callblocker.extension.gone
import com.sgztech.callblocker.extension.toTelephoneFormated
import com.sgztech.callblocker.extension.visible
import com.sgztech.callblocker.util.AlertDialogUtil
import com.squareup.picasso.Picasso
import com.tomash.androidcontacts.contactgetter.entity.ContactData
import kotlinx.android.synthetic.main.contact_card_view.view.*

class ContactAdapter(
    private val list: List<ContactData>,
    private val saveCallback : (contact: ContactData) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_card_view, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: ContactData) {
            itemView.tvContactName.text = contact.compositeName
            if (contact.phoneList.isNotEmpty()) {
                itemView.tvNumberPhone.text = contact.phoneList[0].mainData.toTelephoneFormated()
                itemView.btnAddBlockList.visible()
            } else {
                itemView.tvNumberPhone.text = itemView.context.getString(R.string.contact_without_number)
                itemView.btnAddBlockList.gone()
            }

            if(contact.photoUri.toString().isNotEmpty()){
                Picasso.get().load(contact.photoUri).into(itemView.ivPhoto)
            }else{
                itemView.ivPhoto.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_person_black_24dp))
            }

            if(contact.emailList.isNotEmpty()){
                itemView.tvEmail.text = contact.emailList[0].mainData
            }else{
                itemView.tvEmail.text = ""
            }

            itemView.btnAddBlockList.setOnClickListener {
                createAlertDialog(contact).show()
            }
        }

        private fun createAlertDialog(contact: ContactData): AlertDialog {
            val context = itemView.context
            return AlertDialogUtil.create(
                context,
                R.string.dialog_message_add_contact_block_list
            ) {
                saveCallback(contact)
            }
        }
    }
}