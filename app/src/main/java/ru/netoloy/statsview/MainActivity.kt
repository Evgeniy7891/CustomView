package ru.netoloy.statsview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import ru.netoloy.statsview.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val statsView = findViewById<StatsView>(R.id.statsView)
        val textView = findViewById<TextView>(R.id.textAnim)
        val data = listOf(
            500F,
            500F,
            500F,
            500F,
        )

        statsView.data = data.map {
            it / data.sum()
        }
        statsView.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.animation).apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        textView.setText("onAnimationStart")
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        textView.setText("onAnimationEnd")
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                        textView.setText("onAnimationReapeat")
                    }
                })
            }
        )
    }
}