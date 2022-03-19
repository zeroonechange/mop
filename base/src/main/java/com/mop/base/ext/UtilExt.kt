package com.mop.base.ext

import com.mop.base.base.BaseApp

class UtilExt {

}

fun dp2px(dpValue: Float): Int {
    val scale = BaseApp.instance!!.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}