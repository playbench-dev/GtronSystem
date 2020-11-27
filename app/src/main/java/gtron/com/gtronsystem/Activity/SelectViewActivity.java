package gtron.com.gtronsystem.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.SelectViewFunc.SelectViewListAdapter;
import gtron.com.gtronsystem.Utils.Utils;

public class SelectViewActivity extends AppCompatActivity {

    private String TAG = "SelectViewActivity";
    ListView listview ;
    SelectViewListAdapter adapter;
    ProgressDialog asyncDialog;
    SharedPreferences pref;
    LinearLayout linearBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_view);
//        getSupportActionBar().hide();

        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        asyncDialog = new ProgressDialog(this);
        // Adapter 생성
        adapter = new SelectViewListAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.list_select_view);
        linearBack = (LinearLayout)findViewById(R.id.linear_select_view_back);

        new ViewListServer().execute(String.valueOf(Utils.USER_SITE_NO));

        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public class ViewListServer extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("Loading...");
            asyncDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectViewInfoList());
            http.addOrReplace("siteNo",strings[0]);
            HttpClient post = http.create();
            post.request();
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("MainActivity", "1 is success = " + s);
            try {
                JSONObject jsonObjectView = new JSONObject(s);
                JSONArray jsonArrayView = jsonObjectView.getJSONArray("result");
                for (int i = 0; i < jsonArrayView.length(); i++) {
                    JSONObject jsonObject = jsonArrayView.getJSONObject(i);
                    adapter.addItem(jsonObject.getString("VIEW_NAME"),
                            jsonObject.getString("PROCESS_CNT"), jsonObject.getString("SENSOR_CNT"),jsonObject.getString("VIEW_NO"),true);
                }
                listview.setAdapter(adapter);
                asyncDialog.dismiss();
            } catch (Exception e) {

            }
        }
    }
    @Override
    public void onPause(){
        super.onPause();

//        if (pref.getString("view_no","").length()!=0) {
//            new ViewSelectServer().execute(String.valueOf(Utils.USER_NO), pref.getString("view_no", ""));
//        }
    }

    public static class ViewSelectServer extends AsyncTask<String, String, String> {

        private String TAG = "SelectViewActivity";

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.UpdateCurrentView());
            http.addOrReplace("userNo",strings[0]);
            http.addOrReplace("siteNo",strings[1]);
            http.addOrReplace("currentView",strings[2]);
            HttpClient post = http.create();
            post.request();
            String body = post.getBody();

            Log.i(TAG,"ViewSelectServer check : " + body);

            return body;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
