package com.noob.flappybird

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Background {
    var x :Float = 0f
    var y : Float = 0f
    var background : Bitmap
    var bird_one : Bitmap
    var bird_two : Bitmap


    constructor(screenx : Float, screeny : Float, res : Resources) {
        background = BitmapFactory.decodeResource(res, R.drawable.background)
        background = Bitmap.createScaledBitmap(background, screenx.toInt(), screeny.toInt(), false)

        bird_one = BitmapFactory.decodeResource(res, R.drawable.bird_up)
        bird_one = Bitmap.createScaledBitmap(bird_one, 130, 130, false)
        bird_two = BitmapFactory.decodeResource(res, R.drawable.bird_down)
        bird_two = Bitmap.createScaledBitmap(bird_two, 130, 130, false)

    }
}