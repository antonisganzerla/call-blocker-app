package com.sgztech.blacklist.view


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.blacklist.R
import com.sgztech.blacklist.adapter.CallLogAdapter
import com.sgztech.blacklist.loader.CallLogApp
import com.sgztech.blacklist.loader.LogCallLoader
import kotlinx.android.synthetic.main.fragment_call_log.*


class CallLogFragment : Fragment() {

    private lateinit var loader: LogCallLoader

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call_log, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLoader()
    }


    private fun setupRecyclerView(list: MutableList<CallLogApp>) {
        recycler_view.adapter = CallLogAdapter(list)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)
    }

    private fun setupLoader() {
        activity?.let {
            loader = LogCallLoader(it) { list ->
                setupRecyclerView(list)
            }
        }
    }
}