package com.mop.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View

class MineTitleView : View {

    constructor(context: Context) : this(context, null) {
        initDraw()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        initDraw()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, 0) {
        initDraw()
    }

    var paint: Paint? = null

    private var density = 0f
    private var fontScale = 0f
    private var nickNameWidth: Float = 0f
    private var nickNameTxtHeight = 0f
    private var defaultWidth = 0

    private val TAG = "MineTitleView"

    private fun initDraw() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        density = Resources.getSystem().displayMetrics.density
        fontScale = Resources.getSystem().displayMetrics.scaledDensity
        nickNameTxtHeight = dp2px(39)
        defaultWidth = dp2px(176).toInt()
    }

    val white = Color.parseColor("#FFFFFF")  // 昵称颜色
    val red = Color.parseColor("#FF0000")   // 测试时的颜色
    val strikeColor = Color.parseColor("#efcd65") // 标志 正方形角颜色
    val tagTextColor = Color.parseColor("#efcd65") // 标志  字体颜色


    var oneLineTotalWidth: Float = 0f
    var tagNameWidth: Float = 0f

    var nickName = "公路之歌"
    var tagName = "官方"


    /**
     * 先计算文字的长度    计算标志的长度  俩个间隔12dp
     * 如果总长度大于俩控件的长度  就俩行
     *      先画第一行的文字  再画第二行的   最后画标志文字  外框
     *
     * 如果一行可以放下俩控件   就直接画昵称 和 标志
     *
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        Log.e(TAG, "onDraw: ")
        super.onDraw(canvas)
        paint?.apply {
            if (oneLineTotalWidth > defaultWidth) {  // 一行放不下
                if (nickNameWidth > defaultWidth) {
                    val idx = findSuitableIdx(nickName)
                    val lineOne = nickName.substring(0, idx)
                    val lineTwo = nickName.substring(idx)
                    drawName(lineOne, 0f, nickNameTxtHeight, this, canvas)
                    drawNameAndTag(lineTwo, tagName, tagNameWidth, 0f, nickNameTxtHeight * 2, this, canvas)
                } else {
                    drawName(nickName, 0f, nickNameTxtHeight, this, canvas)
                    drawNameAndTag("", tagName, tagNameWidth, 0f, nickNameTxtHeight * 2, this, canvas)
                }
            } else {
                drawNameAndTag(nickName, tagName, tagNameWidth, 0f, nickNameTxtHeight, this, canvas)
            }
        }
    }

    // 根据绘制的字体宽度  来决定截取哪一段字符串
    private fun findSuitableIdx(str: String): Int {
        var idx = 1
        if (str.length > 6) {  // 理论上不会出现6个以下的字符串  6个中文刚好可以显示   英文字符 更窄
            idx = 6
            paint?.apply {
                for (i in str.length..6) {
                    if (measureText(str.substring(0, i)) < defaultWidth) {
                        idx = i
                        break
                    }
                }
            }
        }
        return idx
    }


    // 指定默认的宽和高  设置 wrap_conten 属性时设置此默认宽高即可
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.e(TAG, "onMeasure: ")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, suitableH)
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidth, heightSpecSize)
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, suitableH)
        }
    }

    var suitableH = 0

    private fun calculate() {
        paint?.apply {
            textSize = sp2px(28)
            nickNameWidth = measureText(nickName, 0, nickName.length)

            textSize = sp2px(14)  // 如果没标志 长度为0
            tagNameWidth = if (tagName.isNullOrEmpty()) 0f else measureText(tagName, 0, tagName.length) + dp2px(5) * 2 // 内间距 5dp // 如果没标志  也不用间距了
            oneLineTotalWidth = nickNameWidth + if (tagNameWidth == 0f) 0f else (tagNameWidth + dp2px(12))  // 昵称名称长度  +  标志长度  +  间距

            suitableH = ((if (oneLineTotalWidth > defaultWidth) nickNameTxtHeight * 2 else nickNameTxtHeight) + dp2px(15)).toInt()
        }
    }

    private fun freshUI() {
        calculate()

        val params = layoutParams
        params.height = suitableH
        layoutParams = params

        requestLayout()
    }

    fun updateNickName(txt: String?) {
        nickName = txt ?: ""

        freshUI()
    }

    fun updateTagName(txt: String?) {
        tagName = txt ?: ""
        freshUI()
    }

    fun updateNickameAndTagName(name: String?, tag: String?) {
        nickName = name ?: ""
        tagName = tag ?: ""
        freshUI() //        invalidate() // 不会执行 onMeasure  适合定高宽固定的场景
    }

    /***
     *  绘制昵称  颜色和字体大小都写死的
     *  x,y 字体 左下角位置
     */
    private fun drawName(str: String, x: Float, y: Float, paint: Paint, canvas: Canvas?) {
        paint.apply { //            color = red  // 自测时 红色显眼
            color = white
            style = Paint.Style.FILL
            textSize = sp2px(28)
            canvas?.drawText(str, x, y, this)
        }
    }

    private fun drawNameAndTag(str: String, tag: String, tagNameWidth: Float, x: Float, y: Float, paint: Paint, canvas: Canvas?) {
        paint.apply {

            drawName(str, x, y, this, canvas)

            if (tagNameWidth > 0) {

                val lineTwoWidth = measureText(str, 0, str.length)
                val oneLineTotalWidth = lineTwoWidth + tagNameWidth + (if (str.isNullOrEmpty()) 0f else dp2px(12))

                val left = oneLineTotalWidth - tagNameWidth
                val top = y - dp2px(19)
                val right = oneLineTotalWidth
                val bottom = y

                color = strikeColor
                style = Paint.Style.STROKE
                strokeWidth = dp2px(1)
                canvas?.drawRect(
                    Rect(
                        left.toInt(), top.toInt(), right.toInt(), bottom.toInt()
                    ), this
                )

                color = tagTextColor
                textSize = sp2px(14)
                style = Paint.Style.FILL
                canvas?.drawText(
                    tag, (left + dp2px(5)), bottom - dp2px(5), this
                )
            }
        }
    }

    fun sp2px(spValue: Int): Float {
        return (0.5f + spValue * fontScale) as Float
    }

    fun dp2px(dpValue: Int): Float {
        return (0.5f + dpValue * density) as Float
    }
}