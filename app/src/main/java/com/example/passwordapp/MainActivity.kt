package com.example.passwordapp

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.passwordapp.databinding.ActivityMainBinding
import com.example.passwordapp.misc.Constants

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActLog"
    lateinit var mainBinding: ActivityMainBinding

    lateinit var myClipboard: ClipboardManager //  Does not work: private var clipboardManager: ClipboardManager? = null
    lateinit var myClip: ClipData

    // Get En and Ru symbols
//    private val en_UP = getString(R.string.en_up)
//    private val en_LOW = getString(R.string.en_low)
//    private val en = en_UP + en_LOW
//
//    private val ru_UP = resources.getString(R.string.ru_up)   // FIXME java.lang.NPE
//    private val ru_LOW = resources.getString(R.string.ru_low)
//    private val ru = ru_UP + ru_LOW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "In onCreate")
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        val enteredText = mainBinding.enterText.text

        mainBinding.copyButton.setOnClickListener {   // Copy text
            Log.d(TAG, "Button Copy pressed")
            myClip = ClipData.newPlainText(Constants.COPIED_TEXT, enteredText)
            myClipboard.setPrimaryClip(myClip)

            Toast.makeText(this, R.string.text_was_copied, Toast.LENGTH_SHORT).show()
        }

        mainBinding.pasteButton.setOnClickListener {  // Paste text
            Log.d(TAG, "Text pasted")
            val myClipData = myClipboard.primaryClip
            val clipDataItem = myClipData?.getItemAt(0)

            mainBinding.anotherLayoutTextView.text = clipDataItem?.text.toString()

            Toast.makeText(applicationContext, "Text Pasted", Toast.LENGTH_SHORT)
                .show()  // short perion of time
        }

        mainBinding.translateButton.setOnClickListener {
            if (enteredText.isEmpty()) {   // Check if empty
                Log.d(TAG, "Entered text is empty! Can't translate!")
                mainBinding.anotherLayoutTextView.text = ""
                return@setOnClickListener  // TODO check
            }

            /**
             * Checks if symbol is in eng or rus keyborad layout and translates for another layout.
             * For example, 'R' goes to 'К', '[' goes to 'х', '{' goes to 'Х'
             */
            Log.d(TAG, "Entered text: $enteredText")
            var translatedText = ""
            for (ind in enteredText.indices) {
                Log.d(TAG, "Current symbol: ${enteredText[ind]}")
                var ch = when (enteredText[ind]) {
                    in Constants.EN_LOW_KEYB -> Constants.RU_LOW[ind]
                    in Constants.EN_UP_KEYB -> Constants.RU_UP[ind]
                    in Constants.RU_LOW -> Constants.EN_LOW_KEYB[ind]
                    in Constants.RU_UP -> Constants.EN_UP_KEYB[ind]
                    else -> ""  // TODO throw Toast for wrong symbol
//                    Toast.makeText(this, "Вы ввели неверный символ: ${enteredText[ind]} \n",
//                        Toast.LENGTH_SHORT)
                }
                Log.d(TAG, "Translated to: $ch")
                translatedText += ch
            }
            mainBinding.anotherLayoutTextView.text = translatedText

        }

        mainBinding.passwordGenerateButton.setOnClickListener {
            val numberOfSymbolsInPswd: Int = mainBinding.numOfSymbolsInPswd.text.toString().toInt()
            val isBigEngChecked = mainBinding.bigEngCheckBox.isChecked
            val isNumberChecked = mainBinding.numbersCheckBox.isChecked
            val isSpecialSymbChecked = mainBinding.specialSymbCheckBox.isChecked

            if (numberOfSymbolsInPswd < Constants.MIN_PSWD_LENGTH) {  // Check if too litle symbols
                mainBinding.numOfSymbolsInPswd.text.clear()
                Toast.makeText(this, Constants.TOO_SHORT_PSWD_ALERT, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mainBinding.readyPasswordTextView.text = generatePassword(
                numberOfSymbolsInPswd,
                isBigEngChecked,
                isNumberChecked,
                isSpecialSymbChecked
            )
        }


    }

    private fun generatePassword(length: Int, up: Boolean, num: Boolean, special: Boolean): String {
        var symbolsRange = Constants.EN_LOW  // Default - en low
        if(up) symbolsRange = "$symbolsRange${Constants.EN_UP}"  // TODO check min 1 symb of this category
        if(num) symbolsRange = "$symbolsRange${Constants.NUM}"
        if(special) symbolsRange = "$symbolsRange${Constants.SPEC}"
        // TODO check length
        val generatedPswd = CharArray(length) { i -> randomSymbol(symbolsRange) }.joinToString("")
        Log.d(TAG, "Generated: $generatedPswd")
        return generatedPswd
    }

    private fun randomSymbol(symbolsRange: String): Char {
        return symbolsRange[symbolsRange.indices.random()]
    }
}