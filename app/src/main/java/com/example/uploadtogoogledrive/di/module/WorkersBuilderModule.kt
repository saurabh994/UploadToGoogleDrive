package com.example.uploadtogoogledrive.di.module

import androidx.work.ListenableWorker
import com.example.uploadtogoogledrive.di.key.WorkerKey
import com.example.uploadtogoogledrive.ui.worker.IWorkerFactory
import com.example.uploadtogoogledrive.worker.UploadWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WorkersBuilderModule {
    @Binds
    @IntoMap
    @WorkerKey(UploadWorker::class)
    abstract fun bindChatCompressWork(worker: UploadWorker.Factory): IWorkerFactory<out ListenableWorker>
}