package com.mop.base.utils;

import android.app.Activity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.mop.base.data.config.RouterTable;


public class LoginServiceUtil {
    /**
     * @param param1
     * @param param2
     */
    public static void navigateCartPage(String param1, String param2) {
        ARouter.getInstance()
                .build(RouterTable.PATH_SERVICE_MINE)
                .withString("key1", param1)
                .withString("key2", param2)
                .navigation();
    }

    /**
     * 获取服务
     *
     * @return
     */
    public static ILoginService getService() {
        return (ILoginService) ARouter.getInstance().build(RouterTable.PATH_SERVICE_LOGIN).navigation();
    }


    /**
     * 路由去登录界面
     *
     * @return
     */
    public static void goToLogin() {
        ARouter.getInstance()
                .build(RouterTable.PATH_PAGE_LOGIN).greenChannel()
                .navigation();
    }

    /**
     * 路由去登录界面,带返回值
     *
     * @param requestCode >0
     * @return
     */
    public static void goToLogin(Activity activity, int requestCode) {
        ARouter.getInstance()
                .build(RouterTable.PATH_PAGE_LOGIN).greenChannel()
                .navigation(activity, requestCode);
    }
}
