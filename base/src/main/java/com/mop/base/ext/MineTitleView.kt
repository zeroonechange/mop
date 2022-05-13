package com.mop.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
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
    private var nickNameTxtHeight = 0f


    private fun initDraw() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        density = Resources.getSystem().displayMetrics.density
        fontScale = Resources.getSystem().displayMetrics.scaledDensity
        nickNameTxtHeight = dp2px(39)
    }

    val white = Color.parseColor("#FFFFFF")  // 昵称颜色
    val strikeColor = Color.parseColor("#efcd65") // 标志 正方形角颜色
    val tagTextColor = Color.parseColor("#efcd65") // 标志  字体颜色


    val nickName = "科兴吴彦祖"
    val tagName = "官方大帅比"

    /**
     * 先计算文字的长度    计算标志的长度  俩个间隔12dp
     * 如果总长度大于俩控件的长度  就俩行
     *      先画第一行的文字  再画第二行的   最后画标志文字  外框
     *
     * 如果一行可以放下俩控件   就直接画昵称 和 标志
     *
     * 测试没问题了
     *  后续用自定义样式  灵活使用  开放一些口子
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint?.apply {

            textSize = sp2px(28)
            val nickNameWidth = measureText(nickName, 0, nickName.length)

            color = strikeColor
            style = Paint.Style.STROKE
            strokeWidth = dp2px(1)
            textSize = sp2px(14)

            val tagNameWidth = measureText(tagName, 0, tagName.length) + dp2px(5) * 2 // 内间距 5dp

            val oneLineTotalWidth = nickNameWidth + tagNameWidth + dp2px(12)   // 昵称名称长度  +  标志长度  +  间距

            color = white
            style = Paint.Style.FILL
            textSize = sp2px(28)
            canvas?.drawText(nickName, 0, 0 , nickNameWidth , nickNameTxtHeight, this)

            color = strikeColor
            style = Paint.Style.STROKE
            strokeWidth = dp2px(1)
            canvas?.drawRect(
                Rect(
                    (oneLineTotalWidth - tagNameWidth) as Int,
                    0,
                    oneLineTotalWidth as Int,
                    dp2px(17) as Int
                ),
                this)

            color = tagTextColor
            textSize = sp2px(14)
            style = Paint.Style.FILL
            canvas?.drawText(tagName,
                (oneLineTotalWidth-tagNameWidth + dp2px(5)) as Int,
                0 ,
                nickNameWidth ,
                nickNameTxtHeight,
                this)


            /*if(oneLineTotalWidth > width){  // 一行放不下

            }else{

            }*/

            canvas?.drawLine(0f, (height / 2).toFloat(), width.toFloat(), (height / 2).toFloat(), this)
        }
    }

    fun sp2px(spValue: Int): Float {
        return (0.5f + spValue * fontScale) as Float
    }

    fun dp2px(dpValue: Int): Float {
        return (0.5f + dpValue * density) as Float
    }

}