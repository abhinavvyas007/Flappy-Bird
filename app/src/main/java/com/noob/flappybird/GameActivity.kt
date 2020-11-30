package com.noob.flappybird

import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

class GameActivity : AppCompatActivity() {
    lateinit var gameView : GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        var point = Point()
        windowManager.defaultDisplay.getSize(point)
        gameView = GameView(this, point.x.toFloat(), point.y.toFloat())
        setContentView(gameView)

    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
}