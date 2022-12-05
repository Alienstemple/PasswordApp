package com.example.passwordapp.misc

import android.util.Log

object PasswordGenerator {
    private val TAG = "PswdGeneratorLog"
     fun generatePassword(length: Int, up: Boolean, num: Boolean, special: Boolean): String {
         var symbolsRange = Constants.EN_LOW  // Default - en low
         if(up) symbolsRange = "$symbolsRange${Constants.EN_UP}"  // TODO check min 1 symb of this category
         if(num) symbolsRange = "$symbolsRange${Constants.NUM}"
         if(special) symbolsRange = "$symbolsRange${Constants.SPEC}"

         val generatedPswd = CharArray(length) { i -> randomSymbol(symbolsRange) }.joinToString("")
         Log.d(TAG, "Generated: $generatedPswd")
         return generatedPswd
     }

     private fun randomSymbol(symbolsRange: String): Char {
         return symbolsRange[symbolsRange.indices.random()]
     }
}