package com.zlove.smart.app

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zlove.smart.router.annotations.Destination
import com.zlove.smart.router.runtime.SmartRouter

@Destination(url = "tiktok://main/", description = "APP首页")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.jump).setOnClickListener {
            SmartRouter.open(this, "tiktok://profile/?key=value")
        }
    }

}