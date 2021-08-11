package com.example.melearning

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class Utils {

    interface Callback {
        fun called()
    }

    companion object {
        fun parseStringToNumber(text: String): Double {
            var percentText = text
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

        fun getColor(context: Context, colorRes: Int): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.getColor(colorRes)
            }
            else {
                return context.resources.getColor(colorRes)
            }
        }
    }
}