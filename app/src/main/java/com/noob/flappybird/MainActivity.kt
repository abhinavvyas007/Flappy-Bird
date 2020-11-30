package com.noob.flappybird

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var point : Int = 0
    var high : Int = 0
    lateinit var sharedPreferences : SharedPreferences
    final var   myprefrence = "mypref"
    final var  name : String = "namekey"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        point = intent.getIntExtra("key", 0)
            retrive()
        if(high < point) {
            saveview()
            highscore.setText("highscore: $point")
        }
        else {
            highscore.setText("Highscore: $high")
        }


        play.setOnClickListener {
            var intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
    public fun saveview() {
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(name, point)
        editor.apply()
    }
    public  fun retrive() {
        sharedPreferences = getSharedPreferences(myprefrence, MODE_PRIVATE)
        if (sharedPreferences.contains(name)) {
           high = sharedPreferences.getInt(name, 0)
        }
    }

}