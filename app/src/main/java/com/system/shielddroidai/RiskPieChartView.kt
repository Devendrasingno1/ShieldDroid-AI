package com.system.shielddroidai

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class RiskPieChartView(
    context: Context,
    private val critical: Int,
    private val high: Int,
    private val medium: Int,
    private val low: Int
) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val total = critical + high + medium + low
        if (total == 0) return

        val size = 160f
        val left = 40f
        val top = 30f
        val right = left + size
        val bottom = top + size

        var startAngle = -90f

        fun drawSlice(count: Int, color: Int) {
            if (count <= 0) return
            val sweepAngle = (count.toFloat() / total.toFloat()) * 360f
            paint.color = color
            canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, true, paint)
            startAngle += sweepAngle
        }

        drawSlice(critical, Color.parseColor("#F44336"))
        drawSlice(high, Color.parseColor("#FF9800"))
        drawSlice(medium, Color.parseColor("#FFC107"))
        drawSlice(low, Color.parseColor("#4CAF50"))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(220, 190)
    }
}