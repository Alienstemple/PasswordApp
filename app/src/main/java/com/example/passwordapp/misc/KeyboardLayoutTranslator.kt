package com.example.passwordapp.misc

import android.content.Context
import android.util.Log
import com.example.passwordapp.R

class KeyboardLayoutTranslator(context: Context) {
    private val TAG = "KeybLayTranslLog"

    private val en_up_keyb = context.resources.getString(R.string.en_up_keyb)
    private val en_low_keyb = context.resources.getString(R.string.en_low_keyb)
    private val ru_up = context.resources.getString(R.string.ru_up)
    private val ru_low = context.resources.getString(R.string.ru_low)

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
                in en_low_keyb -> ru_low[en_low_keyb.indexOf(enteredText[ind])]
                in en_up_keyb -> ru_up[en_up_keyb.indexOf(enteredText[ind])]
                in ru_low -> en_low_keyb[ru_low.indexOf(enteredText[ind])]
                in ru_up -> en_up_keyb[ru_up.indexOf(enteredText[ind])]
                else -> ""
            }
            Log.d(TAG, "Translated to: $ch")
            translatedText += ch
        }

        return translatedText
    }
}