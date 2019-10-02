package com.sgztech.callblocker.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.callblocker.R
import com.sgztech.callblocker.adapter.BlockListAdapter
import com.sgztech.callblocker.extension.gone
import com.sgztech.callblocker.extension.visible
import com.sgztech.callblocker.model.Contact
import com.sgztech.callblocker.util.ToastUtil.show
import com.sgztech.callblocker.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.fragment_block_list.*
import org.koin.android.ext.android.inject

class BlockListFragment : Fragment() {

    private val viewModel: ContactViewModel by inject()
    private lateinit var adapter: BlockListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_block_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupAdapter()
        setupRecyclerView()
        loadData()
    }

    private fun setupAdapter() {
        adapter = BlockListAdapter { contact ->
            viewModel.delete(contact)
            show(requireContext(), R.string.message_delete_item_block_list)
        }
    }

    private fun setupRecyclerView() {
        recycler_view_block_list.let {
            it.adapter = adapter
            it.layoutManager =  LinearLayoutManager(activity)
            it.setHasFixedSize(true)
        }
    }

    private fun loadData() {
        viewModel.getAll().observe(
            this, Observer {
                adapter.setContacts(it)
                setupListVisibility(it)
            }
        )
    }

    private fun setupListVisibility(list: List<Contact>) {
        if (list.isEmpty()) {
            recycler_view_block_list.gone()
            panel_empty_list.visible()
        } else {
            recycler_view_block_list.visible()
            panel_empty_list.gone()
        }
    }
}
