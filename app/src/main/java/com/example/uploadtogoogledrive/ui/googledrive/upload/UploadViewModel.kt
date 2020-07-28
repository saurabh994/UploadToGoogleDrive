package com.example.uploadtogoogledrive.ui.googledrive.upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import com.example.uploadtogoogledrive.di.scope.ActivityScope
import com.example.uploadtogoogledrive.ui.googledrive.signin.GoogleSignInActivity
import com.example.uploadtogoogledrive.ui.viewmodel.BaseActivityViewModel
import com.example.uploadtogoogledrive.worker.UploadWorker
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@ActivityScope
class UploadViewModel @Inject constructor(): BaseActivityViewModel(),
    EasyPermissions.PermissionCallbacks {
    private var account: GoogleSignInAccount?=null
    lateinit var mCredential: GoogleAccountCredential
    var called = 0

    override fun handleIntent(intent: Intent) {
        super.handleIntent(intent)
        intent.extras?.apply {
            account = get(GoogleSignInActivity.ACCOUNT) as GoogleSignInAccount
        }
        mCredential = GoogleAccountCredential.usingOAuth2(
            mContext, listOf(*UploadActivity.SCOPES)
        )
            .setBackOff(ExponentialBackOff())
        getResultsFromApi()
    }

    fun getResultsFromApi() {
        if (mCredential.selectedAccountName == null) {
            chooseAccount()
        }else {
            if (called==1)
                UploadWorker.startUpload(mCredential)
        }
    }

    @AfterPermissionGranted(UploadActivity.REQUEST_PERMISSION_GET_ACCOUNTS)
    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(
                mContext, Manifest.permission.GET_ACCOUNTS)) {

            if (account?.email != null) {
                mCredential.selectedAccountName = account?.email
                getResultsFromApi()
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                mContext as Activity,
                "This app needs to access your Google account (via Contacts).",
                UploadActivity.REQUEST_PERMISSION_GET_ACCOUNTS,
                Manifest.permission.GET_ACCOUNTS
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults, this
        )
    }

}