package ru.netoloy.statsview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netoloy.statsview.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val statsView = findViewById<StatsView>(R.id.statsView)
        val data = listOf(
            500F,
            500F,
            500F,
            500F,
        )
        statsView.data = data.map {
            it / data.sum()
        }
    }
}