package com.mop.base.data

import android.widget.*
import androidx.collection.ArrayMap
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.mop.base.app.BaseApp
import com.mop.base.data.config.GlobalConfig
import com.mop.base.data.interceptor.HeaderInterceptor
import com.mop.base.data.interceptor.logging.Level
import com.mop.base.data.interceptor.logging.LoggingInterceptor
import com.mop.base.utils.*
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.platform.Platform
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

/**
 *
 * 目的1：没网的时候，尝试读取缓存，避免界面空白，只需要addInterceptor和cache即可（已实现）
 * 目的2：有网的时候，总是读取网络上最新的，或者设置一定的超时时间，比如10秒内有多个同一请求，则都从缓存中获取（没实现）
 * 目的3：不同的接口，不同的缓存策略（？）
 */
object HttpRequest {
    private const val mSpName = "http_request_flag"
    private const val mKeyIsSave = "is_save"

    // 缓存 service
    private val mServiceMap = ArrayMap<String, Any>()

    //缓存长超时连接的service
    private val mTimeoutServiceMap = ArrayMap<String, Any>()

    // 默认的 baseUrl
    lateinit var mDefaultBaseUrl: String

    // 默认的请求头
    private var mDefaultHeader: ArrayMap<String, String> = ArrayMap<String, String>()

    /**
     * 存储 baseUrl，以便可以动态更改
     */
    private lateinit var mBaseUrlMap: ArrayMap<String, String>

    /**
     * 请求超时时间，秒为单位
     */
    var mDefaultTimeout = 20L

    /**
     * 添加默认的请求头
     */
    @JvmStatic
    fun addDefaultHeader(name: String, value: String) {
        mDefaultHeader[name] = value
    }


    /**
     * 如果有不同的 baseURL，那么可以相同 baseURL 的接口都放在一个 Service 钟，通过此方法来获取
     */
    @JvmStatic
    fun <T> getService(cls: Class<T>, host: String, vararg interceptors: Interceptor?): T {
        val name = cls.name

        var obj: Any? = mServiceMap[name]
        if (obj == null) {
            obj = crateService(
                cls = cls, host = host, timeOut = mDefaultTimeout, interceptors = interceptors
            )
            mServiceMap[name] = obj
        }
        @Suppress("UNCHECKED_CAST") return obj as T
    }

    /**
     * 设置了 [mDefaultBaseUrl] 后，可通过此方法获取 Service
     * @param token 新的token替换原先的token
     */
    @JvmStatic
    fun <T> getService(cls: Class<T>, token: String? = null): T {
        addCommonHeader(token)
        if (!this::mDefaultBaseUrl.isInitialized) {
            throw RuntimeException("必须初始化 mBaseUrl")
        }

        val headers = HeaderInterceptor(mDefaultHeader)
        return getService(cls, mDefaultBaseUrl, headers)
    }

    /**
     * 可设置超时时间
     */
    @JvmStatic
    fun <T> getServiceWithTimeout(cls: Class<T>, timeOut: Long): T {
        if (!this::mDefaultBaseUrl.isInitialized) {
            throw RuntimeException("必须初始化 mBaseUrl")
        }
        val name = cls.name
        var obj: Any? = mTimeoutServiceMap[name]
        if (obj == null) {
            val headers = HeaderInterceptor(mDefaultHeader)
            obj = crateService(
                cls = cls, host = mDefaultBaseUrl, timeOut = timeOut, headers
            )
            mTimeoutServiceMap[name] = obj
        }
        @Suppress("UNCHECKED_CAST") return obj as T
    }

