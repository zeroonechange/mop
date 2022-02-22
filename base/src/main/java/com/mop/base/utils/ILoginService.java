package com.mop.base.utils;


import com.alibaba.android.arouter.facade.template.IProvider;

public interface ILoginService extends IProvider {

    String getUserId();

    void logout( );
    void refreshToken(String  token);
    void setName(String  token);
    void setPic(String  token);
    void setLv(String  lvstr);
    void clearLoginData();

    String getToken();

    String getPhoneNum();

    String getAccessToken();

    String getTokenId();

    String getBeanId();
    String getLvStr();

    String getNickName();

    String getSex();
    String getAccessId();
    String getUserpicture();

    String getAvatar();

    String getSwellvehicleVO();

    String getObjects();

    String getSsoUserId();

    boolean isLogin();

    String getPtToken();

    String getMallToken();


    interface LoginStateChangedListener {
        void onLoginChanged(boolean isLogin);
    }
}
