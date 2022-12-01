package com.example.passwordapp

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.passwordapp.misc.Constants
import com.example.passwordapp.databinding.ActivityMainBinding
import com.example.passwordapp.misc.Layouts

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActLog"
    lateinit var mainBinding: ActivityMainBinding

    lateinit var myClipboard: ClipboardManager //  Does not work: private var clipboardManager: ClipboardManager? = null
    lateinit var myClip: ClipData

    // Get En and Ru symbols
    private val en_UP = resources.getString(R.string.en_up)
    private val en_LOW = resources.getString(R.string.en_low)
    private val en = en_UP + en_LOW

    private val ru_UP = resources.getString(R.string.ru_up)
    private val ru_LOW = resources.getString(R.string.ru_low)
    private val ru = ru_UP + ru_LOW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "In onCreate")
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        myClipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        val enteredText = mainBinding.enterText.text

        mainBinding.copyButton.setOnClickListener {
            Log.d(TAG, "Button Copy pressed")
            myClip = ClipData.newPlainText(Constants.COPIED_TEXT, enteredText)
            myClipboard.setPrimaryClip(myClip)

            Toast.makeText(this, R.string.text_was_copied, Toast.LENGTH_SHORT).show()
        }

        mainBinding.pasteButton.setOnClickListener {
            Log.d(TAG, "Text pasted")
            val myClipData = myClipboard.primaryClip
            val clipDataItem = myClipData?.getItemAt(0)

            mainBinding.anotherLayoutTextView.text = clipDataItem?.text.toString()

            Toast.makeText(applicationContext, "Text Pasted", Toast.LENGTH_SHORT).show()  // short perion of time
        }

//        TODO("Find out what is object")
        mainBinding.enterText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 == null || enteredText.isEmpty()) {   // Check if empty
                    mainBinding.anotherLayoutTextView.text = ""
                    return
                }

//                var layout: Layouts = Layouts.Any  // FIXME Initialize layout
//
//                for (c in p0) {
//
//                    layout = when {
//                        c in en -> Layouts.En
//                        c in ru -> Layouts.Ru
//                        else -> Layouts.Any
//                    }
//                }

            }

        }

        )



//
//                Log.d("Layout", "$layout $text")
//
//                when (layout) {
//                    Layouts.Any -> {
//                        output.setText("")
//                        output.hint = resources.getString(R.string.ambiguous_text)
//                    }
//                    Layouts.En -> {
//                        var s = ""
//                        for (c in text) {
//                            val i = keysEn.indexOfFirst { it == c}
//                            s += if (i != -1) {
//                                keysRu[i]
//                            } else {
//                                c
//                            }
//                        }
//                        output.setText(s)
//                    }
//                    Layouts.Ru -> {
//                        var s = ""
//                        for (c in text) {
//                            val i = keysRu.indexOfFirst { it == c }
//                            s += if (i != -1) {
//                                keysEn[i]
//                            } else {
//                                c
//                            }
//                        }
//                        output.setText(s)
//                    }
//                }
//            }


    }
}