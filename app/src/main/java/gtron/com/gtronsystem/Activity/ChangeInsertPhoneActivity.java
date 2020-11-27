package gtron.com.gtronsystem.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.Utils.Utils;

public class ChangeInsertPhoneActivity extends AppCompatActivity implements View.OnClickListener{

    final static String TAG  = "ChangeInsertPhone";

    Button btnNext,btnCancel;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String userInfo = "";
    EditText edtPhone,edtEmail;
    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_insert_phone);

        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();

        beforeIntent = getIntent();

        FindViewById();

    }

    void FindViewById(){
        btnNext = (Button) findViewById(R.id.btn_change_insert_phone_next);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtEmail = (EditText)findViewById(R.id.edt_id);
        btnCancel = (Button)findViewById(R.id.btn_change_insert_phone_cancel);

        btnCancel.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        edtEmail.setFilters(new InputFilter[]{filter});
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_change_insert_phone_next: {
                Utils.USER_EMAIL = edtEmail.getText().toString();
                Utils.USER_PHONE = edtPhone.getText().toString();
                if (edtEmail.getText().length()!=0 && edtPhone.getText().length()!=0){
                    new SelectCheckUserInfoNetworkTask().execute();
                }else{

                }

                break;
            }
            case R.id.btn_change_insert_phone_cancel : {
                onBackPressed();
                break;
            }
        }
    }

    public class SelectCheckUserInfoNetworkTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.selectCheckedUserInfo());

            http.addOrReplace("findId",edtEmail.getText().toString());
            http.addOrReplace("findPhone",edtPhone.getText().toString());

            HttpClient post = http.create();

            post.request();

            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"SelectCheckUserInfoNetworkTask = " + s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                if (jsonArray.length() == 0){
                    Toast.makeText(ChangeInsertPhoneActivity.this, "Please check ID and Password", Toast.LENGTH_SHORT).show();
                }else{
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    Utils.SMS_CALLBACK = jsonObject.getString("SMS_CALLBACK");
                    Utils.USER_SITE_NO = jsonObject.getInt("SITE_NO");
                    Intent intent = new Intent(ChangeInsertPhoneActivity.this, CheckNumberActivity.class);
                    intent.putExtra("checkStatus",1);
                    startActivity(intent);
                }
            }catch (JSONException e){

            }
        }
    }
}
