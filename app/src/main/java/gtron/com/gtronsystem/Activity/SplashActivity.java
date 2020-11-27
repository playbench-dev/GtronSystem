package gtron.com.gtronsystem.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.AlarmService;
import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.Utils.UpdateHelper;
import gtron.com.gtronsystem.Utils.Utils;

import static gtron.com.gtronsystem.Utils.Utils.SMS_CERT;
import static gtron.com.gtronsystem.Utils.Utils.SMS_ENABLE;
import static gtron.com.gtronsystem.Utils.Utils.TOKEN;

public class SplashActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener{

    private String TAG = "SplashActivity";
    private final int SPLASHACTIVITY_DISPLAY_LENGTH = 500;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    static String token;
    int sum = 0;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);

        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        Utils.TOKEN = task.getResult().getToken();
                        Log.d(TAG, Utils.TOKEN);
                    }
                });

        FirebaseInstanceId.getInstance().getInstanceId().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SplashActivity.this, "Failed to get token Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public void onUpdateCheckListener(String urlApp) {
        Log.i(TAG,"url : " + urlApp);
        if (urlApp.equals("true")){
            final String appPackageName = getPackageName();
            final View innerView = getLayoutInflater().inflate(R.layout.update_popup, null);
            dialog = new Dialog(SplashActivity.this);
            dialog.setContentView(innerView);

            TextView txtUpdate = (TextView)dialog.findViewById(R.id.txt_update_now);

            Display display = getWindowManager().getDefaultDisplay();
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = params.MATCH_PARENT;
            params.height = params.WRAP_CONTENT;
            dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);

            dialog.show();
            dialog.setCancelable(false);

            txtUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });

        }else{
            if(pref.getInt("login_flag",0) == 0){
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, SPLASHACTIVITY_DISPLAY_LENGTH);
            }else{
                if (pref.getString("userEmail","").length() == 0 || pref.getString("userPw","").length() == 0){
                    new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, SPLASHACTIVITY_DISPLAY_LENGTH);
                }else{
                    mHandler.sendEmptyMessage(0);
                }
            }
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.i(TAG,"token : " + token);
            try{
                if (token == null){
                    token = FirebaseInstanceId.getInstance().getToken();
                    mHandler.sendEmptyMessageDelayed(0,2000);
                }else if (token.length() == 0){
                    token = FirebaseInstanceId.getInstance().getToken();
                    mHandler.sendEmptyMessageDelayed(0,2000);
                }else{
                    Log.i(TAG,"before login USER_SITE_NO : " + Utils.USER_SITE_NO + " email : " + pref.getString("userEmail","") + ", pw : " + pref.getString("userPw", ""));
                    new LoginNetworkTask().execute(pref.getString("userEmail",""),pref.getString("userPw",""));
                    mHandler.removeMessages(0);
                }
            }catch (NullPointerException e){
                token = FirebaseInstanceId.getInstance().getToken();
                mHandler.sendEmptyMessageDelayed(0,2000);
            }
        }
    };

    public class LoginNetworkTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.LoginProcess());

            Log.i(TAG,"e : " + strings[0] + " p : " + strings[1]);
            http.addOrReplace("status","android");
            http.addOrReplace("email",strings[0]);
            http.addOrReplace("password",strings[1]);

            HttpClient post = http.create();

            post.request();

            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"LoginNetworkTask s = " + s);

            if (s.length() == 0){
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("result")){
                    if (jsonObject.getInt("LEVEL") == 1){

                        Utils.USER_EMAIL = jsonObject.getString("USER_EMAIL");
                        Utils.USER_PHONE = jsonObject.getString("PHONE");
                        Utils.USER_NAME = jsonObject.getString("USER_NAME");
                        Utils.USER_LEVEL = jsonObject.getInt("LEVEL");
                        Utils.USER_SITE_NO = jsonObject.getInt("SITE_NO");
                        Utils.USER_NO = jsonObject.getInt("USER_NO");
                        Utils.SITE_SMS_ENABLE = jsonObject.getInt("SITE_SMS_ENABLE");
                        SMS_ENABLE = jsonObject.getInt("SMS_ENABLE");
                        SMS_CERT = jsonObject.getInt("SMS_CERT");
                        Utils.SMS_RECEIVE = jsonObject.getInt("SMS_RECEIVE");
                        Utils.MONITORING = jsonObject.getInt("MONITORING");
                        Utils.LOGO_PATH = Utils.JsonIsNullCheck(jsonObject,"LOGO_PATH_1");

                        if (Utils.JsonIsNullCheck(jsonObject,"PUSH_START_TIME").length()==0){
                            Utils.PUSH_START_TIME = "09:00";
                        }else{
                            Utils.PUSH_START_TIME = Utils.JsonIsNullCheck(jsonObject,"PUSH_START_TIME");
                        }
                        if (Utils.JsonIsNullCheck(jsonObject,"PUSH_END_TIME").length()==0){
                            Utils.PUSH_END_TIME = "18:00";
                        }else{
                            Utils.PUSH_END_TIME = Utils.JsonIsNullCheck(jsonObject,"PUSH_END_TIME");
                        }
                        Utils.PUSH_FLAG = Utils.JsonIsNullCheck(jsonObject,"PUSH_FLAG");
                        Utils.POPUP_FLAG = Utils.JsonIsNullCheck(jsonObject,"POPUP_FLAG");
                        Utils.SOUND = Utils.JsonIsNullCheck(jsonObject,"SOUND");
                        Utils.CHANNEL_FLAG = Utils.JsonIsNullCheck(jsonObject,"CHANNEL_FLAG");

                        editor.putString("PUSH_START_TIME",Utils.PUSH_START_TIME);
                        editor.putString("PUSH_END_TIME",Utils.PUSH_END_TIME);
                        editor.putString("SOUND",Utils.SOUND);
                        editor.putString("CHANNEL_FLAG",Utils.CHANNEL_FLAG );
                        editor.apply();


                        Log.i(TAG,"after login USER_SITE_NO : " + Utils.USER_SITE_NO);
                        new SendregistServer().execute(String.valueOf(Utils.USER_NO), token);

                    }else{
                        Toast.makeText(SplashActivity.this, "Manager ID can not Login to Mobile service", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class SendregistServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.GcmConnection());
            http.addOrReplace("status","android");
            http.addOrReplace("user_no",strings[0]);
            http.addOrReplace("token",strings[1]);
            http.addOrReplace("siteNo", String.valueOf(Utils.USER_SITE_NO));

            HttpClient post = http.create();

            post.request();
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {

            editor.putInt("login_flag",1);
            editor.commit();
            if (Utils.MONITORING == 2){
                Intent intent = new Intent(SplashActivity.this, PushMainActivity.class);
                startActivity(intent);
                onBackPressed();
            }else{
                Log.i(TAG,"CERT : " + SMS_CERT + " ENABLE : " + SMS_ENABLE);
                if (Utils.SITE_SMS_ENABLE == 1){
                    if (SMS_CERT == 2 && SMS_ENABLE == 1){
                        Intent intent = new Intent(SplashActivity.this, InsertPhoneActivity.class);
                        startActivity(intent);
                    }else{
                        Intent beforeIntent = getIntent();
                        if (beforeIntent.hasExtra("title")){
                            Intent intent = new Intent(SplashActivity.this, MessageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("notification","true");
                            startActivity(intent);
                            onBackPressed();
                        }else{
                            Intent intent = null;
                            if (pref.getString("viewType","").length() == 0 || pref.getString("viewType","").equals("tile")){
                                intent = new Intent(SplashActivity.this, TileViewActivity.class);
                            }else{
                                intent = new Intent(SplashActivity.this, MainActivity.class);
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            onBackPressed();
                        }
                    }
                }else{
                    Intent beforeIntent = getIntent();
                    if (beforeIntent.hasExtra("title")){
                        Intent intent = new Intent(SplashActivity.this, MessageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("notification","true");
                        startActivity(intent);
                        onBackPressed();
                    }else{
                        Intent intent = null;
                        if (pref.getString("viewType","").length() == 0 || pref.getString("viewType","").equals("tile")){
                            intent = new Intent(SplashActivity.this, TileViewActivity.class);
                        }else{
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        onBackPressed();
                    }
                }

            }
        }
    }

    private void sendMessage(String sendMessage){
        Intent intent = new Intent("update");
        intent.putExtra("message", sendMessage);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateHelper.with(this).onUpdateCheck(this).check();
    }

}