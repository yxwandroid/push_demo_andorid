package com.wilson.demo;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UHandler;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.Map;


public class App extends Application implements Application.ActivityLifecycleCallbacks {
    private String TAG = "wilson_push";
    public static int count = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
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
                Log.i(TAG, "注册成功 onSuccess ：deviceToken：-------->  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.i(TAG, "注册失败 onFailure：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });


        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {


            @Override
            public void launchApp(Context context, UMessage uMessage) {
                Log.i(TAG, " launchApp  ：-------->  " + uMessage.custom);
            }

            @Override
            public void openUrl(Context context, UMessage uMessage) {
                Log.i(TAG, " openUrl  ：-------->  " + uMessage.custom);
            }

            @Override
            public void openActivity(Context context, UMessage uMessage) {
                Log.i(TAG, " openActivity  ：-------->  " + uMessage.custom);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Log.i(TAG, " dealWithCustomAction  ：-------->  " + msg.custom);
            }


        };

        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public void handleMessage(Context context, UMessage uMessage) {
                super.handleMessage(context, uMessage);

                Log.i(TAG, " handleMessage  ：-------->  "+count +"---"+ uMessage);
                String manufacturer = Build.MANUFACTURER.toLowerCase();
                if (TextUtils.isEmpty(manufacturer)) {
                    return;
                }
                if (manufacturer.contains("xiaomi")) {
                   return;
                }else{
                    count++;
                    BadgerUtil.addBadger(context, count);
                }
            }

            @Override
            public Notification getNotification(Context context, UMessage uMessage) {

                String manufacturer = Build.MANUFACTURER.toLowerCase();
                if (TextUtils.isEmpty(manufacturer)) {
                      return super.getNotification(context, uMessage);
                }
                if (manufacturer.contains("xiaomi")) {
                    count++;
                    Notification notification = new NotificationCompat.Builder(context, "badge")
                            .setContentTitle(uMessage.title)
                            .setContentText(uMessage.text)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                            .setNumber(count)
                            .setAutoCancel(true)
                            .build();
                    return notification;
                }else{
                    return super.getNotification(context, uMessage);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);


    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        count =0;
        BadgerUtil.addBadger(activity, count);
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
//        if(AppUtil.isBackground(this)) {
//            BadgerUtil.addBadger(this, count);
//        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
