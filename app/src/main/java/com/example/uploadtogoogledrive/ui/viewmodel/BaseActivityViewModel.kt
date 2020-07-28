package com.example.uploadtogoogledrive.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel

abstract class BaseActivityViewModel : ViewModel() {
    protected lateinit var mContext: Context

    open fun handleActivityContext(context: Context) {
        mContext = context
    }

    open fun handleCreate(){}

    open fun handleIntent(intent: Intent) {}
}