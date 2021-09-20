package com.example.melearning

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

@Suppress("unused")
class Utils {

    interface Callback {
        fun called()
    }

    companion object {
        fun parseStringToNumber(text: String): Double {
            var retValue = 0.0
            if (text.isEmpty())
                return retValue
            try {
                retValue = text.toDouble()
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

        fun <T>glideLoadImage(imageId: T,
                              imageView: ImageView,
                              finished: () -> Unit = {})
            = Glide.with(imageView)
            .load(imageId)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    println("glide loaded")
                    finished()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    println("glide loaded")
                    finished()
                    return false
                }
            })
            .into(imageView)

        fun <T>glideLoadImage(imageId: T,
                              context: Fragment,
                              imageView: ImageView,
                              finished: () -> Unit = {})
                = Glide.with(context)
            .load(imageId)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    println("glide loaded")
                    finished()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    println("glide loaded")
                    finished()
                    return false
                }
            })
            .into(imageView)
    }
}