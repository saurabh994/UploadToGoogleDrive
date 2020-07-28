package com.example.uploadtogoogledrive.di.module

import android.app.Application
import android.content.Context
import com.example.uploadtogoogledrive.GoogleDrive
import com.example.uploadtogoogledrive.di.qualifier.ApplicationContext
import dagger.Binds
import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Module(includes = [AndroidSupportInjectionModule::class, ActivityBuilderModule::class,TimberModule::class,WorkersBuilderModule::class])
abstract class ApplicationModule {
    @Binds
    @Singleton
    abstract fun bindApplication(application: GoogleDrive): Application

    @Binds
    @Singleton
    @ApplicationContext
    abstract fun bindApplicationContext(application: Application): Context
}