    private fun <T> crateService(
        cls: Class<T>, host: String, timeOut: Long, vararg interceptors: Interceptor?
    ): T {
        val httpClientBuilder = OkHttpClient.Builder()

        // 超时时间
        httpClientBuilder.connectTimeout(timeOut, TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(timeOut, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(timeOut, TimeUnit.SECONDS)

        // 拦截器
        interceptors.forEach { interceptor ->
            interceptor?.let {
                httpClientBuilder.addInterceptor(it)
            }
        }

        // 日志拦截器，是否打印由 LogUtil 控制
        httpClientBuilder.addInterceptor(
            LoggingInterceptor.Builder() // 打印的等级
                .setLevel(Level.BASIC) // 打印类型
                .log(Platform.INFO) // request 的 Tag
                .request("Request") // Response 的 Tag
                .response("Response").build()
        ) //添加第三方日志拦截器Chucker
        httpClientBuilder.addInterceptor(ChuckerInterceptor(BaseApp.getInstance()))
        val client = httpClientBuilder.build()


        //如果是debug 不进行证书认证
        if (TrustCertPathUtils.isApkInDebug()) {
            TrustCertPathUtils.settingSSLTrust(client)
        }

        val builder = Retrofit.Builder().client(client) // 基础url
            .baseUrl(host) // JSON解析
            .addConverterFactory(GsonConverterFactory.create())

        if (GlobalConfig.gIsNeedChangeBaseUrl) {
            if (!this::mBaseUrlMap.isInitialized) {
                mBaseUrlMap = ArrayMap()
            } // 将 url 缓存起来
            val sp = SPUtils.getInstance(mSpName)
            if (sp.getBoolean(mKeyIsSave)) {
                mBaseUrlMap[host] = sp.getString(host)
            } else {
                mBaseUrlMap[host] = ""
            }

            builder.callFactory {
                LogUtil.i("HttpRequest", "getService: old ${URLDecoder.decode(it.url().toString(), "UTF-8")}")
                mBaseUrlMap.forEach { entry ->
                    val key = entry.key
                    var value = entry.value // 找到 url 并且需要更改
                    val url = it.url().toString()
                    if (url.startsWith(key) && value.isNotEmpty()) { // 防止尾缀有问题
                        if (key.endsWith("/") && !value.endsWith("/")) {
                            value += "/"
                        } else if (!key.endsWith("/") && value.endsWith("/")) {
                            value = value.substring(0, value.length - 1)
                        } // 替换 url 并创建新的 call
                        val newRequest: Request = it.newBuilder().url(HttpUrl.get(url.replaceFirst(key, value))).build()
                        LogUtil.i("HttpRequest", "getService: new ${newRequest.url()}")
                        return@callFactory client.newCall(newRequest)
                    }
                }
                client.newCall(it)
            }
        } // Kotlin 使用协程，Java 使用 rx
        return builder.build().create(cls)
    }

    private fun addCommonHeader(token: String?) {
        addDefaultHeader(
            "Authorization", if (token.isNullOrEmpty()) LoginServiceUtil.getService().token else token
        )
        addDefaultHeader("Content-Type", "application/json")
        addDefaultHeader("SourceType", "ANDROID")
        addDefaultHeader("SourceAppVer", AppUtil.versionName)

        /*  val timeStamp = DateUtils.date2TimeStamp(DateUtil.formatYMDHMS_(), DateUtil.YMDHMS_).toString()
          addDefaultHeader("AppId", "GWM719278367448862")
          addDefaultHeader("Secret", "8bc742859a7849ec9a924c979afa5a00")
          addDefaultHeader("TimeStamp", timeStamp)
          addDefaultHeader(
              "Sign", Sha256Util.encryption(timeStamp + "GWM719278367448862" + "8bc742859a7849ec9a924c979afa5a00")
          )
          addDefaultHeader("DeviceId", DeviceIdUtils.getDeviceId(BaseApp.getInstance()))*/
    }

    /**
     * 同步的请求，当一个界面需要调用多个接口才能呈现出来时，可以在子线程中或者Observable.zip操作多个接口
     */
    @JvmStatic
    fun <T> execute(call: Call<T>): T? {
        try {
            return call.execute().body()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * Retrofit 的原生异步请求，如果你不想使用 Rx，那么可以使用这个

    @JvmStatic
    fun <T, R> request(
    call: Call<T>, callback: CommonObserver<R>
    ): Call<T> {
    callback.onStart()

    call.enqueue(object : Callback<T> {
    override fun onResponse(
    call: Call<T>, response: Response<T>
    ) {
    val baseResponse = response.body()

    @Suppress("UNCHECKED_CAST") val resp = baseResponse as? IBaseResponse<R>
    if (resp == null) {
    callback.onFailed(entityNullable, msgEntityNullable)
    } else {
    callback.onNext(resp)
    }
    callback.onComplete()
    }

    override fun onFailure(
    call: Call<T>, t: Throwable
    ) {
    callback.onError(t)
    callback.onComplete()
    }
    })
    return call
    }
     */
}
