package com.graceful.skyward

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object BCryptUtils {

    private val encoder = BCryptPasswordEncoder()

    fun encode(entry: String): String {
        return encoder.encode(entry)
    }

    fun macthes(entry: String, hash: String): Boolean {
        return encoder.matches(entry, hash)
    }
}