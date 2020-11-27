package gtron.com.gtronsystem.Activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import gtron.com.gtronsystem.FirebaseService.MyFirebaseMessagingService;
import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.AlarmService;
import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;

public class PopupActivity extends AppCompatActivity {

    private String TAG = "PopupActivity";
    private static PowerManager.WakeLock sCpuWakeLock;
    MyFirebaseMessagingService service;
    public static String msg  = "";
    MediaPlayer player;
    AlarmManager mManager;
    SharedPreferences pref;
    Timer timer;
    TimerTask timerTask;
    final long time= 10000;
    final long lastTime = System.currentTimeMillis();
    int i;
    Vibrator vibrator;
    ArrayList<Vibrator> vibratorArrayList = new ArrayList<>();
    TextView tv;
    Notification notification;
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        player = new MediaPlayer();
        mManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        Intent alarmIntent = new Intent("com.test.alarmtestous.ALARM_START");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mManager.set(AlarmManager.RTC_WAKEUP,1000,pendingIntent);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                // 키잠금 해제하기
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                // 화면 켜기
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        tv = (TextView)findViewById(R.id.message);

        service = new MyFirebaseMessagingService();

        tv.setText(msg);

        Log.i("PopupActivity","msg = " + msg);
        Button ib = (Button)findViewById(R.id.btn_ok);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PopupActivity.this, AlarmService.class);
                stopService(intent);

                NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(0);
//                timer.cancel();
//                timerTask.cancel();
                for (int i = 0; i < vibratorArrayList.size(); i++){
                    vibratorArrayList.get(i).cancel();
                }
//                new UpdateMessageNextServer().execute(String.valueOf(Utils.USER_NO),String.valueOf(Utils.USER_SITE_NO));
                onBackPressed();
                overridePendingTransition(0,0);
            }
        });
//            notifyMe();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        tv.setText(intent.getStringExtra("popup"));
    }

    private void notifyMe(){

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        vibratorArrayList.add(vibrator);

        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {

                Log.i(TAG,"insert popup timer");

                Uri alarmSound;
                if(pref.getString("ringtone_path","").length()==0){
                    alarmSound = Uri.parse("android.resource://"+getPackageName()+"/raw/"+"sound1_5s");
                }else{
                    alarmSound = Uri.parse(pref.getString("ringtone_path",""));
                    Log.i(TAG,"alarmSound = " + alarmSound);
                }

                long currentTime = System.currentTimeMillis();
                long[] pattern = {500,500,500,500};
//
//                vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
//                vibrator.vibrate(pattern,-1);

                Intent alarmIntent = new Intent("com.test.alarmtestous.ALARM_START");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                mManager.setRepeating(AlarmManager.RTC_WAKEUP,1000,1000*3,pendingIntent);

                if (pref.getInt("setting_sound",0)==0 || pref.getInt("setting_sound",0)==1){

                    Log.i(TAG,"popup 1111");

                    AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                    if (pref.getBoolean("sound_on",false)){
                        Log.i(TAG,"popup 2222");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Log.i(TAG,"popup 3333");
                            am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, am.getStreamVolume(AudioManager.STREAM_NOTIFICATION), AudioManager.FLAG_PLAY_SOUND);
                        }else{
                            Log.i(TAG,"popup 4444");
                            am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), AudioManager.FLAG_PLAY_SOUND);
                        }
                    }else{
                        Log.i(TAG,"popup 5555");
                        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, am.getStreamVolume(AudioManager.STREAM_NOTIFICATION), AudioManager.FLAG_PLAY_SOUND);
                    }

                    notificationBuilder = new NotificationCompat.Builder(PopupActivity.this)
                            .setSmallIcon(R.mipmap.ic_gtron_logo)
                            .setContentTitle("G-tron System")
                            .setContentText(msg)
                            .setAutoCancel(true)
                            .setTicker(msg)
                            .setContentIntent(pendingIntent)
                            .setSound(alarmSound);

                    notification = notificationBuilder.build();

                }else if (pref.getInt("setting_sound",0)==2){
                    notificationBuilder = new NotificationCompat.Builder(PopupActivity.this)
                            .setSmallIcon(R.mipmap.ic_gtron_logo)
                            .setContentTitle("G-tron System")
                            .setContentText(msg)
                            .setAutoCancel(true)
                            .setTicker(msg)
                            .setContentIntent(pendingIntent)
                            .setVibrate(pattern);
                    notification = notificationBuilder.build();
                }else if (pref.getInt("setting_sound",0)==3){
                    AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
                    if (pref.getBoolean("sound_on",false)){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, am.getStreamVolume(AudioManager.STREAM_NOTIFICATION), AudioManager.FLAG_PLAY_SOUND);
                        }else{
                            am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), AudioManager.FLAG_PLAY_SOUND);
                        }
                    }else{
                        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, am.getStreamVolume(AudioManager.STREAM_NOTIFICATION), AudioManager.FLAG_PLAY_SOUND);
                    }

                    notificationBuilder = new NotificationCompat.Builder(PopupActivity.this)
                            .setSmallIcon(R.mipmap.ic_gtron_logo)
                            .setContentTitle("G-tron System")
                            .setContentText(msg)
                            .setAutoCancel(true)
                            .setTicker(msg)
                            .setContentIntent(pendingIntent)
                            .setVibrate(pattern)
                    .setSound(alarmSound);

                    notification = notificationBuilder.build();

                }

                notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0,notification);

                if (sCpuWakeLock != null) {
                    return;
                }

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setVisibility(Notification.VISIBILITY_PUBLIC);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("0",
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_HIGH);
                    AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build();
                    channel.setSound(alarmSound, att);
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(channel);
                    }
                }

                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                sCpuWakeLock = pm.newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                PowerManager.ON_AFTER_RELEASE, "Getron");

                sCpuWakeLock.acquire();

                if (sCpuWakeLock != null) {
                    sCpuWakeLock.release();
                    sCpuWakeLock = null;
                }

            }
            @Override
            public boolean cancel() {
                vibrator.cancel();
                return super.cancel();
            }
        };


        if(pref.getInt("repeat_cycle",0)!=0 && pref.getInt("repeat_cycle",0)!=5 ){
            if (pref.getInt("repeat_cycle",0)==1){
                i = 60;
            }else if (pref.getInt("repeat_cycle",0)==2){
                i = 60 * 5;
            }else if (pref.getInt("repeat_cycle",0)==3){
                i = 60 * 10;
            }else if (pref.getInt("repeat_cycle",0)==4){
                i = 60 * 30;
            }else if (pref.getInt("repeat_cycle",0)==6){
                i = 5;
            }
            timer.schedule(timerTask, 0, 1000*i);
        }
    }

    public class UpdateMessageNextServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.UpdateMessage());

            http.addOrReplace("userNo",strings[0]);
            http.addOrReplace("siteNo",strings[1]);

            HttpClient post = http.create();

            post.request();
            String body = post.getBody();


            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"update : " + s);

        }
    }
}
