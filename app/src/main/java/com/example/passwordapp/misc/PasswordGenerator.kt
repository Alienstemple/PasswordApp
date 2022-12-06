package com.example.passwordapp.misc

import android.content.Context
import android.util.Log
import com.example.passwordapp.MainActivity
import com.example.passwordapp.R

class PasswordGenerator(context: Context) {
    private val TAG = "PswdGeneratorLog"

    private val en_up = context.resources.getString(R.string.en_up)
    private val en_low = context.resources.getString(R.string.en_low)
    private val numbers = context.resources.getString(R.string.num)
    private val spec = context.resources.getString(R.string.spec)

    fun generatePassword(length: Int, up: Boolean, num: Boolean, special: Boolean): String {
        var symbolsRange = en_low  // Default - en low
        if (up) symbolsRange =
            "$symbolsRange${en_up}"  // TODO check min 1 symb of this category
        if (num) symbolsRange = "$symbolsRange${numbers}"
        if (special) symbolsRange = "$symbolsRange${spec}"

        val generatedPswd = CharArray(length) { i -> randomSymbol(symbolsRange) }.joinToString("")
        Log.d(TAG, "Generated: $generatedPswd")
        return generatedPswd
    }

    fun checkStrength(password: String) {
        // TODO
    }

    private fun randomSymbol(symbolsRange: String): Char {
        return symbolsRange[symbolsRange.indices.random()]
    }
}