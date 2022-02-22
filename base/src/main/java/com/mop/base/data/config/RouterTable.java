package com.mop.base.data.config;

/**
 * 登陆部分路由信息
 */
public interface RouterTable {

    /**
     * 需要登陆的路由分组名
     */
    String NEED_LOGIN = "needLogin";
    /**
     * 我的
     */
    String PATH_SERVICE_MINE = "/module_mine/MineService";
    /**
     * 登录服务
     */
    String PATH_SERVICE_LOGIN = "/module_login/LoginService";
    /**
     * 登录界面
     */
    String PATH_PAGE_LOGIN = "/module_login/LoginDistributeActivity";
    
}
