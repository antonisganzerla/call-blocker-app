package com.sgztech.blacklist.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.blacklist.R
import com.sgztech.blacklist.adapter.BlackListAdapter
import com.sgztech.blacklist.core.CoreApplication
import com.sgztech.blacklist.model.Contact
import kotlinx.android.synthetic.main.fragment_black_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BlackListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_black_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        GlobalScope.launch(context = Dispatchers.Main) {
            setupRecyclerView(loadBlackList())
        }
    }

    private fun setupRecyclerView(list: MutableList<Contact>) {
        recycler_view_black_list.let {
            it.adapter = BlackListAdapter(list)
            it.layoutManager =  LinearLayoutManager(activity)
            it.setHasFixedSize(true)
        }
    }

    private suspend fun loadBlackList(): MutableList<Contact>{
        val result = GlobalScope.async {
            val dao = CoreApplication.database?.contactDao()
            dao?.all()
        }
        return result.await()?.toMutableList() ?: return mutableListOf()
    }
}
