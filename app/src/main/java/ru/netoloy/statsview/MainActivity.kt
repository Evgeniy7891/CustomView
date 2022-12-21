package ru.netoloy.statsview

import android.animation.LayoutTransition
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import ru.netoloy.statsview.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val root = findViewById<ViewGroup>(R.id.root)
        root.layoutTransition = LayoutTransition().apply {
            setDuration(2000)
            setInterpolator(LayoutTransition.CHANGE_APPEARING,BounceInterpolator())
        }

        findViewById<View>(R.id.goButton).setOnClickListener {
          val view =  layoutInflater.inflate(R.layout.stats_view,root, false)
            root.addView(view, 0)
        }
    }
}