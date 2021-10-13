package com.example.melearning.fragments.bottom_shirt

import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import androidx.viewbinding.ViewBinding
import com.example.melearning.FragmentManagerUtils
import com.example.melearning.fragments.BaseBindFragment

@Suppress("unused", "MemberVisibilityCanBePrivate", "ClickableViewAccessibility")
class BottomShirtFragment<T: ViewBinding>(val layoutId: Int):
    BaseBindFragment<T>(),
    View.OnTouchListener
{
    private var heights = listOf<Int>()
    private var heightByPercent: List<Float>? = null
    private var heightIndex = 1
    private var prevY = 0
    private var totalYOffset = 0

    override fun layoutId() = this.layoutId

    fun layoutBinding(): T = binding

    fun dismiss() {
        FragmentManagerUtils.removeFragment(
            parentFragmentManager, this@BottomShirtFragment)
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

    override fun onCreateViewEnd() {
        val rootView = binding.root

        rootView.setOnTouchListener(this)

        rootView.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
            initHeightsByPercent(v.height)
            v.offsetTopAndBottom(heights[heightIndex])
        }
    }

    fun getY(motionEvent: MotionEvent): Int = motionEvent.rawY.toInt()

    @Synchronized
    override fun onTouch(v: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                debugPrintln("ACTION_DOWN")

                if(prevY == 0) {
                    totalYOffset = heights[heightIndex]
                }
                prevY = getY(motionEvent)
            }

            MotionEvent.ACTION_MOVE -> {
                if(prevY == 0) {
                    debugPrintln("INIT")
                    totalYOffset = heights[heightIndex]
                    prevY = getY(motionEvent)
                    return true
                }

                val currentY = getY(motionEvent)
                var diffY = currentY - prevY
                if(totalYOffset + diffY < 0) {
                    diffY = -totalYOffset
                }

                v.offsetTopAndBottom(diffY)
                prevY = currentY
                totalYOffset += diffY
            }

            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                debugPrintln("ACTION_UP")
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
                        debugPrintln("translate to ${heights[foundSegment]}")
                        val newOffset = heights[foundSegment] - currentOffset
                        totalYOffset += newOffset
                        v.animate()
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

    fun setInsideScrollView(scrollView: ScrollView) {
        var insideTotalYOffset = 0
        var insidePrevY = 0
        scrollView.setOnTouchListener { _, event ->
            var ret = false
            val curY = event.rawY.toInt()

            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    debugPrintln("scrollView: ACTION_DOWN")
                    insidePrevY = curY
                    insideTotalYOffset = 0
                    onTouch(layoutBinding().root, event)
                }
                MotionEvent.ACTION_MOVE -> {
                    if(insidePrevY != 0) {
                        val diff = curY - insidePrevY
                        insideTotalYOffset += diff

                        if(insideTotalYOffset > 0 && scrollView.scrollY == 0) {
                            ret = onTouch(layoutBinding().root, event)
                        }
                    }

                    insidePrevY = curY
                }
                MotionEvent.ACTION_CANCEL,
                MotionEvent.ACTION_UP -> {
                    debugPrintln("scrollView: ACTION_UP")
                    onTouch(layoutBinding().root, event)
                    insidePrevY = 0
                }
            }

            ret
        }
    }
}