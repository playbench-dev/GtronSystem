package gtron.com.gtronsystem.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.Utils.Utils;

public class ChangePwActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "ChangePwActivity";
    EditText edtPw,edtPwConfirm;
    TextView txtCheckVisible;
    Button btnNext,btnCancel;
    boolean checkStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        FindViewById();

    }

    void FindViewById(){

        edtPw = (EditText)findViewById(R.id.edt_change_pw);
        edtPwConfirm = (EditText)findViewById(R.id.edt_change_pw_confirm);
        btnNext = (Button) findViewById(R.id.btn_change_pw_next);
        btnCancel = (Button)findViewById(R.id.btn_change_pw_cancel);
        txtCheckVisible = (TextView)findViewById(R.id.txt_change_pw_check);

        btnNext.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        edtPw.setFilters(new InputFilter[]{filter});
        edtPwConfirm.setFilters(new InputFilter[]{filter});

        edtPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0){
                    if (edtPw.getText().toString().equals(edtPwConfirm.getText().toString())){
                        txtCheckVisible.setVisibility(View.VISIBLE);
                        txtCheckVisible.setText("비밀번호가 확인되었습니다.");
                        txtCheckVisible.setTextColor(Color.parseColor("#1e52ba"));
                        checkStatus = true;
                    }else{
                        txtCheckVisible.setVisibility(View.VISIBLE);
                        txtCheckVisible.setText("비밀번호를 확인해주세요.");
                        txtCheckVisible.setTextColor(Color.parseColor("#FA9595"));
                        checkStatus = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtPwConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0){
                    if (edtPwConfirm.getText().toString().equals(edtPw.getText().toString())){
                        txtCheckVisible.setVisibility(View.VISIBLE);
                        txtCheckVisible.setText("비밀번호가 확인되었습니다.");
                        txtCheckVisible.setTextColor(Color.parseColor("#1e52ba"));
                        checkStatus = true;
                    }else{
                        txtCheckVisible.setVisibility(View.VISIBLE);
                        txtCheckVisible.setText("비밀번호를 확인해주세요.");
                        txtCheckVisible.setTextColor(Color.parseColor("#FA9595"));
                        checkStatus = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

    public class UpdateUserPwNetworkTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.updateUserPw());

            http.addOrReplace("findId",Utils.USER_EMAIL);
            http.addOrReplace("findPhone",Utils.USER_PHONE);
            http.addOrReplace("userPw",edtPw.getText().toString());

            HttpClient post = http.create();

            post.request();

            String body = post.getBody();

            Log.i(TAG,"UpdateUserPwNetworkTask = " + body);

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(ChangePwActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_change_pw_next: {
                if (checkStatus){
                    new UpdateUserPwNetworkTask().execute();
                    Toast.makeText(this, "Changed", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.btn_change_pw_cancel : {
                onBackPressed();
                break;
            }
        }
    }
}
