package com.example.passwordapp

import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.ImageView
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

            // Show ready password
            val generatedPassword = PasswordGenerator(this).generatePassword(
                numberOfSymbolsInPswd,
                isBigEngChecked,
                isNumberChecked,
                isSpecialSymbChecked
            )
            mainBinding.readyPasswordTextView.text = generatedPassword

            // Evaluate password strength with progress bar

            var strength = when(generatedPassword.length) {
                in 14..Int.MAX_VALUE -> "STRONG"
                in 10..13 -> "GOOD"
                in 6..9 -> "MEDIUM"
                in Constants.MIN_PSWD_LENGTH..6 -> "BAD"
                else -> "BAD"  // Should not reach this code
            }

            var levelList: Drawable = resources.getDrawable(R.drawable.progress_bar_items, theme)

            // or LevelListDrawable().addLevel(Drawable) ?
//            levelList.level = 2
//            var clipForeground = ClipDrawable(
//                resources.getDrawable(R.drawable.progress_bar_rectangle_green, theme), ClipDrawable.VERTICAL, Gravity.AXIS_CLIP)
//            clipForeground.setLevel(7000)

//            var foreground = resources.getDrawable(R.drawable.progress_bar_clip, theme)
//
//            foreground.setLevel(5)
            mainBinding.pswdStrengthProgressBar.setImageLevel(7000)
        }
    }
}