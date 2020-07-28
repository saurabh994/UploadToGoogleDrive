package com.example.uploadtogoogledrive.di.module

import com.example.uploadtogoogledrive.di.scope.ActivityScope
import com.example.uploadtogoogledrive.ui.googledrive.upload.UploadActivity
import com.example.uploadtogoogledrive.ui.googledrive.upload.UploadModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {
    @ContributesAndroidInjector(modules = [UploadModule::class])
    @ActivityScope
    abstract fun contributeUploadActivity(): UploadActivity
}