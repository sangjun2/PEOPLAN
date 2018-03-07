package com.sang.peoplan;

import android.app.Application;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

/**
 * Created by Sangjun on 2018-01-17.
 */

public class GlobalApplication extends Application { // Application 상속 받은 놈이 먼저 실행됨
    private static volatile GlobalApplication instance = null;
    public static final String SERVER_URL = "http://13.125.226.126:9000";

    public static GlobalApplication getGlobalApplicationContext() {
        if(instance == null) {
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this; // ??
        KakaoSDK.init(new KakaoSDKAdapter()); // 카카오 연동을 위한 설정
    }
}
