/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.urgoo.client;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.growingio.android.sdk.collection.Configuration;
import com.growingio.android.sdk.collection.GrowingIO;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.shrb.hrsdk.HRSDK;
import com.tencent.bugly.crashreport.CrashReport;
import com.urgoo.data.SPManager;
import com.urgoo.message.EaseHelper;
import com.zw.express.tool.image.ImageLoaderUtil;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

public class BaseApplication extends Application {

    public static Context applicationContext;
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        applicationContext = this;
        instance = this;
        ImageLoaderUtil.initImageLoader(this);
        SPManager.getInstance(this);

        //极光注册
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        ShareSDK.initSDK(this);
        HRSDK.getInstance().initWithAppID("0689b939-4c66-4ef0-8841-e2e390cd259e");

        // Fresco初始化
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this).setDownsampleEnabled(true).build();
        Fresco.initialize(this, config);

        //杨德成 20160721 统计分析 测试先注释
        GrowingIO.startWithConfiguration(this, new Configuration("b667b6102e2b4e19")
                .setURLScheme("growing.7c5e86390298801a")
                .useID()
                .setChannel("XXX应用商店")
                .trackAllFragments());


        //杨德成 20160802 添加腾讯bugly
        CrashReport.initCrashReport(getApplicationContext(), "900030352", false);

        EaseHelper.getInstance().init(getApplicationContext());
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
