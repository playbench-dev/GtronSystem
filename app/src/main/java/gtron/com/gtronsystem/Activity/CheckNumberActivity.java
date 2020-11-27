package gtron.com.gtronsystem.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import gtron.com.gtronsystem.Utils.Utils;
import gtron.com.gtronsystem.Views.ClearEditText;
import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.R;

public class CheckNumberActivity extends AppCompatActivity {
    final static String TAG  = "CheckNumberActivity";

    TextView txtTime;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userInfo = "";
    ClearEditText edtCode;
    CountDownTimer timer2;
    Button btnNext,btnSendCode,btnCancel;
    int millis = 60;
    int min = 0;
    Intent beforeIntent;
    static String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_number);
//        getSupportActionBar().hide();
        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();
        beforeIntent = getIntent();

        token = FirebaseInstanceId.getInstance().getToken();

        btnNext = (Button) findViewById(R.id.btn_check_number_next);
        btnSendCode = (Button) findViewById(R.id.btn_check_number_send_code);
        edtCode = (ClearEditText)findViewById(R.id.edt_phone_check_code);
        txtTime = (TextView)findViewById(R.id.txt_check_time);
        btnCancel = (Button)findViewById(R.id.btn_check_number_cancel);

        btnSendCode.setEnabled(true);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnSendCode.isEnabled()) {
                    new SMSServer().execute(Utils.USER_PHONE,Utils.SMS_CALLBACK);

                    txtTime.setVisibility(View.VISIBLE);
                    timer2 = new CountDownTimer(60000, 1000) {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onTick(long millisUntilFinished) {
                            btnSendCode.setEnabled(false);
                            btnSendCode.setElevation(1);
                            if (millis != 0){
                                millis--;
                            }else{
                                millis = 59;
                                if (min !=0) {
                                    min--;
                                }
                            }
                            if (millis > 9){
                                txtTime.setText("0" + min + ":" + "" + millis);
                            }else{
                                txtTime.setText("0" + min + ":" + "0" + millis);
                            }
                        }

                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onFinish() {
                            btnSendCode.setEnabled(true);
                            btnSendCode.setElevation(2);
                            min = 0;
                            millis = 59;
                            txtTime.setVisibility(View.INVISIBLE);
                        }
                    };
                    timer2.start();
                }else{
                    Toast.makeText(CheckNumberActivity.this, "인증번호가 이미 전송되었습니다.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CheckNextServer().execute(Utils.USER_PHONE);
            }
        });
    }

    public class SMSServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SmsCertProc());

            http.addOrReplace("recvPhoneNo",strings[0]);
            http.addOrReplace("sendPhoneNo","0317762050");
            http.addOrReplace("siteNo", String.valueOf(Utils.USER_SITE_NO));

            Log.i(TAG,"ssss : " + strings[0] + " ssss : " + strings[1]);

            HttpClient post = http.create();

            post.request();
            String body = post.getBody();


            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"ssss : " + s);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            timer2.cancel();
        } catch (Exception e) {}
        timer2=null;
    }

    public class CheckNextServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectSmsByPhoneNo());
            http.addOrReplace("recvPhoneNo",strings[0]);
            HttpClient post = http.create();
            post.request();
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"sss : " + s);
            try{
                JSONArray jsonArrayCheck = new JSONArray(s);
                JSONObject jsonObjectCheck = jsonArrayCheck.getJSONObject(jsonArrayCheck.length()-1);

                if (jsonObjectCheck.isNull("CODE")==false) {
                    if (jsonObjectCheck.getString("CODE").contains(edtCode.getText().toString()) == true) {
                        new InsertPhoneNextServer().execute(String.valueOf(Utils.USER_NO));
                    } else {
                        Toast.makeText(CheckNumberActivity.this, "인증번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CheckNumberActivity.this, "인증번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){

            }
        }
    }

    public class InsertPhoneNextServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.UpdateUserPhone());
            http.addOrReplace("userNo",strings[0]);
            http.addOrReplace("phone",Utils.USER_PHONE);
            HttpClient post = http.create();

            post.request();
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            if (beforeIntent.getIntExtra("checkStatus",0) != 1){
                new SendregistServer().execute(token, String.valueOf(Utils.USER_NO));
            }else {
                Intent intent = new Intent(CheckNumberActivity.this, ChangePwActivity.class);
                startActivity(intent);
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

            Intent intent = new Intent(CheckNumberActivity.this, TileViewActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            onBackPressed();
        }
    }
}
