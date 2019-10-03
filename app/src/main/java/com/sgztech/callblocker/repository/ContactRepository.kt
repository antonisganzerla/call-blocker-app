package com.sgztech.callblocker.repository

import androidx.lifecycle.LiveData
import com.sgztech.callblocker.dao.ContactDao
import com.sgztech.callblocker.extension.toTelephoneFormated
import com.sgztech.callblocker.model.Contact
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ContactRepository(private val dao: ContactDao) {

    fun insert(contact: Contact) {
        GlobalScope.launch {
            dao.add(contact)
        }
    }

    fun delete(contact: Contact) {
        GlobalScope.launch {
            dao.delete(contact)
        }
    }

    suspend fun loadByNumberPhone(numberPhone: String): Contact {
        val contact = GlobalScope.async {
            dao.loadByNumberPhone(numberPhone.toTelephoneFormated())
        }
        return contact.await()
    }

    fun getAll(): LiveData<List<Contact>> {
        return dao.all()
    }
}