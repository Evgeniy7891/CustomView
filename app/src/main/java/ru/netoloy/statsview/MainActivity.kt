package ru.netoloy.statsview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.transition.Scene
import ru.netoloy.statsview.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val statsView = findViewById<StatsView>(R.id.statsView)
        val root = findViewById<ViewGroup>(R.id.root)
        val scene = Scene.getSceneForLayout(root, R.layout.end_scane, this)
        findViewById<View>(R.id.goButton).setOnClickListener {
           TransitionManager.go(scene)
        }
    }
}