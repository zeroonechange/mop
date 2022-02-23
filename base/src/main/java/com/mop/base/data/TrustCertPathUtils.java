package com.mop.base.data;

import android.app.Application;
import android.content.pm.ApplicationInfo;


import com.mop.base.utils.LogUtil;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Create by xk
 * 2021年1月14日
 *
 * @description: 信任证书
 */
public class TrustCertPathUtils {
    public static Application application;

    public static void initApplication(Application application) {
        TrustCertPathUtils.application = application;
    }

    public static boolean isApkInDebug() {
        if (application==null){
            LogUtil.e("请先初始化Application!!!");
            return false;
        }
        try {
            ApplicationInfo info = application.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static OkHttpClient settingSSLTrust(OkHttpClient okHttpClient) {

        SSLContext sc = null;

        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            }, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }

        HostnameVerifier hv1 = (s, sslSession) -> true;

        String workerClassName = "okhttp3.OkHttpClient";

        try {
            Class workerClass = Class.forName(workerClassName);
            Field hostnameVerifier = workerClass.getDeclaredField("hostnameVerifier");
            hostnameVerifier.setAccessible(true);
            hostnameVerifier.set(okHttpClient, hv1);

            Field sslSocketFactory = workerClass.getDeclaredField("sslSocketFactoryOrNull");
            sslSocketFactory.setAccessible(true);
            sslSocketFactory.set(okHttpClient, sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return okHttpClient;
    }
}
