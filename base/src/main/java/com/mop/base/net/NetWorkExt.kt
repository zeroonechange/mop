package com.mop.base.net

import com.drake.net.NetConfig
import com.drake.net.okhttp.setConverter
import java.util.concurrent.TimeUnit


// https://api.unsplash.com/photos/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA
// https://api.unsplash.com/photos/random/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA&count=10
// https://api.unsplash.com/search/photos/?client_id=sihvDbENIqGcVINNkjtayOx4xavvIU32j_3U2Rl2cTA&query="hot girl"&page=1&per_page=10


fun initNetUnsplash() {
    NetConfig.init("https://api.unsplash.com/") {
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)
        writeTimeout(30, TimeUnit.SECONDS)
//        setConverter(SerializationConverter())
        setConverter(GsonConverter())
        retryOnConnectionFailure(true)
//            addInterceptor()
//            cache()
    }
}