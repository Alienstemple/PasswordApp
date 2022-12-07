package com.example.passwordapp.misc

import android.util.Log

class PasswordGenerator(private val en_up: String, private val en_low: String, private val numbers: String, private val spec: String) {
    private val TAG = "PswdGeneratorLog"

    fun generatePassword(length: Int, up: Boolean, num: Boolean, special: Boolean): String {
        var symbolsRange = en_low  // Default - en low
        if (up) symbolsRange = "$symbolsRange${en_up}"   // TODO StringBuilder
        if (num) symbolsRange = "$symbolsRange${numbers}"
        if (special) symbolsRange = "$symbolsRange${spec}"

        var generatedPswd = CharArray(length) { i -> randomSymbol(symbolsRange) }.joinToString("")

        // Add at least 1 symb of every category
        if (up) replaceRandomSymbol(generatedPswd, en_up)
        if (num) replaceRandomSymbol(generatedPswd, numbers)
        if (special) replaceRandomSymbol(generatedPswd, spec)

        Log.d(TAG, "Generated: $generatedPswd")
        return generatedPswd
    }

    fun checkStrength(password: String): PasswordStrength {
        return when(password.length) {
            in 14..Int.MAX_VALUE -> PasswordStrength.STRONG
            in 10..13 -> PasswordStrength.GOOD
            in 6..9 -> PasswordStrength.MEDIUM
            in Constants.MIN_PSWD_LENGTH..6 -> PasswordStrength.BAD
            else -> PasswordStrength.BAD  // Should not reach this code
        }
    }

    private fun randomSymbol(symbolsRange: String): Char {
        return symbolsRange[symbolsRange.indices.random()]
    }

    private fun replaceRandomSymbol(inputString: String, targetSymbolsRange: String): String {
        var randomIndex = inputString.indices.random()
        var outputString = StringBuilder(inputString).also { it.setCharAt(randomIndex, randomSymbol(targetSymbolsRange)) }.toString()
        return outputString
    }
}