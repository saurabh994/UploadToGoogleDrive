package com.example.uploadtogoogledrive.ui.googledrive.upload

import java.lang.Exception

interface UploadInterface {
    fun exception(e : Exception)

    fun success()
}