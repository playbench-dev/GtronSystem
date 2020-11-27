/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gtron.com.gtronsystem.FirebaseService;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gtron.com.gtronsystem.Activity.PopupActivity;
import gtron.com.gtronsystem.Activity.SplashActivity;
import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.AlarmService;

import static android.app.Notification.CATEGORY_MESSAGE;
import static android.app.Notification.PRIORITY_HIGH;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static gtron.com.gtronsystem.Utils.Utils.PUSH_END_TIME;
import static gtron.com.gtronsystem.Utils.Utils.PUSH_START_TIME;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyNotificationListener";

    private static PowerManager.WakeLock sCpuWakeLock;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    NotificationCompat.Builder notificationBuilder;
    Notification notification;
    NotificationManager notificationManager;
    NotificationCompat.Builder build;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i(TAG,"s");
    }

    @Override
    public void onCreate(){
        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();
        Log.i(TAG,"onCreate");
    }
    // [START receive_message]
    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.i(TAG,"remoteMessage id = "+remoteMessage.getData().get("android_channel_id"));
        Log.i(TAG,"remoteMessage.getData().get(data) = "+remoteMessage.getData().get("body"));

        String date1 = new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()));

        Log.i(TAG,"date1 : " + date1);

        SimpleDateFormat pmAmFormat = new SimpleDateFormat("HH:mm a");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd");

        SimpleDateFormat yyyyMMddFormat1111 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date dateTest = null;

        try {
            dateTest = simpleDateFormat.parse(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try{
            Log.i(TAG,"time start : " + pmAmFormat.format(simpleDateFormat.parse(pref.getString("PUSH_START_TIME",""))));
            Log.i(TAG,"time end : " + pmAmFormat.format(simpleDateFormat.parse(pref.getString("PUSH_END_TIME",""))));

            if (simpleDateFormat.parse(pref.getString("PUSH_START_TIME","")).after(simpleDateFormat.parse(pref.getString("PUSH_END_TIME","")))) {

                Log.i(TAG,"aaaaa");
                if (simpleDateFormat.parse(pref.getString("PUSH_START_TIME", "")).before(dateTest) ||
                        simpleDateFormat.parse(pref.getString("PUSH_END_TIME", "")).after(dateTest)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        showNotification(this, "G-tron System", remoteMessage.getData().get("body"), Integer.parseInt(remoteMessage.getData().get("totalCnt")), remoteMessage.getData().get("android_channel_id"));
                    } else {
                        updateIconBadge(this, (Integer.parseInt(remoteMessage.getData().get("totalCnt"))));
                        sendMessageNotification("G-tron System", remoteMessage.getData().get("body"));
                    }
                }
            }else if (pref.getString("PUSH_START_TIME","").equals(pref.getString("PUSH_END_TIME",""))){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    showNotification(this,"G-tron System",remoteMessage.getData().get("body"),Integer.parseInt(remoteMessage.getData().get("totalCnt")),remoteMessage.getData().get("android_channel_id"));
                }else{
                    updateIconBadge(this, (Integer.parseInt(remoteMessage.getData().get("totalCnt"))));
                    sendMessageNotification("G-tron System",remoteMessage.getData().get("body"));
                }
            }else{
                if (yyyyMMddFormat1111.parse(yyyyMMddFormat.format(new Date(System.currentTimeMillis())) + " " + pref.getString("PUSH_START_TIME","")).before(new Date(System.currentTimeMillis())) && yyyyMMddFormat1111.parse(yyyyMMddFormat.format(new Date(System.currentTimeMillis())) + " " + pref.getString("PUSH_END_TIME","")).after(new Date(System.currentTimeMillis()))){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        showNotification(this,"G-tron System",remoteMessage.getData().get("body"),Integer.parseInt(remoteMessage.getData().get("totalCnt")),remoteMessage.getData().get("android_channel_id"));
                    }else{
                        updateIconBadge(this, (Integer.parseInt(remoteMessage.getData().get("totalCnt"))));
                        sendMessageNotification("G-tron System",remoteMessage.getData().get("body"));
                    }
                }
            }

            Log.i(TAG,"badgeCnt : " + remoteMessage.getData().get("totalCnt"));

            editor.putInt("badgeCnt",(Integer.parseInt(remoteMessage.getData().get("totalCnt"))));
            editor.commit();

        }catch (Exception e){
            Log.e(TAG,"e : " + e);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                showNotification(this,"G-tron System",remoteMessage.getData().get("body"),Integer.parseInt(remoteMessage.getData().get("totalCnt")),remoteMessage.getData().get("android_channel_id"));
            }else{
                updateIconBadge(this, (Integer.parseInt(remoteMessage.getData().get("totalCnt"))));
                sendMessageNotification("G-tron System",remoteMessage.getData().get("body"));
            }
        }
    }

    public static void updateIconBadge(Context context, int notiCnt) {
        Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        badgeIntent.putExtra("badge_count", notiCnt);
        badgeIntent.putExtra("badge_count_package_name", context.getPackageName());
        badgeIntent.putExtra("badge_count_class_name", getLauncherClassName(context));
        context.sendBroadcast(badgeIntent);
    }
    public static String getLauncherClassName(Context context) {
        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    @Override
    public void onTaskRemoved(Intent intent){
        Log.i(TAG,"task");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("InvalidWakeLockTag")
    public void showNotification(Context context, String title, String body, int cnt, String channelId) {

        Intent intent11 = new Intent(this, SplashActivity.class);
        intent11.putExtra("notification","true");
        intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent11, PendingIntent.FLAG_ONE_SHOT);

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        // make the channel. The method has been discussed before.

//        String channelId = "";

        long[] pattern = {1000,1000,1000,1000};
        int[]  pattern1 ={0     , 50   ,  150     , 50, 150};


            if (pref.getString("SOUND","").length() == 0 || pref.getString("SOUND","").contains("sound1_5s")){
                channelId = "sound1_5s.m4a";
            }else if (pref.getString("SOUND","").contains("sound1_1s")){
                channelId = "sound1_1s.m4a";
            }else if (pref.getString("SOUND","").contains("sound1_10s")){
                channelId = "sound1_10s.m4a";
            }else if (pref.getString("SOUND","").contains("sound1_20s")){
                channelId = "sound1_20s.m4a";
            }else if (pref.getString("SOUND","").contains("sound2_5s")){
                channelId = "sound2_5s.m4a";
            }else if (pref.getString("SOUND","").contains("sound2_1s")){
                channelId = "sound2_1s.m4a";
            }else if (pref.getString("SOUND","").contains("sound2_10s")){
                channelId = "sound2_10s.m4a";
            }else if (pref.getString("SOUND","").contains("sound2_20s")){
                channelId = "sound2_20s.m4a";
            }else if (pref.getString("SOUND","").contains("sound3_5s")){
                channelId = "sound3_5s.m4a";
            }else if (pref.getString("SOUND","").contains("sound3_1s")){
                channelId = "sound3_1s.m4a";
            }else if (pref.getString("SOUND","").contains("sound3_10s")){
                channelId = "sound3_10s.m4a";
            }else if (pref.getString("SOUND","").contains("sound3_20s")){
                channelId = "sound3_20s.m4a";
            }else if (pref.getString("SOUND","").contains("sound4_5s")){
                channelId = "sound4_5s.m4a";
            }else if (pref.getString("SOUND","").contains("sound4_1s")){
                channelId = "sound4_1s.m4a";
            }else if (pref.getString("SOUND","").contains("sound4_10s")){
                channelId = "sound4_10s.m4a";
            }else if (pref.getString("SOUND","").contains("sound4_20s")){
                channelId = "sound4_20s.m4a";
            }else if (pref.getString("SOUND","").contains("sound5_5s")){
                channelId = "sound5_5s.m4a";
            }else if (pref.getString("SOUND","").contains("sound5_1s")){
                channelId = "sound5_1s.m4a";
            }else if (pref.getString("SOUND","").contains("sound5_10s")){
                channelId = "sound5_10s.m4a";
            }else if (pref.getString("SOUND","").contains("sound5_20s")){
                channelId = "sound5_20s.m4a";
            }else if (pref.getString("SOUND","").contains("sound6_5s")){
                channelId = "sound6_5s.m4a";
            }else if (pref.getString("SOUND","").contains("sound6_1s")){
                channelId = "sound6_1s.m4a";
            }else if (pref.getString("SOUND","").contains("sound6_10s")){
                channelId = "sound6_10s.m4a";
            }else if (pref.getString("SOUND","").contains("sound6_20s")){
                channelId = "sound6_20s.m4a";
            }else{
                channelId = "sound1_5s.m4a";
            }

        build = new NotificationCompat.Builder(this, channelId);
        build.setContentTitle(title);
        build.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        build.setContentText(body);
        build.setSmallIcon(R.drawable.ic_logo);
        build.setNumber(cnt);
        build.setPriority(PRIORITY_HIGH);
        build.setAutoCancel(true);
        build.setWhen(System.currentTimeMillis());
//        build.setDefaults(Notification.DEFAULT_SOUND);
        build.setVibrate(pattern);
        build.setChannelId(channelId);
        build.setCategory(CATEGORY_MESSAGE);
        build.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        build.setContentIntent(pi);
//        build.setSound(alarmSound);

       notification = build.build();

        if (pref.getBoolean("push_on",true)){
            notificationManager.notify(1,notification);
        }

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.FULL_WAKE_LOCK, "MY TAG" );
        wakeLock.acquire();
        wakeLock.release();

        sCpuWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "WAKELOCK");

        sCpuWakeLock.acquire();

        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }

    @SuppressLint("WrongConstant")
    public void sendMessageNotification(String title, String body){

        if (pref.getBoolean("push_on",true)){
            Intent intent = new Intent(this, SplashActivity.class);
            intent.putExtra("notification","true");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            long[] pattern = {500,500,500,500};

            Uri alarmSound;

            if(pref.getString("SOUND","").contains("default") || pref.getString("SOUND","").contains("defualt")){
                alarmSound = Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound1_5s");
            }else{
                alarmSound = Uri.parse("android.resource://"+getPackageName()+"/raw/"+pref.getString("SOUND","").substring(0,pref.getString("SOUND","").indexOf(".")));
                Log.i(TAG,"alarmSound = " + alarmSound);
            }

            notificationBuilder = new NotificationCompat.Builder(this);


            if (pref.getInt("setting_sound",0) == 0 || pref.getInt("setting_sound",0) == 1){

                AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);

                if (pref.getBoolean("sound_on",false)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamVolume(AudioManager.STREAM_RING), AudioManager.FLAG_PLAY_SOUND);
                    }else{
                        am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), AudioManager.FLAG_PLAY_SOUND);
                    }
                }else{
                    am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamVolume(AudioManager.STREAM_RING), AudioManager.FLAG_PLAY_SOUND);
                }
                notificationBuilder.setSmallIcon(R.mipmap.ic_gtron_logo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setTicker(body)
                        .setAutoCancel(true)
                        .setContentIntent(pi)
                .setSound(alarmSound);

//                MediaPlayer player = MediaPlayer.create(this, alarmSound);
//                player.start();

            }else if (pref.getInt("setting_sound",0) == 2){
                notificationBuilder.setSmallIcon(R.mipmap.ic_gtron_logo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setTicker(body)
                        .setAutoCancel(true)
                        .setContentIntent(pi)
                        .setVibrate(pattern);
            }else if (pref.getInt("setting_sound",0) == 3){
                AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                if (pref.getBoolean("sound_on",false)){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamVolume(AudioManager.STREAM_RING), AudioManager.FLAG_PLAY_SOUND);
                    }else{
                        am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), AudioManager.FLAG_PLAY_SOUND);
                    }
                }else{
                    am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamVolume(AudioManager.STREAM_RING), AudioManager.FLAG_PLAY_SOUND);
                }
                notificationBuilder.setSmallIcon(R.mipmap.ic_gtron_logo)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setTicker(body)
                        .setAutoCancel(true)
                        .setContentIntent(pi)
                        .setVibrate(pattern)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                        .setSound(alarmSound);

//                MediaPlayer player = MediaPlayer.create(this, alarmSound);
//                player.start();
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setCategory(CATEGORY_MESSAGE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVisibility(Notification.VISIBILITY_PUBLIC);
            }

            notification = notificationBuilder.build();

            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0,notification);

//            if (pref.getBoolean("popup_on",true)) {
//                Intent i = new Intent( getApplicationContext(), PopupActivity.class );
//                if (isAppIsInBackground(this)){
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                }else{
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                }
//                i.putExtra("popup",body);
//                PopupActivity.msg = body;
//                PendingIntent pi1 = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
//                try {
//                    pi1.send();
//                } catch (PendingIntent.CanceledException e) {
//                    e.printStackTrace();
//                }
//            }
        }
//        else if (pref.getBoolean("popup_on",true)){
//            Intent i = new Intent( getApplicationContext(), PopupActivity.class );
//            if (isAppIsInBackground(this)){
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            }else{
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            }
//            i.putExtra("popup",body);
//            PopupActivity.msg = body;
//            PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
//            try {
//                pi.send();
//            } catch (PendingIntent.CanceledException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}

