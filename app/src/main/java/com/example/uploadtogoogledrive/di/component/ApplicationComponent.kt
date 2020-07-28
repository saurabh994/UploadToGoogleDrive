package com.example.uploadtogoogledrive.di.component

import com.example.uploadtogoogledrive.GoogleDrive
import com.example.uploadtogoogledrive.di.module.*
import com.example.uploadtogoogledrive.ui.activity.BaseActivityModule
import com.example.uploadtogoogledrive.ui.googledrive.upload.UploadModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent : AndroidInjector<GoogleDrive> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: GoogleDrive): Builder

        fun build(): ApplicationComponent
    }
}
