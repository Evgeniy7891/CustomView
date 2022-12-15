package ru.netoloy.statsview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import ru.netoloy.statsview.R
import ru.netoloy.statsview.utils.AndroidUtils
import java.lang.Float.sum
import java.lang.Math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(
    context,
    attributeSet,
    defStyleAttr,
    defStyleRes
) {
    private var textSize = AndroidUtils.dp(context, 20).toFloat()
    private var lineWidth = AndroidUtils.dp(context, 5)
    private var colors = emptyList<Int>()

    // обработка атрибутов
    init {
        context.withStyledAttributes(
            attributeSet,
            R.styleable.StatsView
        ) {
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth.toFloat()).toInt()
            colors = listOf(
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor()),
            )
        }
    }

    var data: List<Float> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private var radius = 0F
    private var center = PointF(0f, 0f)
    private var oval = RectF(0F, 0F, 0F, 0F)

    private val paint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        // ширина строки
        strokeWidth = lineWidth.toFloat()
        // Стиль отрисовки
        style = Paint.Style.STROKE
        // Округление краев
        strokeJoin = Paint.Join.ROUND
        // Округление при пересечении
        strokeCap = Paint.Cap.ROUND
    }

    // кисть для отрисовки текста
    private val textPaint = Paint(
        Paint.ANTI_ALIAS_FLAG
    ).apply {
        // размер текста
        textSize = this@StatsView.textSize
        // Стиль отрисовки
        style = Paint.Style.FILL
        // Гравитация
        textAlign = Paint.Align.CENTER
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    private fun generateRandomColor(): Int = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFF.toInt())

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) {
            return
        }
        var startAngle = -90F
        data.forEachIndexed { index, datum ->
            println("DATUM - $datum")
           val angle = (datum / data.sum())*360F
            println("angle - $angle")
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas.drawArc(oval, startAngle, angle, false, paint)
            startAngle += angle
        }
        paint.color = colors[0]
       canvas.drawArc(oval, startAngle, 8F , false, paint)
        // canvas.drawCircle(center.x, center.y, radius, paint)
        // отрисовка текста
        canvas.drawText(
            "%.2f%%".format(data.sum()*100),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint
        )
    }
}