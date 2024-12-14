package com.example.mamduhtaskmanager

import android.app.Application
import com.example.mamduhtaskmanager.data.AppContainer

class TaskApplicatoin: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container  = AppContainer(this)
    }
}