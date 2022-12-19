package ru.netoloy.statsview.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
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

    private var progress = 0F
    private var animator: ValueAnimator? = null
    var progressSequential:FloatArray? = null

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
            load()
        }

    private fun load() {
        animator?.apply {
            cancel()
            removeAllListeners()
        }

        animator = ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener {
                interpolator = LinearInterpolator()
                duration = 2_000
                progress = it.animatedValue as Float
                println("PROGRESS - $progress")
                invalidate()
            }
            start()
        }

//        val progSeq = FloatArray(data.size)
//        animator = ValueAnimator.ofFloat(0f, 4f).apply {
//            addUpdateListener {
//                progress = it.animatedValue as Float
//                println("PROGRESS - $progress")
//                for(i in 0 ..  progSeq.size - 1){
//                    if(progress < i) {
//                        progSeq[i] = 0f
//                    } else {
//                        if (progress > i + 1)
//                            progSeq[i]=1f
//                     else
//                        progSeq[i] = progress - i
//                    }
//                }
//                    progressSequential = progSeq
//                    invalidate()
//
//            }
//                duration = 5000
//                start()
//        }
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
        var addAngle = startAngle
        val rotation = 360F * progress
        println("rotation - $rotation")
        println("progeress- $progress")

        data.forEachIndexed { index, datum ->
            println("DATUM - $datum")
           val angle = (datum / data.sum())*360F
            println("angle - $angle")
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            canvas.drawArc(oval, startAngle + rotation, angle * progress, false, paint)
            println("start - ${startAngle+rotation} - sweep = ${angle* progress}")
            startAngle += angle
        }

//        data.forEachIndexed { index, datum ->
//            println("DATUM - $datum")
//            val angle = datum *360F
//            println("angle - $angle")
//            paint.color = colors.getOrElse(index) { generateRandomColor() }
//            canvas.drawArc(oval, startAngle,angle * (progressSequential.let{ it?.get(index) ?: 0F }), false, paint)
//            println("index = $index, start - ${startAngle} - sweep = ${angle * (progressSequential.let{ it?.get(index) ?: 0F })}")
//            startAngle += angle
//        }

//        paint.color = colors[0]
//       canvas.drawArc(oval, startAngle, 1F , false, paint)
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