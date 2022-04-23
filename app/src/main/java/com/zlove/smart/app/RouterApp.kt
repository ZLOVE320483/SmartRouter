package com.zlove.smart.app

import android.app.Application
import com.zlove.smart.router.runtime.SmartRouter

/**
 * Author by zlove, Email zlove.zhang@bytedance.com, Date on 2022/4/23.
 */
class RouterApp: Application() {

    override fun onCreate() {
        super.onCreate()
        SmartRouter.init()
    }
}