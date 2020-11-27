package gtron.com.gtronsystem.Activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.internal.service.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.SelectViewFunc.SelectTileViewAdapter;
import gtron.com.gtronsystem.SelectViewFunc.SelectViewListAdapter;
import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.Utils.Utils;

public class SelectTileViewActivity extends AppCompatActivity {

    private String TAG = "SelectViewActivity";
    ListView listview ;
    ProgressDialog asyncDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    LinearLayout linearBack;
    LinearLayout linearListParent;
    ArrayList<TextView> textViewArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tile_view);

        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();

        asyncDialog = new ProgressDialog(this);
        linearBack = (LinearLayout)findViewById(R.id.linear_select_view_back);
        linearListParent = (LinearLayout)findViewById(R.id.linear_select_tile_view_list_parent);

        new SelectTileListServer().execute(String.valueOf(Utils.USER_SITE_NO));

        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    void TileSelectListView(final int idx, String title, String columns, String rows, final String tileNo, boolean status){
        View listView = new View(getApplicationContext());
        listView = getLayoutInflater().inflate(R.layout.select_tile_view_list_item,null);
        TextView txtTitle = (TextView)listView.findViewById(R.id.select_tile_view_list_item_title);
        final TextView selectedTextView = (TextView) listView.findViewById(R.id.txt_selected_btn) ;

        linearListParent.addView(listView);
        textViewArrayList.add(selectedTextView);

        txtTitle.setText(title);

        selectedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < textViewArrayList.size(); i++){
                    textViewArrayList.get(i).setText("Not Selected");
                    textViewArrayList.get(i).setTextColor(Color.parseColor("#6A6C70"));
                    textViewArrayList.get(i).setBackgroundResource(R.drawable.btn_02);
                }
                selectedTextView.setText("Selected");
                selectedTextView.setTextColor(Color.parseColor("#ffffff"));
                selectedTextView.setBackgroundResource(R.drawable.btn_01);
                editor.putInt("select_tile_view_position",idx);
                editor.putString("tileNo",tileNo);
                editor.commit();
                Utils.TILE_NO = tileNo;
            }
        });
    }

    public class SelectTileListServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectTileList());
            http.addOrReplace("siteNo",strings[0]);
            http.addOrReplace("activeFlag","1");
            HttpClient post = http.create();
            post.request();
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"s : " + s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    TileSelectListView(i,object.getString("TITLE"),object.getString("COLUMNS"),object.getString("ROWS"),object.getString("TILE_NO"),true);
                }

                try{
                    if (Utils.TILE_NO.length() != 0){
                        textViewArrayList.get(pref.getInt("select_tile_view_position",0)).setText("Selected");
                        textViewArrayList.get(pref.getInt("select_tile_view_position",0)).setTextColor(Color.parseColor("#ffffff"));
                        textViewArrayList.get(pref.getInt("select_tile_view_position",0)).setBackgroundResource(R.drawable.btn_01);
                    }else{
                        textViewArrayList.get(0).setText("Selected");
                        textViewArrayList.get(0).setTextColor(Color.parseColor("#ffffff"));
                        textViewArrayList.get(0).setBackgroundResource(R.drawable.btn_01);
                    }
                }catch (Exception e){

                }
            }catch (JSONException e){

            }
        }
    }
}
