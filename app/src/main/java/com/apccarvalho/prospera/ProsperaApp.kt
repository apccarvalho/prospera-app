package com.apccarvalho.prospera

import android.app.Application
import com.apccarvalho.prospera.di.AppContainer

class ProsperaApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
