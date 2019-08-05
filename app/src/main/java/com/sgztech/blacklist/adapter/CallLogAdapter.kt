package com.sgztech.blacklist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.blacklist.R
import com.sgztech.blacklist.core.CoreApplication
import com.sgztech.blacklist.extension.toPtBrDateString
import com.sgztech.blacklist.extension.toTelephoneFormated
import com.sgztech.blacklist.model.CallLogApp
import kotlinx.android.synthetic.main.call_log_card_view.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class CallLogAdapter(
    private val list: MutableList<CallLogApp>
) : RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.call_log_card_view, parent, false)
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
            itemView.tvNumberPhone.text = callLog.contact.numberPhone.toTelephoneFormated()
            itemView.tvDirection.text = callLog.direction
            itemView.tvDate.text = callLog.callDayTime?.toPtBrDateString()
            itemView.btnAddBlackList.setOnClickListener {
                createAlertDialog(callLog).show()

            }
        }

        private fun createAlertDialog(callLog: CallLogApp): AlertDialog {
            return AlertDialog.Builder(itemView.context)
                .setTitle("Atenção")
                .setMessage("Deseja adicionar o número a lista de bloqueio?")
                .setPositiveButton("Sim") { _, _ ->
                    saveContact(callLog)
                    Toast.makeText(itemView.context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancelar") { _, _ ->
                    // unnecessary implementatiton
                }
                .create()
        }

        private fun saveContact(callLog: CallLogApp){
            GlobalScope.launch {
                val dao = CoreApplication.database?.contactDao()
                callLog.contact.blockedDate = Date().toPtBrDateString()
                callLog.contact.blocked = true
                dao?.add(callLog.contact)
            }
        }
    }

    companion object{
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 1
    }

}