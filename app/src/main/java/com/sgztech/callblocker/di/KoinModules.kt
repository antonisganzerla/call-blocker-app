package com.sgztech.callblocker.di

import com.sgztech.callblocker.database.AppDatabase
import com.sgztech.callblocker.repository.ContactRepository
import com.sgztech.callblocker.viewmodel.ContactViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dbModule = module {
    single {
        AppDatabase.getInstance(
            context = get()
        )
    }
    factory { get<AppDatabase>().contactDao() }
}

val repositoryModule = module {
    single { ContactRepository(get()) }
}

val uiModule = module {
    viewModel { ContactViewModel(get()) }
}