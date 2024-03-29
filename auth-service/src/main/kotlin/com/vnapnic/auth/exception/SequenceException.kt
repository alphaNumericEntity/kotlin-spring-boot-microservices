package com.vnapnic.auth.exception

class SequenceException(private val errMsg: String) : RuntimeException() {
    private val errCode: String? = null

    companion object {
        private const val serialVersionUID = 1L
    }
}