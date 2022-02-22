package com.mop.base.data.config

import android.content.Context

class AppConstants {
    companion object {

        var context: Context? = null

        val applicationID: String = "com.a.b"

        @JvmStatic
        var testBaseUrl: String = "http://a.com"
        var shareBaseUrl: String = "http://a.com"
        var H5BaseUrl: String = "http://a.com"
        var ssoBaseUrl: String = "http://a.com"
        var customBaseUrl: String = "http://a.com"

        @JvmStatic
        var vehicleNetFlag: String = "33"

        const val KEY_SEE_TAB_TO_MEOW_FRIENDS_CLUB_T0_FOUR = 4 //喵友会

    }
}