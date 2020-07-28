package com.example.uploadtogoogledrive.worker

import android.content.Context
import androidx.work.*
import com.example.uploadtogoogledrive.GoogleDrive
import com.example.uploadtogoogledrive.ui.googledrive.upload.UploadInterface
import com.example.uploadtogoogledrive.ui.googledrive.upload.UploadManager
import com.example.uploadtogoogledrive.ui.worker.IWorkerFactory
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider


class UploadWorker(
    private val appContext: Context,
    private val workerParams: WorkerParameters,
    private val uploadManager: UploadManager
) : RxWorker(appContext, workerParams) {

    override fun createWork(): Single<Result> {
        return Single.create((SingleOnSubscribe<Boolean> {
            myCallback?.let { it1 ->
                uploadManager.startUpload(it1,googleAccountCredential).subscribe({ next ->
                    if (!isStopped) {
                        enQueueRequest()
                        it.onSuccess(true)
                    }
                }, { t ->
                    if (!isStopped) it.onError(t)
                })
            }
        })).map {
            if (it) {
                Result.success()
            }
            else
                Result.failure()
        }.onErrorReturn {
            Result.failure()
        }
    }

    class Factory @Inject constructor(
        private val uploadManager : Provider<UploadManager>
    ) : IWorkerFactory<UploadWorker> {
        override fun create(appContext: Context, params: WorkerParameters): UploadWorker {
            return UploadWorker(
                appContext,
                params,
                uploadManager.get()
            )
        }
    }

    companion object {
        private var myCallback: UploadInterface? = null
        private var googleAccountCredential: GoogleAccountCredential? = null

        fun updateApi(listener: UploadInterface) {
            this.myCallback = listener
        }

        fun startUpload(mCredential: GoogleAccountCredential) {
            googleAccountCredential = mCredential

            enQueueRequest()
        }

        private fun enQueueRequest() {
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance()
            dueDate.set(Calendar.HOUR_OF_DAY, 11)
            dueDate.set(Calendar.MINUTE, 0)
            dueDate.set(Calendar.SECOND, 0)
            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR, 24)
            }

            val timeDiff = dueDate.timeInMillis.minus(currentDate.timeInMillis)
            val request = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            WorkManager.getInstance(GoogleDrive.applicationContext())
                .enqueueUniqueWork("UploadWork", ExistingWorkPolicy.REPLACE, request.setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                ) .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                    .build())
        }
    }
}
