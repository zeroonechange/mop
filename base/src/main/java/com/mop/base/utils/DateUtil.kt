package com.mop.base.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by 杨永青 on 16/9/14.
 */
object DateUtil {
    private const val YMDHM_ = "yyyy-MM-dd HH:mm"
    const val HM = "HH:mm"
    const val YMDHMS_ = "yyyy-MM-dd HH:mm:ss"
    const val YMDHMS_SSS = "yyyy-MM-dd HH:mm:ss.SSS"
    const val YMD_ = "yyyy-MM-dd"
    const val MDHM = "MM-dd HH:mm"

    fun formatYMDHMS_(): String {
        return SimpleDateFormat(YMDHMS_, Locale.getDefault()).format(Date())
    }

    fun formatYMDHMS_SSS(): String {
        return SimpleDateFormat(YMDHMS_SSS, Locale.getDefault()).format(Date())
    }

    fun formatYMDHM_(): String {
        return SimpleDateFormat(YMDHM_, Locale.getDefault()).format(Date())
    }

    fun formatYMDHMS_2Date(time: String): Date {
        return SimpleDateFormat(YMDHMS_, Locale.getDefault()).parse(time) ?: Date()
    }


    fun parseYMD(date: String, addDay: Int): String? {
        try {
            val cal = Calendar.getInstance()
            cal.time = SimpleDateFormat(YMD_, Locale.getDefault()).parse(date)!!
            cal.add(Calendar.DAY_OF_MONTH, addDay)
            return formatYMD_(cal.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 提交后未返回创建修改时间字段
     * 模拟提交到后台的修改时间 当前时间延迟1-2秒
     * **/
    fun submissionTime(): String? {
        try {
            val cal = Calendar.getInstance()
            cal.add(Calendar.SECOND,1)
            return  SimpleDateFormat(YMDHMS_,Locale.getDefault()).format(cal.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formatYMDHMS_()
    }

    /**
     * @param dateStart 2019-02-14 16:13:06
     * @param dateEnd 2019-03-14 16:19:29
     * @return [28,0,6]
     */
    fun parseYMDHMS_ToDHM(dateStart: String, dateEnd: String): IntArray? {
        val df = SimpleDateFormat(YMDHMS_, Locale.getDefault())
        try {
            val start = df.parse(dateStart)!!
            val end = df.parse(dateEnd)!!
            val diff = end.time - start.time//这样得到的差值是微秒级别
            val days = (diff / (1000 * 60 * 60 * 24)).toInt()

            val hours = ((diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)).toInt()
            val minutes =
                ((diff - (days * (1000 * 60 * 60 * 24)).toLong() - (hours * (1000 * 60 * 60)).toLong()) / (1000 * 60)).toInt()
            //  System.out.println("" + days + "天" + hours + "小时" + minutes + "分");
            val arr = IntArray(3)
            arr[0] = days
            arr[1] = hours
            arr[2] = minutes
            return arr
        } catch (ignored: Exception) {
        }

        return null
    }

    fun parseYMDHMS_ToHMS(dateStart: String, dateEnd: String): String {
        return getHhMmSs(parseYMDHMS_ToMilliseconds(dateStart, dateEnd))
    }

    fun parseYMDHMS_ToMilliseconds(dateStart: String, dateEnd: String): Long {
        val df = SimpleDateFormat(YMDHMS_, Locale.getDefault())
        try {
            val start = df.parse(dateStart)!!
            val end = df.parse(dateEnd)!!
            return end.time - start.time//这样得到的差值是微秒级别
        } catch (ignored: Exception) {
        }

        return 0
    }

    fun getHhMmSs(diff: Long): String {
        val hour = diff / (60 * 1000 * 60)
        val min = diff / (60 * 1000) - hour * 60
        val sec = diff / 1000 - hour * 60 * 60 - min * 60
        return "$hour:" + (if (min < 10)
            "0$min"
        else
            min) + ":" + if (sec < 10)
            "0$sec"
        else
            sec
    }

    fun getDdHhMmSs(diff: Long): String {
        var hour = diff / (60 * 1000 * 60)
        val min = diff / (60 * 1000) - hour * 60
        val sec = diff / 1000 - hour * 60 * 60 - min * 60
        val day = hour / 24
        hour %= 24
        return if (day == 0L)
            ""
        else
            day.toString() + "天" + (if (hour < 10)
                "0$hour"
            else
                hour) + ":" + (if (min < 10)
                "0$min"
            else
                min) + ":" + if (sec < 10)
                "0$sec"
            else
                sec
    }

    fun parseYMDHMS_FrontZero(source: String, field: Int, targetLen: Int): String? {
        LogUtil.i("DateUtil", "parseYMDHMS_FrontZero: $source")
        try {
            val calendar = Calendar.getInstance()
            calendar.time = SimpleDateFormat(YMDHM_, Locale.getDefault()).parse(source)!!
            val a = calendar.get(field)
            val str = a.toString()
            val len = str.length
            if (len < targetLen) {
                val builder = StringBuilder()
                for (i in 0 until targetLen - len) {
                    builder.append(0)
                }
                builder.append(a)
                return builder.toString()
            }
            return str
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun formatYMDHMS(): String {
        return SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
    }

    fun formatMDHM(): String {
        return SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()).format(Date())
    }

    fun formatMDHM(time: Long): String {
        return SimpleDateFormat("MM/dd HH:mm", Locale.getDefault()).format(Date(time))
    }

    fun formatYMD_CN(date: Date): String {
        return SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault()).format(date)
    }

    fun formatYMD_(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    fun formatYMD_(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }

    fun formatYMD_HM_CN(date: Date): String {
        return SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault()).format(date)
    }

    fun formatIMG(): String {
        return SimpleDateFormat("'IMG'_yyyyMMddHHmmss'.png'", Locale.getDefault()).format(
            Date()
        )
    }

    fun getClassicsHeaderDateString(date: Date): String {
        if (date == null) return ""

        val calendar = Calendar.getInstance()
        calendar.time = date

        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        calendar.time = Date()

        var currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        val currentYear = calendar[Calendar.YEAR]
        val currentMonth = calendar[Calendar.MONTH]

        if (day == currentDay && month == currentMonth && year == currentYear) {
            return "最后更新:今天${SimpleDateFormat(HM, Locale.getDefault()).format(date)}"
        } else {
            return "最后更新:${SimpleDateFormat(YMDHM_, Locale.getDefault()).format(date)}"
        }
    }


    /**
     * 判断是否同一年
     * **/

    fun isSameDate(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2

        return cal1[Calendar.YEAR] === cal2[Calendar.YEAR]
    }

    /**
     * 判断是否同一天
     * **/

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return cal1[Calendar.YEAR] == cal2[Calendar.YEAR] && cal1[Calendar.MONTH] == cal2[Calendar.MONTH] && cal1[Calendar.DATE] == cal2[Calendar.DATE]
    }


}
