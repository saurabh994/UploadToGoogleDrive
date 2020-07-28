package com.example.uploadtogoogledrive.ui.googledrive.upload

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.uploadtogoogledrive.R
import com.example.uploadtogoogledrive.databinding.ActivityUploadToDriveBinding
import com.example.uploadtogoogledrive.ui.activity.BaseActivity
import com.example.uploadtogoogledrive.worker.UploadWorker
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.services.drive.DriveScopes
import kotlinx.android.synthetic.main.activity_upload_to_drive.*
import timber.log.Timber
import javax.inject.Inject

class UploadActivity : BaseActivity<ActivityUploadToDriveBinding, UploadViewModel>(),UploadInterface {
    override val layoutViewRes: Int = R.layout.activity_upload_to_drive

    override val viewModelClass: Class<UploadViewModel> = UploadViewModel::class.java

    @Inject
    lateinit var uploadManager: UploadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btUpload.setOnClickListener {
            Toast.makeText(this,"Work is scheduled",Toast.LENGTH_LONG).show()
            viewModel.called = 1
            UploadWorker.updateApi(this)
            viewModel.getResultsFromApi()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            REQUEST_AUTHORIZATION -> {
                viewModel.getResultsFromApi()
            }
        }
    }

    companion object{
        val SCOPES = arrayOf(DriveScopes.DRIVE)
        private const val REQUEST_AUTHORIZATION = 1001
        const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
    }

    override fun exception(e: Exception) {
        if (e is UserRecoverableAuthIOException) {
            startActivityForResult(
                (e).intent,
                REQUEST_AUTHORIZATION
            )
        }
    }

    override fun success() {
        Toast.makeText(this,"Work Done",Toast.LENGTH_LONG).show()

    }

}