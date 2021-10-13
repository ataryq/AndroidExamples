package com.example.melearning.fragments.bottom_shirt

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.transition.Slide
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.FragmentActivity
import androidx.transition.TransitionManager
import com.example.melearning.R

@Suppress("unused")
class BottomShirtChangingHeight(private val activity: FragmentActivity,
                                private val rootLayout: ViewGroup) {

    private fun showParams(rootView: View, view: View) {
        val array: IntArray = intArrayOf(0, 0)
        rootView.getLocationOnScreen(array)
        println("rootView on screen top: ${array[1]}")
        println("rootView on screen bottom: ${array[1] + rootView.height}")

        view.getLocationOnScreen(array)
        println("popup view on screen top: ${array[1]}")
        println("popup view on screen bottom: ${array[1] + view.height}")

        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        println("screen height: ${displayMetrics.heightPixels}")
    }

    private fun calculateRootViewBottomOffset(rootView: View): Int {
        val array: IntArray = intArrayOf(0, 0)
        rootView.getLocationOnScreen(array)

        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getRealMetrics(displayMetrics)

        return displayMetrics.heightPixels - (array[1] + rootView.height)
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun show() {
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.popup_window, null) ?: return

        var mHeight = 500

        val popupWindow = PopupWindow(
            view,
            LinearLayout.LayoutParams.MATCH_PARENT,
            mHeight
        )

        view.findViewById<Button>(R.id.dismissButton).setOnClickListener { popupWindow.dismiss() }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow.enterTransition = Slide().apply { slideEdge = Gravity.BOTTOM }
            popupWindow.exitTransition = Slide().apply { slideEdge = Gravity.BOTTOM }
        }

        view.setOnClickListener {
            popupWindow.dismiss()
        }

        val rootView = rootLayout
        println("rootView bottom: ${rootView.bottom}")

        val maxYOffset = -calculateRootViewBottomOffset(rootView)
        var mPosY = maxYOffset

        view.setOnTouchListener(object : View.OnTouchListener {
            private var dy = 0
            private var dx = 0
            private var mPrevRawY = 0

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(view: View?, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        println("ACTION_DOWN: before mPos: [$mPosY]")
                        println("ACTION_DOWN: before pos: [$dx, $dy]")

                        dy = mPosY - motionEvent.rawY.toInt()

                        println("ACTION_DOWN: after pos: [$dx, $dy]")

                        showParams(rootView, view!!)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        println("ACTION_MOVE: before mPos: [$mPosY]")
                        println("ACTION_MOVE: before pos: [$dx, $dy]")

                        var diff = 0
                        if(mPrevRawY == 0) mPrevRawY = motionEvent.rawY.toInt()
                        else {
                            diff = motionEvent.rawY.toInt() - mPrevRawY
                            mPrevRawY = motionEvent.rawY.toInt()
                        }

                        mPosY = motionEvent.rawY.toInt() + dy

                        println("ACTION_MOVE: before mPos: [$mPosY]")
                        println("ACTION_MOVE: before pos: [$dx, $dy]")

                        println("pos y: $mPosY")

                        mHeight -= diff

                        popupWindow.update(0, /*-mPosY*/0, -1,
                            mHeight )
                        println("mHeight: $mHeight")
                    }
                }
                return true
            }
        })

        TransitionManager.beginDelayedTransition(rootView)
        popupWindow.showAtLocation(
            rootView,
            Gravity.BOTTOM,
            0,
            0
        )
    }
}