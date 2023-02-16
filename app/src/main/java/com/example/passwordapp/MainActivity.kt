package com.example.passwordapp

import android.content.*
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.passwordapp.databinding.ActivityMainBinding
import com.example.passwordapp.misc.Constants
import com.example.passwordapp.misc.KeyboardLayoutTranslator
import com.example.passwordapp.misc.PasswordGenerator
import com.example.passwordapp.misc.PasswordStrength

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    lateinit var myClipboard: ClipboardManager //  Does not work: private var clipboardManager: ClipboardManager? = null
    lateinit var myClip: ClipData

    private lateinit var en_up_keyb: String
    private lateinit var en_low_keyb: String
    private lateinit var en_up: String
    private lateinit var en_low: String
    private lateinit var ru_up: String
    private lateinit var ru_low: String
    private lateinit var numbers: String
    private lateinit var special: String

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "In onCreate")
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initFromResources()

        val enteredText = initCopyPaste()

        initTranslate(enteredText)

        initPasswordGenearte()

        saveAndRestoreEnteredText(enteredText)

        initFromSettings()

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    private fun initFromSettings() {
        val prefs = PreferenceManager  // Preferences из настроек
            .getDefaultSharedPreferences(this)
        // вытащили preferences
        val numberOfSymbols: String? = prefs.getString(getString(R.string.numberOfSymbols), "0")
        val useDigits: Boolean = prefs.getBoolean(getString(R.string.digits), false)
        val useCapital: Boolean = prefs.getBoolean(getString(R.string.capital), false)
        val useSpecial: Boolean = prefs.getBoolean(getString(R.string.special), false)

        Log.d(TAG,
            "Got from preferences: numOfSymb = $numberOfSymbols, digits = $useDigits, capital = $useCapital, special = $useSpecial")

        // preferences.get()
        mainBinding.apply {
            // установим значения
            numOfSymbolsInPswd.setText(numberOfSymbols)
            numbersCheckBox.isChecked = useDigits
            bigEngCheckBox.isChecked = useCapital
            specialSymbCheckBox.isChecked = useSpecial
        }
    }

    private fun saveAndRestoreEnteredText(enteredText: Editable?) {
        preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)

        mainBinding.enterText.setText(preferences.getString(ENTERED_TEXT_VALUE, ""))

        mainBinding.saveBtn?.setOnClickListener {
            val textValue = mainBinding.enterText.text.toString()
            preferences.edit()
                .putString(ENTERED_TEXT_VALUE, textValue)
                .apply()
        }
    }

    private fun initPasswordGenearte() {
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
            val generatedPassword =
                PasswordGenerator(en_up, en_low, numbers, special).generatePassword(
                    numberOfSymbolsInPswd,
                    isBigEngChecked,
                    isNumberChecked,
                    isSpecialSymbChecked
                )
            mainBinding.readyPasswordTextView.text = generatedPassword

            // Evaluate password strength with progress bar
            val strength =
                PasswordGenerator(en_up, en_low, numbers, special).checkStrength(generatedPassword)
            val imageLevel = when (strength) {
                PasswordStrength.STRONG -> 10000
                PasswordStrength.GOOD -> 7500
                PasswordStrength.MEDIUM -> 5000
                PasswordStrength.BAD -> 3500
            }

            mainBinding.pswdStrengthProgressBar.setImageLevel(imageLevel)
        }
    }

    private fun initTranslate(enteredText: Editable?) {
        mainBinding.translateButton.setOnClickListener {
            if (enteredText != null) {
                if (enteredText.isEmpty()) {   // Check if empty
                    Log.d(TAG, "Entered text is empty! Can't translate!")
                    mainBinding.anotherLayoutTextView.text = ""
                    return@setOnClickListener
                }
            }

            var translatedText =
                KeyboardLayoutTranslator(en_up_keyb, en_low_keyb, ru_up, ru_low).translate(
                    enteredText.toString())
            mainBinding.anotherLayoutTextView.text = translatedText

        }
    }

    private fun initCopyPaste(): Editable? {
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
        return enteredText
    }

    private fun initFromResources() {
        en_up_keyb = resources.getString(R.string.en_up_keyb)
        en_low_keyb = resources.getString(R.string.en_low_keyb)
        en_up = resources.getString(R.string.en_up)
        en_low = resources.getString(R.string.en_low)
        ru_up = resources.getString(R.string.ru_up)
        ru_low = resources.getString(R.string.ru_low)
        numbers = resources.getString(R.string.num)
        special = resources.getString(R.string.spec)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_menu -> {
                intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = "MainActLog"
        private val APP_PREFERENCES = "APP_PREFERENCES"
        private val ENTERED_TEXT_VALUE = "ENTERED_TEXT_VALUE"
    }
}