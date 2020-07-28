package com.example.uploadtogoogledrive.ui.googledrive.upload

import androidx.appcompat.app.AppCompatActivity
import com.example.uploadtogoogledrive.di.key.ActivityViewModelKey
import com.example.uploadtogoogledrive.di.scope.ActivityScope
import com.example.uploadtogoogledrive.ui.activity.BaseActivityModule
import com.example.uploadtogoogledrive.ui.viewmodel.BaseActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module(includes = [BaseActivityModule::class])
abstract class UploadModule {
    @Binds
    @ActivityScope
    abstract fun bindActivity(activity: UploadActivity): AppCompatActivity

    @Binds
    @IntoMap
    @ActivityViewModelKey(UploadViewModel::class)
    @ActivityScope
    abstract fun bindViewModel(viewModel: UploadViewModel): BaseActivityViewModel
}