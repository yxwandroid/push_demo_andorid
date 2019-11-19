package com.wilson.demo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;


public class MyAPP extends Application {
    private String TAG = "wilson_push";

    @Override
    public void onCreate() {
        super.onCreate();

        String AppKey = "5d1ab3d53fc195d690000a13";
        String Secret = "5f555515f05f1db2830d5cc23bb89469";


//        // 在此处调用基础组件包提供的初始化函数 相应信息可在应用管理 -> 应用信息 中找到 http://message.umeng.com/list/apps
// 参数一：当前上下文context；
// 参数二：应用申请的Appkey（需替换）；
// 参数三：渠道名称；
// 参数四：设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机；
// 参数五：Push推送业务的secret 填充Umeng Message Secret对应信息（需替换）
        UMConfigure.init(this, AppKey, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, Secret);

//获取消息推送代理示例
        PushAgent mPushAgent = PushAgent.getInstance(this);


//        mPushAgent.setResourcePackageName("com.wilson.android.demo");

//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG, "注册成功：deviceToken：-------->  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG, "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });


        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {


            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Log.i(TAG, " msg  ：-------->  " + msg.custom);
                Log.i(TAG, " msg  ：-------->  " + msg.extra.toString());
            }

        };

        mPushAgent.setNotificationClickHandler(notificationClickHandler);

    }
}
