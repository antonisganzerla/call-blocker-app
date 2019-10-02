package com.sgztech.callblocker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sgztech.callblocker.model.Contact
import com.sgztech.callblocker.repository.ContactRepository

class ContactViewModel (private val repository: ContactRepository) : ViewModel()
{

    fun insert(contact: Contact) {
        repository.insert(contact)
    }

    fun delete(contact: Contact) {
        repository.delete(contact)
    }

    fun getAll(): LiveData<List<Contact>> {
        return repository.getAll()
    }
}