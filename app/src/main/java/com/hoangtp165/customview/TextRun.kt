package com.hoangtp165.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

/**
 * Created by lucius on 09/10/2017.
 */
class TextRun @SuppressLint("Recycle") constructor(context: Context?, attrs: AttributeSet?) : View(context, attrs), TextRunInterface {

    companion object defaultValue {
        val DEFAULT_SPEED = 1f
        val DEFAULT_HEIGHT = 24
        val DEFAULT_WIDTH = 200
    }

    private var speed: Float
    private var contentText: String
    private val textSize: Int
    private var textColor: Int
    private var background: Int
    private val scaleDensity: Float

    private val paint: Paint
    private var textWidth: Int = 0
    private var textHeight: Int = 0

    init {
        val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.TextRun) ?: throw Exception()
        speed = typedArray.getFloat(R.styleable.TextRun_speed, DEFAULT_SPEED)
        textSize = typedArray.getDimensionPixelSize(R.styleable.TextRun_text_size, 14)
        textColor = typedArray.getColor(R.styleable.TextRun_text_color, Color.BLACK)
        background = typedArray.getColor(R.styleable.TextRun_text_run_background, Color.WHITE)
        contentText = typedArray.getString(R.styleable.TextRun_content)
        scaleDensity = resources.displayMetrics.scaledDensity

        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        calc()
    }

    fun calc() {
        paint.color = textColor
        paint.textSize = textSize.toFloat()
        val rect = Rect()
        paint.getTextBounds(contentText, 0, contentText.length, rect)
        textWidth = rect.width()
        textHeight = rect.height()
    }

    @SuppressLint("SwitchIntDef")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(
                Math.round(when (MeasureSpec.getMode(widthMeasureSpec)) {
                    MeasureSpec.EXACTLY -> MeasureSpec.getSize(widthMeasureSpec)
                    MeasureSpec.AT_MOST -> MeasureSpec.getSize(widthMeasureSpec)
                    else -> DEFAULT_WIDTH
                } * scaleDensity),
                Math.round(when (MeasureSpec.getMode(heightMeasureSpec)) {
                    MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
                    MeasureSpec.AT_MOST -> Math.min(MeasureSpec.getSize(heightMeasureSpec), DEFAULT_HEIGHT)
                    else -> DEFAULT_HEIGHT
                } * scaleDensity)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    private var cvWidth = 0
    private var cvHeight = 0
    private var x = 0
    private var y = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cvWidth = Math.round(w / scaleDensity)
        cvHeight = h

        x = cvWidth
        y = ((cvHeight - textHeight) / 2) + textHeight
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        x -= Math.round(speed * scaleDensity)

        if (x + textWidth < 0) {
            x = cvWidth
        }

        canvas.drawColor(background)
        canvas.drawText(contentText, x.toFloat(), y.toFloat(), paint)
        invalidate()
    }

    override fun setText(text: String) {
        this.contentText = text
        calc()
    }

    override fun setSpeed(speed: Float) {
        this.speed = speed
    }

    override fun setTextColor(color: Int) {
        this.textColor = color
    }


}