package com.zlove.smart.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zlove.smart.router.annotations.Destination

@Destination(url = "", description = "")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}