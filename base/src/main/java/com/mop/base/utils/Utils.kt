package com.mop.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.Parcelable
import android.view.TouchDelegate
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.collection.ArrayMap
import androidx.core.content.ContextCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.regex.Pattern

fun Any.isInUIThread() = Looper.getMainLooper().thread == Thread.currentThread()

object Utils {
    val isNeedCheckPermission: Boolean
        get() = Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1


    fun isNullOrEmptyString(string: String?): Boolean {
        if (string.isNullOrEmpty()) {
            return true
        }
        return false
    }

    /**
     * 测量 view 的大小，返回宽高
     */
    fun measureView(view: View): IntArray {
        val arr = IntArray(2)
        val spec = View.MeasureSpec.makeMeasureSpec(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        view.measure(spec, spec)
        arr[0] = view.measuredWidth
        arr[1] = view.measuredHeight
        return arr
    }

    /**
     * 判断集合是否为空
     */
    fun <E> isEmpty(e: Collection<E>?) = e == null || e.isEmpty()


    /**
     * 系统分享文本的 Intent
     */
    fun shareTextIntent(text: String): Intent {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)
        sendIntent.type = "text/plain"
        return sendIntent
    }

    /**
     * 创建 Intent 实例，并把参数 [map] [bundle] 放进去
     */
    fun getIntentByMapOrBundle(
        context: Context? = null,
        clz: Class<out Activity>? = null,
        map: ArrayMap<String, *>? = null,
        bundle: Bundle? = null
    ): Intent {
        val intent =
            if (context != null && clz != null)
                Intent(context, clz)
            else
                Intent()

        map?.forEach { entry ->
            @Suppress("UNCHECKED_CAST")
            when (val value = entry.value) {
                is Boolean -> {
                    intent.putExtra(entry.key, value)
                }
                is BooleanArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Byte -> {
                    intent.putExtra(entry.key, value)
                }
                is ByteArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Char -> {
                    intent.putExtra(entry.key, value)
                }
                is CharArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Short -> {
                    intent.putExtra(entry.key, value)
                }
                is ShortArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Int -> {
                    intent.putExtra(entry.key, value)
                }
                is IntArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Long -> {
                    intent.putExtra(entry.key, value)
                }
                is LongArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Float -> {
                    intent.putExtra(entry.key, value)
                }
                is FloatArray -> {
                    intent.putExtra(entry.key, value)
                }
                is Double -> {
                    intent.putExtra(entry.key, value)
                }
                is DoubleArray -> {
                    intent.putExtra(entry.key, value)
                }
                is String -> {
                    intent.putExtra(entry.key, value)
                }
                is CharSequence -> {
                    intent.putExtra(entry.key, value)
                }
                is Parcelable -> {
                    intent.putExtra(entry.key, value)
                }
                is Serializable -> {
                    intent.putExtra(entry.key, value)
                }
                is Bundle -> {
                    intent.putExtra(entry.key, value)
                }
                is Intent -> {
                    intent.putExtra(entry.key, value)
                }
                is ArrayList<*> -> {
                    val any = if (value.isNotEmpty()) {
                        value[0]
                    } else null
                    when (any) {
                        is String -> {
                            intent.putExtra(entry.key, value as ArrayList<String>)
                        }
                        is Parcelable -> {
                            intent.putExtra(entry.key, value as ArrayList<Parcelable>)
                        }
                        is Int -> {
                            intent.putExtra(entry.key, value as ArrayList<Int>)
                        }
                        is CharSequence -> {
                            intent.putExtra(entry.key, value as ArrayList<CharSequence>)
                        }
                        else -> {
                            throw RuntimeException("不支持此类型 $value")
                        }
                    }
                }
                is Array<*> -> {
                    when {
                        value.isArrayOf<String>() -> {
                            intent.putExtra(entry.key, value as Array<String>)
                        }
                        value.isArrayOf<Parcelable>() -> {
                            intent.putExtra(entry.key, value as Array<Parcelable>)
                        }
                        value.isArrayOf<CharSequence>() -> {
                            intent.putExtra(entry.key, value as Array<CharSequence>)
                        }
                        else -> {
                            throw RuntimeException("不支持此类型 $value")
                        }
                    }
                }
                else -> {
                    throw RuntimeException("不支持此类型 $value")
                }
            }
        }
        bundle?.let { intent.putExtras(bundle) }
        return intent
    }

    fun releaseBinding(startClz: Class<*>?, targetClz: Class<*>?, obj: Any, filed: String) {
        // 通过反射，解决内存泄露问题
        GlobalScope.launch {
            var clz = startClz
            while (clz != null) {
                // 找到 mBinding 所在的类
                if (clz == targetClz) {
                    try {
                        val field = clz.getDeclaredField(filed)
                        field.isAccessible = true
                        field.set(obj, null)
                    } catch (ignore: Exception) {
                    }
                }
                clz = clz.superclass
            }
        }
    }

    /**
     * view 转 Bitmap
     */
    public fun convertViewToBitmap(view: View): Bitmap? {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            ),
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        return view.drawingCache
    }


    /**
     * 是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
     */
    var useThemestatusBarColor = false

    /**
     * 是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
     */
    var useStatusBarColor = true


    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    /**
     * 单独设置状态栏颜色，如:软键盘抬起冲突，则不隐藏状态栏，修改状态栏颜色即可
     */
    fun setStatusBarColor(activity: Activity, @ColorRes color: Int): Unit {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && useStatusBarColor) {
            activity.getWindow()
                .setStatusBarColor(ContextCompat.getColor(activity.application, color));

        }
    }


    fun regularNum(content: String): Int {
        var i = -1
        if (!content.isEmpty()) {
            try {
                i = Pattern.compile("[^(0-9).]").matcher(content).replaceAll("").trim().toInt()
            } catch (e: Exception) {

            }
        }
        return i
    }

    /**
     * 检测是否有Emoji表情
     *
     * @param source
     * @return
     */
    @SuppressLint("NewApi")
    fun containsEmo(source: String): Boolean {
        var p = Pattern.compile(
            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE
        ) as Pattern
        var m = p.matcher(source);
        return m.find();
    }


    /**
     * list 转 string
     */
    fun listToString(list: ArrayList<String>): String {
        if (list.isEmpty()) {
            return ""
        }
        return list.toString()
            .replace(" ", "")
            .replace("[", "")
            .replace("]", "")
    }

    fun expendTouchArea(view: View, expendSize: Int) {
        view?.let {
            var parentView = it.parent as View
            parentView.post {
                var rect = Rect()
                view.getHitRect(rect); //如果太早执行本函数，会获取rect失败，因为此时UI界面尚未开始绘制，无法获得正确的坐标
                rect.left -= expendSize
                rect.top -= expendSize
                rect.right += expendSize
                rect.bottom += expendSize
                parentView.touchDelegate = TouchDelegate(rect, view)
            }
        }
    }

}
