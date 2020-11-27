package gtron.com.gtronsystem;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.stats.WakeLock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import gtron.com.gtronsystem.Utils.UpdateHelper;

public class Application extends android.app.Application {

    String TAG = getClass().getSimpleName();
    private static PowerManager.WakeLock sCpuWakeLock;

    @Override
    public void onCreate() {
        super.onCreate();

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        Map<String,Object> defaultValue = new HashMap<>();
        defaultValue.put(UpdateHelper.KEY_UPDATE_ENABLE,false);
        defaultValue.put(UpdateHelper.KEY_UPDATE_VERSION,""+BuildConfig.VERSION_NAME);

        remoteConfig.setDefaults(defaultValue);
        remoteConfig.fetch(5).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    remoteConfig.activateFetched();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

            deleteNotificationChannel("default");
            deleteNotificationChannel("mp_1s.mp3");
            deleteNotificationChannel("mp_5s.mp3");
            deleteNotificationChannel("mp_9s.mp3");
            deleteNotificationChannel("0");

//            clearApplicationData();

            ActivityManager activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Log.i(TAG,"isBackground : " + activityManager.isBackgroundRestricted());
            }

            makeNotificationChannel("default","default","",NotificationManager.IMPORTANCE_HIGH, null);
            makeNotificationChannel("defualt","defualt","",NotificationManager.IMPORTANCE_HIGH, null);
            makeNotificationChannel("sound1_1s.m4a","sound1_1s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound1_1s"));
            makeNotificationChannel("sound1_5s.m4a","sound1_5s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound1_5s"));
            makeNotificationChannel("sound1_10s.m4a","sound1_10s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound1_10s"));
            makeNotificationChannel("sound1_20s.m4a","sound1_20s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound1_20s"));

            makeNotificationChannel("sound2_1s.m4a","sound2_1s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound2_1s"));
            makeNotificationChannel("sound2_5s.m4a","sound2_5s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound2_5s"));
            makeNotificationChannel("sound2_10s.m4a","sound2_10s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound2_10s"));
            makeNotificationChannel("sound2_20s.m4a","sound2_20s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound2_20s"));

            makeNotificationChannel("sound3_1s.m4a","sound3_1s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound3_1s"));
            makeNotificationChannel("sound3_5s.m4a","sound3_5s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound3_5s"));
            makeNotificationChannel("sound3_10s.m4a","sound3_10s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound3_10s"));
            makeNotificationChannel("sound3_20s.m4a","sound3_20s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound3_20s"));

            makeNotificationChannel("sound4_1s.m4a","sound4_1s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound4_1s"));
            makeNotificationChannel("sound4_5s.m4a","sound4_5s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound4_5s"));
            makeNotificationChannel("sound4_10s.m4a","sound4_10s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound4_10s"));
            makeNotificationChannel("sound4_20s.m4a","sound4_20s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound4_20s"));

            makeNotificationChannel("sound5_1s.m4a","sound5_1s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound5_1s"));
            makeNotificationChannel("sound5_5s.m4a","sound5_5s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound5_5s"));
            makeNotificationChannel("sound5_10s.m4a","sound5_10s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound5_10s"));
            makeNotificationChannel("sound5_20s.m4a","sound5_20s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound5_20s"));

            makeNotificationChannel("sound6_1s.m4a","sound6_1s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound6_1s"));
            makeNotificationChannel("sound6_5s.m4a","sound6_5s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound6_5s"));
            makeNotificationChannel("sound6_10s.m4a","sound6_10s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound6_10s"));
            makeNotificationChannel("sound6_20s.m4a","sound6_20s.m4a","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound6_20s"));

            makeNotificationChannelVib("vibrate","진동","",NotificationManager.IMPORTANCE_HIGH, Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound1_5s"));
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, String description,int importance, Uri soundUri)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(id,name,importance);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.setShowBadge(true);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();

//        long[] pattern = {500,500,500,500,500,500,500,500};

        channel.setDescription(description);
        channel.setSound(soundUri,audioAttributes);
//        channel.setVibrationPattern(pattern);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.FULL_WAKE_LOCK, "MY TAG" );
        wakeLock.acquire();
        // do something.
        // the screen will stay on during this section.
        wakeLock.release();

        sCpuWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "WAKELOCK");

        sCpuWakeLock.acquire();

        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }

        notificationManager.createNotificationChannel(channel);
    }

    @SuppressLint("InvalidWakeLockTag")
    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannelVib(String id, String name, String description,int importance, Uri soundUri)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(id,name,importance);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        channel.setShowBadge(true);

        long[] pattern = {500,500,500,500,500,500,500,500};

        channel.setVibrationPattern(pattern);
        channel.enableVibration(true);
        channel.setDescription(description);
        channel.setSound(null,null);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakeLock = pm.newWakeLock( PowerManager.FULL_WAKE_LOCK, "MY TAG" );
        wakeLock.acquire();
        // do something.
        // the screen will stay on during this section.
        wakeLock.release();

        sCpuWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "WAKELOCK");

        sCpuWakeLock.acquire();

        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }

        notificationManager.createNotificationChannel(channel);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void deleteNotificationChannel(String id)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.deleteNotificationChannel(id);
    }

}
