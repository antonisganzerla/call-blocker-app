package com.sgztech.callblocker.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgztech.callblocker.R
import com.sgztech.callblocker.adapter.ContactAdapter
import com.sgztech.callblocker.extension.gone
import com.sgztech.callblocker.extension.toPtBrDateString
import com.sgztech.callblocker.extension.toTelephoneFormated
import com.sgztech.callblocker.extension.visible
import com.sgztech.callblocker.model.Contact
import com.sgztech.callblocker.util.ToastUtil.show
import com.sgztech.callblocker.viewmodel.ContactViewModel
import com.tomash.androidcontacts.contactgetter.entity.ContactData
import com.tomash.androidcontacts.contactgetter.main.contactsGetter.ContactsGetterBuilder
import kotlinx.android.synthetic.main.fragment_contact.*
import org.koin.android.ext.android.inject
import java.util.*

class ContactFragment : Fragment() {

    private val viewModel: ContactViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(getContacts())
    }

    private fun setupRecyclerView(list: List<ContactData>) {
        recycler_view_contact.adapter = ContactAdapter(list) { contact ->
            viewModel.insert(
                Contact(
                    name = contact.compositeName,
                    numberPhone = contact.phoneList[0].mainData.toTelephoneFormated(),
                    blocked = true,
                    blockedDate = Date().toPtBrDateString()
                )
            )
            show(requireContext(), R.string.message_add_item_block_list)
        }
        recycler_view_contact.layoutManager = LinearLayoutManager(activity)
        recycler_view_contact.setHasFixedSize(true)
        setupListVisibility(list)
    }

    private fun setupListVisibility(list: List<ContactData>) {
        if (list.isEmpty()) {
            recycler_view_contact.gone()
            panel_empty_list.visible()
        } else {
            recycler_view_contact.visible()
            panel_empty_list.gone()
        }
    }

    private fun getContacts(): List<ContactData> {
        val contacts = ContactsGetterBuilder(requireContext())
            .allFields()
            .buildList()
        return contacts.toList()
    }

}
