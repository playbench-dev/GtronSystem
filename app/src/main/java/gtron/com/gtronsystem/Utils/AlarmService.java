package gtron.com.gtronsystem.Utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.List;

import gtron.com.gtronsystem.Activity.PopupActivity;
import gtron.com.gtronsystem.Activity.SplashActivity;
import gtron.com.gtronsystem.R;

import static android.app.Notification.CATEGORY_MESSAGE;
import static android.app.Notification.PRIORITY_HIGH;
import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Created by kmw on 2017. 11. 5..
 */

public class AlarmService extends Service {

    SharedPreferences pref;
    private static PowerManager.WakeLock sCpuWakeLock;
    Vibrator vibrator;
    NotificationManager notificationManager;
    Notification notification;
    NotificationCompat.Builder build;

    int a = 0;

    @Override
    public IBinder onBind(Intent intent) {
        // Service 객체와 (화면단 Activity 사이에서)
        // 통신(데이터를 주고받을) 때 사용하는 메서드
        // 데이터를 전달할 필요가 없으면 return null;
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)

        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= 26) {

            String CHANNEL_ID = pref.getString("SOUND","");

            if (pref.getString("SOUND","").length() == 0){
                CHANNEL_ID = "sound1_5s.m4a";
            }

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
        Log.d("test", "서비스의 onCreate");

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("test", "서비스의 onStartCommand");

        a = 0;

        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        int cnt = intent.getIntExtra("cnt",0);

        Intent intent11 = new Intent(this, SplashActivity.class);
        intent11.putExtra("notification","true");
        intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent11, PendingIntent.FLAG_ONE_SHOT);

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        // make the channel. The method has been discussed before.

        String channelId = "";

        long[] pattern = {0     , 1000   , 1500     , 2000,1000};
        int[]  pattern1 ={0     , 50   ,  150     , 50, 150};

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

//        AlarmService.Test(this);

//        sendBroadcast(new Intent("com.gtron_vib"));

        if (pref.getString("CHANNEL_FLAG","").equals("2")){
            channelId = "vibrate";
            vibrator.vibrate(VibrationEffect.createWaveform(pattern,pattern1,-1));
            handler.sendEmptyMessage(0);
        }else{
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

            if (pref.getString("CHANNEL_FLAG","").equals("3")){
//                vibrator.vibrate(VibrationEffect.createWaveform(pattern,pattern1, -1));
                VibrationEffect.createWaveform(pattern, VibrationEffect.DEFAULT_AMPLITUDE);
                handler.sendEmptyMessage(0);
            }
        }

         build = new NotificationCompat.Builder(this, channelId);
        build.setContentTitle(title);
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
        build.setContentIntent(pi);
//        build.setSound(alarmSound);

        notification = build.build();

        if (pref.getBoolean("push_on",true)){
            notificationManager.notify(1,notification);
        }

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "Getron");

        sCpuWakeLock.acquire();

        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }

        Log.i(TAG,"back : " + isAppIsInBackground(this));

        if (pref.getBoolean("popup_on",true)) {
            Intent i = new Intent( getApplicationContext(), PopupActivity.class );
            if (isAppIsInBackground(this)){
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }else{
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
            i.putExtra("popup",body);
            i.setData(Uri.parse("test://test11"));
            PopupActivity.msg = body;
            PendingIntent pi1 = PendingIntent.getActivity(getApplicationContext(), 0, i, PendingIntent.FLAG_ONE_SHOT);
            try {
                pi1.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
//            startActivity(i);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            long[] pattern = {0     , 1000};
            int[]  pattern1 ={0     , 50};
            vibrator.vibrate(VibrationEffect.createWaveform(pattern,pattern1, -1));

            handler.sendEmptyMessageDelayed(0,2500);

            a++;

            if (pref.getString("SOUND","").contains("1s")){
                handler.removeMessages(0);
            }else if (pref.getString("SOUND","").contains("5s")){
                if (a == 3){
                    handler.removeMessages(0);
                }
            }else if (pref.getString("SOUND","").contains("10s")){
                if (a == 5){
                    handler.removeMessages(0);
                }
            }else if (pref.getString("SOUND","").contains("20s")){
                if (a == 9){
                    handler.removeMessages(0);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
//        mp.stop(); // 음악 종료
        if (vibrator != null){
            vibrator.cancel();
        }

        if (handler != null){
            handler.removeMessages(0);
        }
        notificationManager.cancelAll();
        Log.d("test", "서비스의 onDestroy");
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
