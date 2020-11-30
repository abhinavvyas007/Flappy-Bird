package com.noob.flappybird

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.core.content.ContextCompat.startActivity
import java.lang.Thread.sleep
import kotlin.random.Random

class GameView: SurfaceView, Runnable {
    lateinit var thread : Thread
    var is_playing : Boolean = true
    var top_tube : Bitmap
    var bottom_tube : Bitmap
    var background1 : Background
    var screenx : Float
    var screeny : Float
    var paint : Paint

    var bird_image : Int = 0
    var birdy : Float
    val gravity = 4
    var velocity = 10
    var gap : Float = 400f
    var distance_between_tubes : Int
    var min_tube_offset : Float
    var max_tube_offset : Float
    var no_of_tubes = 4
    var tube_x_array : Array<Float>
    var top_tube_y_array : Array<Float>
    var score : Int = 0
    val mediaPlayer : MediaPlayer
    val lost : MediaPlayer
    var main : MainActivity

    constructor(context: Context, screenx: Float, screeny : Float) : super(context) {
        this.screenx = screenx
        this.screeny = screeny

        main = MainActivity()
        background1 = Background(screenx,screeny, resources)
        paint = Paint()
        birdy = (screeny / 2 - background1.bird_one.height / 2).toFloat()
        bottom_tube= BitmapFactory.decodeResource(resources, R.drawable.tunnel_down)
        bottom_tube = Bitmap.createScaledBitmap(bottom_tube, 230, 500, false)
        top_tube = BitmapFactory.decodeResource(resources, R.drawable.tunnel_up)
        top_tube = Bitmap.createScaledBitmap(top_tube, 230, 500, false)
        min_tube_offset = gap / 2
        max_tube_offset = screeny - gap - min_tube_offset
        distance_between_tubes = screenx.toInt() * 1/2
        tube_x_array = Array<Float>(size = no_of_tubes) {0f}
        top_tube_y_array = Array<Float>(size = no_of_tubes){0f}
        mediaPlayer = MediaPlayer.create(context, R.raw.score_audio)
        lost = MediaPlayer.create(context, R.raw.lost_sound)
        for(i in 0..no_of_tubes - 1) {
            tube_x_array[i] = (screenx + distance_between_tubes * i).toFloat()
            top_tube_y_array[i] = Random.nextInt(min_tube_offset.toInt(), max_tube_offset.toInt() + 1).toFloat()
        }
    }


    override fun run() {
        while(is_playing) {
            update()
            draw()
            sleep()
        }
        if (!is_playing) {
            var intent = Intent(context, MainActivity::class.java)
            intent.putExtra("key", score)
            startActivity(context, intent, Bundle())
        }
    }

    fun update() {
        var j : Int = 0
        for(i in 0..no_of_tubes - 1) {
            if (score < 50) {
                tube_x_array[i] = tube_x_array[i] - 15
            }
            else if(score < 100) {
                tube_x_array[i] = tube_x_array[i] - 18
            }
            else if(score < 300) {
                tube_x_array[i] = tube_x_array[i] - 20
            }
            else if (score <500) {
                tube_x_array[i] = tube_x_array[i] - 25
            }
            else {
                tube_x_array[i] = tube_x_array[i] - 30
            }
            if (tube_x_array[i] < -top_tube.width) {
                tube_x_array[i] += (distance_between_tubes * no_of_tubes).toFloat()
                top_tube_y_array[i] =
                    Random.nextInt(min_tube_offset.toInt(), max_tube_offset.toInt() + 1).toFloat()
            }
            if ((tube_x_array[i] <= screenx / 2 + background1.bird_one.width / 2 - 7) && (tube_x_array[i] + top_tube.width >= screenx / 2 - background1.bird_one.width/2 + 10)) {
                if (birdy < top_tube_y_array[i] - 9) {
                    is_playing = false
                    lost.start()
                } else if (birdy + background1.bird_one.height > top_tube_y_array[i] + gap + 5) {
                    is_playing = false
                    lost.start()
                }
                if(tube_x_array[i] + top_tube.width < screenx / 2 + 11 && tube_x_array[i] + top_tube.width > screenx / 2 - 5) {
                    score += 10
                    mediaPlayer.start()
                }
            }
        }


        if (bird_image == 0) {
            bird_image = 1
        }
        else {
            bird_image = 0
        }
        if (birdy > screeny - background1.bird_one.height) {
            birdy = screeny - background1.bird_one.height.toFloat()
        }
        if (birdy < screeny - background1.bird_one.height || velocity < 0) {
            velocity += gravity
            birdy += velocity
        }

    }
    fun draw() {
        if(holder.surface.isValid) {
            var canvas : Canvas = holder.lockCanvas()

            canvas.drawBitmap(background1.background, 0f, 0f, null)
            if (bird_image == 0) {
                canvas.drawBitmap(background1.bird_one, screenx/2 - background1.bird_one.width/2, birdy, null)
            }
            else {
                canvas.drawBitmap(background1.bird_two,screenx/2 - background1.bird_one.width/2, birdy,null)
            }
            for(i in 0..no_of_tubes - 1) {
                canvas.drawBitmap(top_tube, tube_x_array[i], top_tube_y_array[i] - top_tube.height, null)
                canvas.drawBitmap(bottom_tube, tube_x_array[i], top_tube_y_array[i] + gap, null)
            }
            paint.setColor(Color.BLACK)
            paint.textScaleX = 2f
            paint.textSize = 40f
            canvas.drawText("Score : $score", 10f,70f, paint)
            holder.unlockCanvasAndPost(canvas)
        }
    }
    fun sleep() {
        Thread.sleep(5)
    }

    fun resume() {
        is_playing = true
        thread = Thread(this)
        thread.start()
    }
    fun pause() {
        is_playing = false
        thread.join()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (birdy < 30) {
            birdy = 0F
            velocity = -1
        }
        else if (velocity > -20) {
            velocity = -40
        }
        return true
    }

}