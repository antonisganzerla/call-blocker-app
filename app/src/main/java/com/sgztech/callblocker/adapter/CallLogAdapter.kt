package com.sgztech.callblocker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.callblocker.R
import com.sgztech.callblocker.extension.toPtBrDateString
import com.sgztech.callblocker.extension.toTelephoneFormated
import com.sgztech.callblocker.util.AlertDialogUtil
import com.wickerlabs.logmanager.LogObject
import com.wickerlabs.logmanager.LogsManager
import kotlinx.android.synthetic.main.call_log_card_view.view.*
import java.util.*


class CallLogAdapter(
    private val list: List<LogObject>,
    private val saveCallback : (callLog: LogObject) -> Unit
) : RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.call_log_card_view, parent, false)
        return CallLogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(callLog: LogObject) {
            itemView.tvNumberPhone.text = callLog.contactName.toTelephoneFormated()
            itemView.tvDate.text = Date(callLog.date).toPtBrDateString()
            itemView.tvDuration.text = callLog.coolDuration

            with(itemView.ivDirection) {
                when (callLog.type) {
                    LogsManager.INCOMING -> this.setImageResource(R.drawable.received)
                    LogsManager.OUTGOING -> this.setImageResource(R.drawable.sent)
                    LogsManager.MISSED -> this.setImageResource(R.drawable.missed)
                    else -> this.setImageResource(R.drawable.cancelled)
                }
            }

            itemView.btnAddBlockList.setOnClickListener {
                createAlertDialog(callLog).show()
            }
        }

        private fun createAlertDialog(callLog: LogObject): AlertDialog {
            val context = itemView.context
            return AlertDialogUtil.create(
                context,
                R.string.dialog_message_add_block_list
            ) {
                saveCallback(callLog)
            }
        }
    }
}