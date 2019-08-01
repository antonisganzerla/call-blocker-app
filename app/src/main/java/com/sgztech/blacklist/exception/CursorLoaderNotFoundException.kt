package com.sgztech.blacklist.exception

class CursorLoaderNotFoundException(private val id: Int): Exception() {

    override val message: String?
        get() = "Not found cursor loader with id: $id."

}