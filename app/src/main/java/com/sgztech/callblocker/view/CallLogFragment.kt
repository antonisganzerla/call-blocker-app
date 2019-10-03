package com.sgztech.callblocker.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.callblocker.R
import com.sgztech.callblocker.adapter.CallLogAdapter
import com.sgztech.callblocker.extension.gone
import com.sgztech.callblocker.extension.toPtBrDateString
import com.sgztech.callblocker.extension.toTelephoneFormated
import com.sgztech.callblocker.extension.visible
import com.sgztech.callblocker.model.Contact
import com.sgztech.callblocker.util.ToastUtil.show
import com.sgztech.callblocker.viewmodel.ContactViewModel
import com.wickerlabs.logmanager.LogObject
import com.wickerlabs.logmanager.LogsManager
import kotlinx.android.synthetic.main.fragment_call_log.*
import org.koin.android.ext.android.inject
import java.util.*

class CallLogFragment : Fragment() {

    private val viewModel: ContactViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_call_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logsManager = LogsManager(requireContext())
        val callLogs = logsManager.getLogs(LogsManager.ALL_CALLS)
        setupRecyclerView(callLogs.reversed())

    }

    private fun setupRecyclerView(list: List<LogObject>) {
        recycler_view_call_log.adapter = CallLogAdapter(list) { callLog ->
            viewModel.insert(
                Contact(
                    name = callLog.contactName,
                    numberPhone = callLog.number.toTelephoneFormated(),
                    blocked = true,
                    blockedDate = Date().toPtBrDateString()
                )
            )
            show(requireContext(), R.string.message_add_item_block_list)
        }
        recycler_view_call_log.layoutManager = LinearLayoutManager(activity)
        recycler_view_call_log.setHasFixedSize(true)
        setupListVisibility(list)
    }

    private fun setupListVisibility(list: List<LogObject>) {
        if (list.isEmpty()) {
            recycler_view_call_log.gone()
            panel_empty_list.visible()
        } else {
            recycler_view_call_log.visible()
            panel_empty_list.gone()
        }
    }
}
