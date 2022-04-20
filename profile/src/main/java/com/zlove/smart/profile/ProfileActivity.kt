package com.zlove.smart.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zlove.smart.router.annotations.Destination

/**
 * Author by zlove, Email zlove.zhang@bytedance.com, Date on 2022/4/20.
 */
@Destination(url = "tiktok://profile/", description = "个人页")
class ProfileActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }
}