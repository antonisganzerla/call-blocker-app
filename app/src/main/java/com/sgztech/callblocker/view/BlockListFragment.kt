package com.sgztech.callblocker.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.callblocker.R
import com.sgztech.callblocker.adapter.BlockListAdapter
import com.sgztech.callblocker.core.CoreApplication
import com.sgztech.callblocker.extension.gone
import com.sgztech.callblocker.extension.visible
import com.sgztech.callblocker.model.Contact
import kotlinx.android.synthetic.main.fragment_block_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BlockListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_block_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        GlobalScope.launch(context = Dispatchers.Main) {
            setupRecyclerView(loadBlockList())
        }
    }

    private fun setupRecyclerView(list: MutableList<Contact>) {
        recycler_view_block_list.let {
            it.adapter = BlockListAdapter(list)
            it.layoutManager =  LinearLayoutManager(activity)
            it.setHasFixedSize(true)
        }
        setupListVisibility(list)
    }

    private fun setupListVisibility(list: MutableList<Contact>) {
        if (list.isEmpty()) {
            recycler_view_block_list.gone()
            tv_empty_block_list.visible()
        } else {
            recycler_view_block_list.visible()
            tv_empty_block_list.gone()
        }
    }

    private suspend fun loadBlockList(): MutableList<Contact>{
        val result = GlobalScope.async {
            val dao = CoreApplication.database?.contactDao()
            dao?.all()
        }
        return result.await()?.toMutableList() ?: return mutableListOf()
    }
}
