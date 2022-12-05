package com.example.passwordapp.misc

import android.util.Log

object KeyboardLayoutTranslator {
    private val TAG = "KeybLayTranslLog"

    fun translate(enteredText: String): String {
        /**
         * Checks if symbol is in eng or rus keyborad layout and translates for another layout.
         * For example, 'R' goes to 'К', '[' goes to 'х', '{' goes to 'Х'
         */
        Log.d(TAG, "Entered text: $enteredText")
        var translatedText = ""
        for (ind in enteredText.indices) {
            Log.d(TAG, "Current symbol: ${enteredText[ind]}")
            var ch = when (enteredText[ind]) {
                in Constants.EN_LOW_KEYB -> Constants.RU_LOW[Constants.EN_LOW_KEYB.indexOf(enteredText[ind])]
                in Constants.EN_UP_KEYB -> Constants.RU_UP[Constants.EN_UP_KEYB.indexOf(enteredText[ind])]
                in Constants.RU_LOW -> Constants.EN_LOW_KEYB[Constants.RU_LOW.indexOf(enteredText[ind])]
                in Constants.RU_UP -> Constants.EN_UP_KEYB[Constants.RU_UP.indexOf(enteredText[ind])]
                else -> ""
            }
            Log.d(TAG, "Translated to: $ch")
            translatedText += ch
        }

        return translatedText
    }
}