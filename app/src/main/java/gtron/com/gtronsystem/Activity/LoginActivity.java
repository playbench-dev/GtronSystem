package gtron.com.gtronsystem.Activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Pattern;

import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.Utils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    final static String TAG  = "LoginActivity";
    TextView txtLoginBool;
    EditText edtId,edtPw;
    static String token;
    Button btnPwChange,btnLogin;

    int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();

        FindViewById();

        editor.putInt("login_flag",0);
        editor.putString("userEmail","");
        editor.putString("userPw","");
        editor.commit();

//        startOverlayWindowService(this);

    }

    public void startOverlayWindowService(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !Settings.canDrawOverlays(context)) {
            onObtainingPermissionOverlayWindow();
        } else {
//            onStartOverlay();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void onObtainingPermissionOverlayWindow() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 1234);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.i(TAG,"token : " + Utils.TOKEN);

            try{
                if (Utils.TOKEN == null){
                    Utils.TOKEN = FirebaseInstanceId.getInstance().getToken();
                    mHandler.sendEmptyMessageDelayed(0,2000);
                }else if (Utils.TOKEN.length() == 0){
                    Utils.TOKEN = FirebaseInstanceId.getInstance().getToken();
                    mHandler.sendEmptyMessageDelayed(0,2000);
                }else{
                    Log.i(TAG,"before login USER_SITE_NO : " + Utils.USER_SITE_NO);
                    new LoginNetworkTask().execute(edtId.getText().toString(),edtPw.getText().toString());
                    mHandler.removeMessages(0);
                }
            }catch (NullPointerException e){
                Utils.TOKEN = FirebaseInstanceId.getInstance().getToken();
                mHandler.sendEmptyMessageDelayed(0,2000);
            }
        }
    };

    void FindViewById()
    {
        edtId = (EditText)findViewById(R.id.edt_login_id);
        edtPw = (EditText)findViewById(R.id.edt_login_password);
        btnLogin = (Button) findViewById(R.id.btn_login_login);
        txtLoginBool = (TextView)findViewById(R.id.txt_login_false);
        btnPwChange = (Button)findViewById(R.id.btn_login_pw_changes);

        btnLogin.setOnClickListener(this);
        btnPwChange.setOnClickListener(this);

        edtId.setFilters(new InputFilter[]{filter});
        edtPw.setFilters(new InputFilter[]{filter});
    }

    protected InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {

            Pattern pattern = Pattern.compile("^[가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025\\u00B7\\uFE55]+$");

            if (pattern.matcher(charSequence).matches()){
                return "";
            }
            return null;
        }
    };

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

    public class LoginNetworkTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.LoginProcess());

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
            Log.i(TAG,"s = " + s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("result")){
                    if (jsonObject.getInt("LEVEL") == 1){

                        editor.putString("userEmail",edtId.getText().toString());
                        editor.putString("userPw",edtPw.getText().toString());

                        Utils.USER_EMAIL = jsonObject.getString("USER_EMAIL");
                        Utils.USER_PHONE = jsonObject.getString("PHONE");
                        Utils.USER_NAME = jsonObject.getString("USER_NAME");
                        Utils.USER_LEVEL = jsonObject.getInt("LEVEL");
                        Utils.USER_SITE_NO = jsonObject.getInt("SITE_NO");
                        Utils.USER_NO = jsonObject.getInt("USER_NO");
                        Utils.SMS_ENABLE = jsonObject.getInt("SMS_ENABLE");
                        Utils.SMS_CERT = jsonObject.getInt("SMS_CERT");
                        Utils.SMS_RECEIVE = jsonObject.getInt("SMS_RECEIVE");
                        Utils.MONITORING = jsonObject.getInt("MONITORING");
                        Utils.LOGO_PATH = Utils.JsonIsNullCheck(jsonObject,"LOGO_PATH_1");

                        if (Utils.JsonIsNullCheck(jsonObject,"PUSH_START_TIME").length()==0){
                            Utils.PUSH_START_TIME = "09:00";
                            editor.putString("PUSH_START_TIME","09:00");
                        }else{
                            Utils.PUSH_START_TIME = Utils.JsonIsNullCheck(jsonObject,"PUSH_START_TIME");
                            editor.putString("PUSH_START_TIME",Utils.PUSH_START_TIME);
                        }
                        if (Utils.JsonIsNullCheck(jsonObject,"PUSH_END_TIME").length()==0){
                            Utils.PUSH_END_TIME = "18:00";
                            editor.putString("PUSH_END_TIME","18:00");
                        }else{
                            Utils.PUSH_END_TIME = Utils.JsonIsNullCheck(jsonObject,"PUSH_END_TIME");
                            editor.putString("PUSH_END_TIME",Utils.PUSH_END_TIME);
                        }
                        Utils.PUSH_FLAG = Utils.JsonIsNullCheck(jsonObject,"PUSH_FLAG");
                        Utils.POPUP_FLAG = Utils.JsonIsNullCheck(jsonObject,"POPUP_FLAG");
                        Utils.SOUND = Utils.JsonIsNullCheck(jsonObject,"SOUND");

                        Utils.CHANNEL_FLAG = Utils.JsonIsNullCheck(jsonObject,"CHANNEL_FLAG");
                        Utils.SITE_SMS_ENABLE = jsonObject.getInt("SITE_SMS_ENABLE");

                        editor.putString("SOUND",Utils.SOUND);
                        editor.putString("CHANNEL_FLAG",Utils.CHANNEL_FLAG );

                        editor.apply();

                        Log.i(TAG,"after login USER_SITE_NO : " + Utils.USER_SITE_NO);

                        new SendregistServer().execute(Utils.TOKEN, String.valueOf(Utils.USER_NO));

                    }else{
                        Toast.makeText(LoginActivity.this, "Manager ID can not Login to Mobile service", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    txtLoginBool.setVisibility(View.VISIBLE);
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
            http.addOrReplace("token",strings[0]);
            http.addOrReplace("siteNo", String.valueOf(Utils.USER_SITE_NO));
            http.addOrReplace("user_no",strings[1]);

            HttpClient post = http.create();

            post.request();
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"SendregistServer : " + s);
            editor.putInt("login_flag",1);
            editor.commit();
            if (Utils.MONITORING == 2){
                Intent intent = new Intent(LoginActivity.this, PushMainActivity.class);
                startActivity(intent);
                onBackPressed();
            }else{
                if (Utils.SITE_SMS_ENABLE == 1){
                    if (Utils.SMS_CERT == 1 || Utils.SMS_ENABLE == 2){
                        new SelectMessageNextServer().execute(String.valueOf(Utils.USER_NO), String.valueOf(Utils.USER_SITE_NO));
                    }else{
                        Intent intent = new Intent(LoginActivity.this, InsertPhoneActivity.class);
                        startActivity(intent);
                    }
                }else{
                    new SelectMessageNextServer().execute(String.valueOf(Utils.USER_NO), String.valueOf(Utils.USER_SITE_NO));
                }
            }
        }
    }

    public class SelectMessageNextServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectMessageList());
            http.addOrReplace("userNo",strings[0]);
            http.addOrReplace("siteNo",strings[1]);
            HttpClient post = http.create();

            post.request();
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"select : " + s);

            int badgeCnt = 0;
            try {
                JSONObject jsonObjectMS = new JSONObject(s);
                JSONArray jsonArrayMS = jsonObjectMS.getJSONArray("list");
                for (int i = 0; i < jsonArrayMS.length(); i++){
                    JSONObject jsonObject = jsonArrayMS.getJSONObject(i);
                    if (jsonObject.getInt("ALIVE_FLAG")==1){
                        badgeCnt++;
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

//        int importance = NotificationManager.IMPORTANCE_HIGH;
//
                    Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pi = PendingIntent.getActivity(LoginActivity.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                    // make the channel. The method has been discussed before.

                    Notification notification = new NotificationCompat.Builder(LoginActivity.this, pref.getString("ringtone_path",""))
                            .setContentText(""+badgeCnt+"Number of messages never read")
                            .setSmallIcon(R.mipmap.ic_logo)
                            .setNumber(badgeCnt)
                            .setAutoCancel(true)
                            .setVisibility(Notification.VISIBILITY_SECRET)
                            .setContentIntent(pi)
                            .build();

                    NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

                    if (badgeCnt > 0){
                        notificationManager.notify(1,notification);
                    }
                }else{
                    updateIconBadge(getApplicationContext(),badgeCnt);
                }

                editor.putInt("badgeCnt",badgeCnt);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, TileViewActivity.class);
                startActivity(intent);
                onBackPressed();

            }catch (Exception e){

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login_login: {
                if(edtId.getText().toString().length() == 0 || edtPw.getText().toString().length() == 0){
                    txtLoginBool.setVisibility(View.VISIBLE);
                }else{
                    mHandler.sendEmptyMessage(0);
                }
                break;
            }
            case R.id.btn_login_pw_changes : {
                Intent intent = new Intent(LoginActivity.this,ChangeInsertPhoneActivity.class);
                intent.putExtra("checkStatus",1);
                startActivity(intent);
                break;
            }
        }
    }
}
