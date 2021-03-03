package com.example.melearning

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class Utils {
    interface Callback {
        fun called()
    }

    companion object {
        fun parseStringToNumber(text: EditText): Double {
            var percentText = text.text.toString()
            var retValue = 0.0
            if(percentText.isEmpty())
                return retValue

            try {
                retValue = percentText.toDouble()
            }
            finally {
                return retValue
            }
        }

        fun addEditTextChangedListener(editTextView: EditText, callback: Callback) {
            editTextView.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(s: CharSequence, s1: Int, s2: Int, s3: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
                {
                    callback.called()
                }
            })
        }

        fun runInThread(runnable: () -> Unit) {
            Thread(runnable).start()
        }

        fun runInUiThread(runnable: () -> Unit) {
            MainActivity.instance?.runOnUiThread(runnable)
        }
    }
}