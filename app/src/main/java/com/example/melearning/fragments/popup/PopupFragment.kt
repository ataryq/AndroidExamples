package com.example.melearning.fragments.popup

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.melearning.FragmentManagerUtils
import com.example.melearning.fragments.BaseBindFragment

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PopupFragment<T: ViewBinding>(val layoutId: Int): BaseBindFragment<T>() {
    private var heights = listOf<Int>()
    private var heightByPercent: List<Float>? = null
    private var heightIndex = 1

    override fun layoutId() = this.layoutId

    fun layoutBinding(): T = binding

    fun dismiss() {
        FragmentManagerUtils.removeFragment(
            parentFragmentManager, this@PopupFragment)
    }

    fun setHeights(heightsInPx: List<Int>) {
        this.heights = heightsInPx.sortedDescending()
    }

    fun setHeightsByPercent(percent: List<Float>) {
        heightByPercent = percent
    }

    fun setDefaultSegmentIndex(index: Int) {
        heightIndex = index
    }

    private fun initHeightsByPercent(viewHeight: Int) {
        if(heightByPercent != null) {
            val segments = mutableListOf<Int>()
            for(segInPercent: Float in heightByPercent!!) {
                segments.add((viewHeight * segInPercent).toInt())
            }
            setHeights(segments)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateViewEnd() {
        val rootView = binding.root

        rootView.setOnTouchListener(object : View.OnTouchListener {
            private var prevY = 0
            private var totalYOffset = 0

            fun getY(motionEvent: MotionEvent): Int = motionEvent.rawY.toInt()

            @Synchronized
            override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if(prevY == 0) {
                            totalYOffset = heights[heightIndex]
                        }
                        prevY = getY(motionEvent)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val currentY = getY(motionEvent)
                        var diffY = currentY - prevY
                        if(totalYOffset + diffY < 0) {
                            diffY = -totalYOffset
                        }
                        view.offsetTopAndBottom(diffY)
                        prevY = currentY
                        totalYOffset += diffY
                    }

                    MotionEvent.ACTION_CANCEL,
                    MotionEvent.ACTION_UP -> {
                        val midSegments = mutableListOf<Int>()
                        for(iSegment in 0 until heights.size - 1) {
                            val currentSeg = heights[iSegment]
                            val nextSeg = heights[iSegment + 1]
                            midSegments.add(currentSeg)
                            midSegments.add((currentSeg + nextSeg) / 2)
                        }
                        midSegments.add(heights.last())

                        val currentOffset = totalYOffset
                        var foundSegment = heights.lastIndex

                        for(iMidSegment in midSegments.size - 1 downTo 0 ) {
                            val curMidHeight = midSegments[iMidSegment]

                            if(curMidHeight > currentOffset) {
                                foundSegment = (iMidSegment + 1) / 2
                                break
                            }
                        }

                        when (foundSegment) {
                            0 -> {
                                parentFragmentManager.popBackStack()
                            }
                            else -> {
                                println("translate to ${heights[foundSegment]}")
                                val newOffset = heights[foundSegment] - currentOffset
                                totalYOffset += newOffset
                                view.animate()
                                    .translationYBy(newOffset.toFloat())
                                    .setDuration(100)
                                    .start()
                            }
                        }
                        heightIndex = foundSegment
                    }
                }
                return true
            }
        })

        rootView.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
            initHeightsByPercent(v.height)
            v.offsetTopAndBottom(heights[heightIndex])
        }
    }
}