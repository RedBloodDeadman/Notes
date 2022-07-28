package com.notes.di

import android.app.Application

class DependencyManager private constructor(
    application: Application
) {

    companion object {
        private lateinit var instance: DependencyManager

        fun init(application: Application) {
            instance = DependencyManager(application)
        }

        fun getAppComponent() = instance.appComponent
    }

    private val appComponent = DaggerAppComponent.factory().create(application)

}