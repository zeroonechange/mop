package com.mop.base.mvvm

import android.content.Intent

interface IActivityResult {
    fun onActivityResultOk(intent: Intent) {}
    fun onActivityResult(resultCode: Int, intent: Intent) {}
    fun onActivityResultCanceled(intent: Intent) {}
}