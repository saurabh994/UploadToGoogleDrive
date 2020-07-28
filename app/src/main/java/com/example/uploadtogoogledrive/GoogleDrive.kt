package com.example.uploadtogoogledrive

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.work.WorkManager
import com.example.uploadtogoogledrive.di.component.DaggerApplicationComponent
import com.example.uploadtogoogledrive.ui.worker.WorkerInjectorFactory
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

class GoogleDrive : DaggerApplication() {
    @Inject
    lateinit var workerInjectorFactory: WorkerInjectorFactory

    @Inject
    lateinit var timberTree: Timber.Tree

    override fun onCreate() {
        super.onCreate()
        instance = this
        configureWorkManager()
        Timber.plant(timberTree)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        try {
            MultiDex.install(this)
        } catch (multiDexException: RuntimeException) {
            multiDexException.printStackTrace()
        }
    }

     private fun configureWorkManager() {
        val config = androidx.work.Configuration.Builder()
            .setMinimumLoggingLevel(Log.VERBOSE)
            .setTaskExecutor(Executors.newFixedThreadPool(10))
            .setExecutor(Executors.newFixedThreadPool(10))
            .setWorkerFactory(workerInjectorFactory)
            .build()
        WorkManager.initialize(this, config)
    }

    companion object {
        private var instance: GoogleDrive? = null

        fun applicationContext(): GoogleDrive {
            return instance as GoogleDrive
        }
    }

}