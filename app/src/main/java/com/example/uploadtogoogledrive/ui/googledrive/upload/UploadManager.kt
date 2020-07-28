package com.example.uploadtogoogledrive.ui.googledrive.upload

import android.content.Context
import android.os.Looper
import android.widget.Toast
import com.example.uploadtogoogledrive.R
import com.example.uploadtogoogledrive.di.qualifier.ApplicationContext
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import io.reactivex.Single
import org.apache.commons.io.FileUtils
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class UploadManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    @Throws(IOException::class)
    fun startUpload(
        myCallback: UploadInterface,
        googleAccountCredential: GoogleAccountCredential?
    ): Single<Any> {
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()
        val service = Drive.Builder(transport, jsonFactory, googleAccountCredential)
                      .setApplicationName("UsingDriveJavaApi")
                      .build()
        val path = context.filesDir.absolutePath.toString() + "/Sample"
        val fileMetadata = File()
        var file2: java.io.File? = null
        fileMetadata.name = "Saurabh file"
        fileMetadata.mimeType = "application/pdf"

        file2 = java.io.File(path)
        val inputStream: InputStream = context.resources.openRawResource(R.raw.sample)
        try {
            FileUtils.copyInputStreamToFile(inputStream, file2)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val mediaContent = FileContent("application/pdf", file2)
        return Single.create {
            try {
                val file: File = service.files().create(fileMetadata, mediaContent)
                        .setFields("id")
                        .execute()
                val mHandler = android.os.Handler(Looper.getMainLooper())
                mHandler.postDelayed({
                    Toast.makeText(context, "File created:" + file.id, Toast.LENGTH_SHORT).show()
                }, 200)
                myCallback.success()
                it.onSuccess(true)
            } catch (e: Exception) {
                it.onSuccess(false)
                myCallback.exception(e)
            }
        }
    }
}