package gtron.com.gtronsystem.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.Utils.Utils;

public class InsertPhoneActivity extends AppCompatActivity {

    final static String TAG  = "InsertPhoneActivity";

    Button btnNext;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userInfo = "";
    EditText edtPhone;
    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_phone);
//        getSupportActionBar().hide();

        beforeIntent = getIntent();

        btnNext = (Button) findViewById(R.id.btn_insert_phone_next);

        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();

        edtPhone = (EditText)findViewById(R.id.edt_phone);
        edtPhone.setText(Utils.USER_PHONE);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"site no : " + Utils.USER_SITE_NO);
                Utils.USER_PHONE = edtPhone.getText().toString();
                new CodeNumberNetworkTask().execute(String.valueOf(Utils.USER_SITE_NO));
            }
        });
    }

    public class CodeNumberNetworkTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectSmsCallback());

            http.addOrReplace("status","android");
            http.addOrReplace("siteNo",strings[0]);

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
                Utils.SMS_CALLBACK = jsonObject.getString("SMS_CALLBACK");
                Intent intent = new Intent(InsertPhoneActivity.this, CheckNumberActivity.class);
                intent.putExtra("checkStatus",beforeIntent.getIntExtra("checkStatus",0));
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
}
