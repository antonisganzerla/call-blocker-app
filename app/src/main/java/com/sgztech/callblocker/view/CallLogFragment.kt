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
import com.sgztech.callblocker.extension.visible
import com.sgztech.callblocker.loader.LogCallLoader
import com.sgztech.callblocker.model.CallLogApp
import kotlinx.android.synthetic.main.fragment_call_log.*


class CallLogFragment : Fragment() {

    private lateinit var loader: LogCallLoader

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_call_log, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLoader()
    }


    private fun setupRecyclerView(list: MutableList<CallLogApp>) {
        recycler_view_call_log.adapter = CallLogAdapter(list)
        recycler_view_call_log.layoutManager = LinearLayoutManager(activity)
        recycler_view_call_log.setHasFixedSize(true)
        setupListVisibility(list)
    }

    private fun setupListVisibility(list: MutableList<CallLogApp>) {
        if (list.isEmpty()) {
            recycler_view_call_log.gone()
            tv_empty_call_log_list.visible()
        } else {
            recycler_view_call_log.visible()
            tv_empty_call_log_list.gone()
        }
    }

    private fun setupLoader() {
        activity?.let {
            loader = LogCallLoader(it) { list ->
                setupRecyclerView(list)
            }
        }
    }
}
