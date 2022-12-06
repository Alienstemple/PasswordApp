package com.example.passwordapp

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.passwordapp.databinding.ActivityMainBinding
import com.example.passwordapp.misc.Constants
import com.example.passwordapp.misc.KeyboardLayoutTranslator
import com.example.passwordapp.misc.PasswordGenerator

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActLog"
    lateinit var mainBinding: ActivityMainBinding

    lateinit var myClipboard: ClipboardManager //  Does not work: private var clipboardManager: ClipboardManager? = null
    lateinit var myClip: ClipData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "In onCreate")
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // Initialise texts
        val en_up = resources.getString(R.string.en_up)
        val en_low = resources.getString(R.string.en_low)

        val en_up_keyb = resources.getString(R.string.en_up_keyb)
        val en_low_keyb = resources.getString(R.string.en_low_keyb)

        val ru_up = resources.getString(R.string.ru_up)
        val ru_low = resources.getString(R.string.ru_low)

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
                return@setOnClickListener
            }

            var translatedText = KeyboardLayoutTranslator(this).translate(enteredText.toString())
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

            // Set text "символов" from plurals resource
            mainBinding.numberOfSymbolsText.text =
                resources.getQuantityText(R.plurals.symbols_in_password, numberOfSymbolsInPswd)

            mainBinding.readyPasswordTextView.text = PasswordGenerator(this).generatePassword(
                numberOfSymbolsInPswd,
                isBigEngChecked,
                isNumberChecked,
                isSpecialSymbChecked
            )

//            mainBinding.pswdStrengthProgressBar.setImageDrawable(R.drawable.progress_bar_rectangle_green)
        }
    }
}