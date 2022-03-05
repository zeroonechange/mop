package com.mop.base.data.config

class RouterTable {

    companion object{

        /**
         * 需要登陆的路由分组名
         */
        const val NEED_LOGIN = "needLogin"

        /**
         * 我的
         */
        const val PATH_SERVICE_MINE = "/module_mine/MineService"

        /**
         * 登录服务
         */
        const val PATH_SERVICE_LOGIN = "/module_login/LoginService"

        /**
         * 登录界面
         */
        const val PATH_PAGE_LOGIN = "/module_login/LoginDistributeActivity"


        const val DATA_MAIN_ACT = "/data/DataMainActivity"


        const val UI_MAIN_ACT = "/ui/UINavAct"
        const val UI_HOME_ACT = "/ui/HomeAct"

    }
}