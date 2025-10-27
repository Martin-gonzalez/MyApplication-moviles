package com.example.myapplication

import android.app.Application
import com.example.myapplication.repository.AppDataRepository

class MyApplication : Application() {
    lateinit var appDataRepository: AppDataRepository

    override fun onCreate() {
        super.onCreate()
        appDataRepository = AppDataRepository(this)
    }
}
