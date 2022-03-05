package com.mop.base.mvvm.binding.viewadapter.view

import android.view.View
import androidx.databinding.BindingAdapter
import com.mop.base.R
import com.mop.base.data.config.GlobalConfig

@BindingAdapter(
    value = ["onClickCommand", "isInterval", "intervalMilliseconds"],
    requireAll = false
)
fun onClickCommand(
    view: View,
    clickCommand: View.OnClickListener?,
    isInterval: Boolean?,
    intervalMilliseconds: Int?
) {
    var interval = isInterval
    // xml中没有配置，那么使用全局的配置
    if (interval == null) {
        interval = GlobalConfig.Click.gIsClickInterval
    }
    // 没有配置时间，使用全局配置
    var milliseconds = intervalMilliseconds
    if (milliseconds == null) {
        milliseconds = GlobalConfig.Click.gClickIntervalMilliseconds
    }
    if (interval) {
        clickCommand?.let { view.clickWithTrigger(milliseconds.toLong(), it) }
    } else {
        view.setOnClickListener(clickCommand)
    }
}


/**
 * get set
 * 给view添加一个上次触发时间的属性（用来屏蔽连击操作）
 */
private var <T : View>T.triggerLastTime: Long
    get() = if (getTag(R.id.triggerLastTimeKey) != null) getTag(R.id.triggerLastTimeKey) as Long else 0
    set(value) {
        setTag(R.id.triggerLastTimeKey, value)
    }

/**
 * get set
 * 给view添加一个延迟的属性（用来屏蔽连击操作）
 */
private var <T : View> T.triggerDelay: Long
    get() = if (getTag(R.id.triggerDelayKey) != null) getTag(R.id.triggerDelayKey) as Long else -1
    set(value) {
        setTag(R.id.triggerDelayKey, value)
    }

/**
 * 判断时间是否满足再次点击的要求（控制点击）
 */
private fun <T : View> T.clickEnable(): Boolean {
    var clickable = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        clickable = true
        triggerLastTime = currentClickTime
    }
    return clickable
}

/***
 * 带延迟过滤点击事件的 View 扩展
 * @param delay Long 延迟时间，默认500毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(
    delay: Long = 500,
    block: View.OnClickListener,
    isViewModel: Boolean = true
) {
    triggerDelay = delay
    if (isViewModel){
        setOnClickListener {
            if (clickEnable()) {
                block.onClick(this)
            }
        }
    }else{
        if (this.hasOnClickListeners()) {
            if (clickEnable()) {
                block.onClick(this)
            }
        } else {
            setOnClickListener {
                if (clickEnable()) {
                    block.onClick(this)
                }
            }
        }
    }
}