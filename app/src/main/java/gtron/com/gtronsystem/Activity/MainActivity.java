package gtron.com.gtronsystem.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.GridView;
import android.widget.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gtron.com.gtronsystem.Utils.BackPressCloseHandler;
import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.SensorFunc.DynamicGridView;
import gtron.com.gtronsystem.SensorFunc.SensorCellAdapter;
import gtron.com.gtronsystem.Utils.Utils;

import static gtron.com.gtronsystem.Utils.Utils.NullCheck;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "MainActivity";
    private long backKeyPressedTime = 0;
    private BackPressCloseHandler backPressCloseHandler;
    DynamicGridView grd1,grd2,grd3,grd4,grd5,grd6;
    TextView txtGrid1,txtGrid2,txtGrid3,txtGrid4,txtGrid5,txtGrid6;
    ImageView btnFold1,btnFold2,btnFold3,btnFold4,btnFold5,btnFold6;
    LinearLayout linGrid1,linGrid2,linGrid3,linGrid4,linGrid5,linGrid6;
    ImageView imgSideMonitoringArrow,imgSideSelectViewArrow;

    String userInfo = "";
    TextView navName,navEmail;
    Notification notification;
    String test = "";

    TextView txtViewName,txtProcessCnt,txtSensorCnt;
    ProcessListServer processListServer;
    SensorCellAdapter adt1,adt2,adt3,adt4,adt5,adt6;

    int currentColor = Color.RED;
    int sensorCnt = 0;

    ArrayList<ArrayList<Integer>> port01ArrayList = new ArrayList<>();
    ArrayList<ArrayList<String>> port01DateArrayList = new ArrayList<>();

    ArrayList<String> portParentArrayList = new ArrayList<>();

    CountDownTimer timer2;

    static String token;
    ProgressDialog asyncDialog;
    static boolean oneCheck = false;
    private DrawerLayout drawerLayout;
    private View drawerView;
    ImageView imgHamber;
    ImageView imgMore;
    LinearLayout linearHamber;
    LinearLayout linearMonitor,linearSelect,linearMessage,linearSetting,linearLogout;
    LinearLayout linearMonitorChildVisible,linearSelectChildVislble;
    TextView txtMonitorChildProcess,txtMonitorChildTile,txtSelectChildProcess,txtSelectChildTile;
    ArrayList<String> colorArrayList = new ArrayList<>();
    ArrayList<Integer> priorityArrayList = new ArrayList<>();
    ArrayList<Integer> port01List = new ArrayList<>();
    ArrayList<String> port01DateList = new ArrayList<>();
    int testI=0;

    ArrayList<String> testProcessNameList = new ArrayList<>();
    ArrayList<String> testProcessNoList = new ArrayList<>();
    ArrayList<ArrayList<String>> testSensorNameArray = new ArrayList<>();
    ArrayList<ArrayList<String>> testSensorIdArray = new ArrayList<>();
    ArrayList<ArrayList<String>> testSensorNoArray = new ArrayList<>();
    int colorNumber = 8;
    ArrayList<Integer> priorityNumber = new ArrayList<>();

    ArrayList<ArrayList<Integer>> sensorModelNoArray = new ArrayList<>();
    ArrayList<ArrayList<String>> sensorProcessArray = new ArrayList<>();
    ArrayList<ArrayList<String>> sensorLineArray = new ArrayList<>();
    ArrayList<ArrayList<String>> sensorLocationArray = new ArrayList<>();
    ArrayList<ArrayList<String>> sensorGroupArray = new ArrayList<>();

    public static int aa = 0;

    ArrayList<ArrayList<String>>    colorListArray = new ArrayList<>();
    ArrayList<ArrayList<String>>    txtColorListArray = new ArrayList<>();
    ArrayList<ArrayList<String>>    nameListArray = new ArrayList<>();
    ArrayList<ArrayList<Integer>>   priorityListArray = new ArrayList<>();
    ArrayList<ArrayList<Integer>>   portNoListArray = new ArrayList<>();

    ArrayList<String> colorList = new ArrayList<>();
    ArrayList<String> txtColorList = new ArrayList<>();
    ArrayList<String> nameList = new ArrayList<>();
    ArrayList<Integer> priorityList = new ArrayList<>();
    ArrayList<Integer> portNoList = new ArrayList<>();

    ArrayList<String> colorList1 = new ArrayList<>();
    ArrayList<String> txtColorList1 = new ArrayList<>();
    ArrayList<String> nameList1 = new ArrayList<>();
    ArrayList<Integer> priorityList1 = new ArrayList<>();
    ArrayList<Integer> portNoList1 = new ArrayList<>();

    public static String allOffColor = "";

    LinearLayout linearStatusBar;

    WindowManager wm;
    Display display;

    LinearLayout linearMessageVisible;
    TextView txtMessageReceive,txtMessageSend;
    public static FrameLayout frameBadge;
    public static TextView txtBadge;

    //
    public static AlertDialog.Builder builder;
    public static LinearLayout popupLayout;
    public static TextView txtProcess,txtName,txtGroup,txtLocation;
    public static LinearLayout linearStatus;
    public static Button btnCancel;
    public static AlertDialog dialog;

    public static FrameLayout linearDialogTest;
    public static TextView txtLandProcess,txtLandName,txtLandGroup,txtLandLocation;
    public static LinearLayout linearLandStatus;
    public static Button btnLandCancel;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ImageView imgLogo;

    SwipeRefreshLayout swipeRefreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        backPressCloseHandler = new BackPressCloseHandler(this);

        NullCheck(this);

        dialogTest();

        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();

        editor.putString("viewType","process");
        editor.apply();

        Log.i(TAG,"tileNo : " + pref.getString("tileNo",""));
        Log.i(TAG,"siteNo : " + pref.getString("siteNo",""));

        Intent intent = getIntent();
        test = intent.getStringExtra("test");

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_main_process_view);
        grd1 = (DynamicGridView)findViewById(R.id.grid_1);
        grd2 = (DynamicGridView)findViewById(R.id.grid_2);
        grd3 = (DynamicGridView)findViewById(R.id.grid_3);
        grd4 = (DynamicGridView)findViewById(R.id.grid_4);
        grd5 = (DynamicGridView)findViewById(R.id.grid_5);
        grd6 = (DynamicGridView)findViewById(R.id.grid_6);
        btnFold1 = (ImageView)findViewById(R.id.btn_fold_1);
        btnFold2 = (ImageView)findViewById(R.id.btn_fold_2);
        btnFold3 = (ImageView)findViewById(R.id.btn_fold_3);
        btnFold4 = (ImageView)findViewById(R.id.btn_fold_4);
        btnFold5 = (ImageView)findViewById(R.id.btn_fold_5);
        btnFold6 = (ImageView)findViewById(R.id.btn_fold_6);

        txtViewName = (TextView)findViewById(R.id.select_view_list_item_title);
        txtProcessCnt = (TextView)findViewById(R.id.select_view_list_item_process_cnt);
        txtSensorCnt = (TextView)findViewById(R.id.select_view_list_item_sensor_cnt);
        txtGrid1 = (TextView)findViewById(R.id.txt_process_name_1);
        txtGrid2 = (TextView)findViewById(R.id.txt_process_name_2);
        txtGrid3 = (TextView)findViewById(R.id.txt_process_name_3);
        txtGrid4 = (TextView)findViewById(R.id.txt_process_name_4);
        txtGrid5 = (TextView)findViewById(R.id.txt_process_name_5);
        txtGrid6 = (TextView)findViewById(R.id.txt_process_name_6);
        linGrid1 = (LinearLayout)findViewById(R.id.lin_grid_title_1);
        linGrid2 = (LinearLayout)findViewById(R.id.lin_grid_title_2);
        linGrid3 = (LinearLayout)findViewById(R.id.lin_grid_title_3);
        linGrid4 = (LinearLayout)findViewById(R.id.lin_grid_title_4);
        linGrid5 = (LinearLayout)findViewById(R.id.lin_grid_title_5);
        linGrid6 = (LinearLayout)findViewById(R.id.lin_grid_title_6);

        linearStatusBar = (LinearLayout)findViewById(R.id.linear_main_status_bar);
        linearHamber = (LinearLayout)findViewById(R.id.linear_main_hamberger);
        imgHamber = (ImageView)findViewById(R.id.img_main_hamber);
        imgMore = (ImageView)findViewById(R.id.img_main_more);

        linearDialogTest = (FrameLayout)findViewById(R.id.linear_popup_test);
        txtLandProcess = (TextView)findViewById(R.id.txt_sensor_process);
        txtLandName = (TextView)findViewById(R.id.txt_sensor_name);
        txtLandGroup = (TextView)findViewById(R.id.txt_sensor_group);
        txtLandLocation = (TextView)findViewById(R.id.txt_sensor_location);
        linearLandStatus = (LinearLayout)findViewById(R.id.linear_sensor_popup_status);
        btnLandCancel = (Button)findViewById(R.id.btn_sensor_popup_cancel);

        linearMessageVisible = (LinearLayout)findViewById(R.id.linear_side_message_child_visible);
        txtMessageReceive = (TextView)findViewById(R.id.txt_side_message_child_receive);
        txtMessageSend = (TextView)findViewById(R.id.txt_side_message_child_send);
        frameBadge = (FrameLayout)findViewById(R.id.frame_main_left_badge);
        txtBadge = (TextView)findViewById(R.id.txt_main_left_badge);

        txtMessageReceive.setOnClickListener(this);
        txtMessageSend.setOnClickListener(this);

        btnLandCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearDialogTest.setVisibility(View.GONE);
            }
        });

        linGrid1.setVisibility(View.GONE);
        linGrid2.setVisibility(View.GONE);
        linGrid3.setVisibility(View.GONE);
        linGrid4.setVisibility(View.GONE);
        linGrid5.setVisibility(View.GONE);
        linGrid6.setVisibility(View.GONE);
        grd1.setVisibility(View.GONE);
        grd2.setVisibility(View.GONE);
        grd3.setVisibility(View.GONE);
        grd4.setVisibility(View.GONE);
        grd5.setVisibility(View.GONE);
        grd6.setVisibility(View.GONE);

        BtnAddEvent();

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);
        navName = (TextView) drawerLayout.findViewById(R.id.nav_name);
        navEmail = (TextView) drawerLayout.findViewById(R.id.nav_email);
        linearMonitor = (LinearLayout)drawerLayout.findViewById(R.id.linear_main_monitor);
        linearSelect = (LinearLayout)drawerLayout.findViewById(R.id.linear_main_select);
        linearMessage = (LinearLayout)drawerLayout.findViewById(R.id.linear_main_message);
        linearSetting = (LinearLayout)drawerLayout.findViewById(R.id.linear_main_setting);
        linearLogout = (LinearLayout)drawerLayout.findViewById(R.id.linear_main_logout);
        imgLogo = (ImageView)drawerLayout.findViewById(R.id.imageView);
        imgSideMonitoringArrow = (ImageView)drawerLayout.findViewById(R.id.img_main_side_monitoring_arrow);
        imgSideSelectViewArrow = (ImageView)drawerLayout.findViewById(R.id.img_main_side_select_view_arrow);

        linearMonitorChildVisible = (LinearLayout)drawerLayout.findViewById(R.id.linear_side_monitoring_child_visible);
        linearSelectChildVislble = (LinearLayout)drawerLayout.findViewById(R.id.linear_side_select_view_child_visible);
        txtMonitorChildProcess = (TextView)drawerLayout.findViewById(R.id.txt_side_monitoring_child_process);
        txtMonitorChildTile = (TextView)drawerLayout.findViewById(R.id.txt_side_monitoring_child_tile);
        txtSelectChildProcess = (TextView)drawerLayout.findViewById(R.id.txt_side_select_view_child_process);
        txtSelectChildTile = (TextView)drawerLayout.findViewById(R.id.txt_side_select_view_child_tile);

        linearMonitor.setOnClickListener(this);
        linearSelect.setOnClickListener(this);
        linearMessage.setOnClickListener(this);
        linearSetting.setOnClickListener(this);
        linearLogout.setOnClickListener(this);
        linearHamber.setOnClickListener(this);
        imgMore.setOnClickListener(this);
        txtMonitorChildProcess.setOnClickListener(this);
        txtMonitorChildTile.setOnClickListener(this);
        txtSelectChildProcess.setOnClickListener(this);
        txtSelectChildTile.setOnClickListener(this);

        navName.setText(Utils.USER_NAME);
        navEmail.setText(Utils.USER_EMAIL);

        if (Utils.LOGO_PATH.length() == 0){
            imgLogo.setImageResource(R.drawable.noimage);
        }else{
            Glide.with(this).load(ServerUtils.server + Utils.LOGO_PATH).into(imgLogo);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                timer2.cancel();
                onResume();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void BtnAddEvent(){
        btnFold1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFold1.setEnabled(false);
                addGridAnimation(grd1,btnFold1,txtGrid1,linGrid1);
                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                    @Override public void run() { // 실행할 동작 코딩
                        mHandler.sendEmptyMessage(0);	// 실행이 끝난후 알림
                } }, 500);

            }
        });
        btnFold2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFold2.setEnabled(false);
                addGridAnimation(grd2,btnFold2,txtGrid2,linGrid2);
                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                    @Override public void run() { // 실행할 동작 코딩
                        mHandler.sendEmptyMessage(1);	// 실행이 끝난후 알림
                    } }, 500);
            }
        });
        btnFold3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFold3.setEnabled(false);
                addGridAnimation(grd3,btnFold3,txtGrid3,linGrid3);
                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                    @Override public void run() { // 실행할 동작 코딩
                        mHandler.sendEmptyMessage(2);	// 실행이 끝난후 알림
                    } }, 500);
            }
        });
        btnFold4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFold4.setEnabled(false);
                addGridAnimation(grd4,btnFold4,txtGrid4,linGrid4);
                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                    @Override public void run() { // 실행할 동작 코딩
                        mHandler.sendEmptyMessage(3);	// 실행이 끝난후 알림
                    } }, 500);
            }
        });
        btnFold5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFold5.setEnabled(false);
                addGridAnimation(grd5,btnFold5,txtGrid5,linGrid5);
                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                    @Override public void run() { // 실행할 동작 코딩
                        mHandler.sendEmptyMessage(4);	// 실행이 끝난후 알림
                    } }, 500);
            }
        });
        btnFold6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFold6.setEnabled(false);
                addGridAnimation(grd6,btnFold6,txtGrid6,linGrid6);
                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                    @Override public void run() { // 실행할 동작 코딩
                        mHandler.sendEmptyMessage(5);	// 실행이 끝난후 알림
                    } }, 500);
            }
        });
    }


    public void addGridAnimation(final GridView grid,ImageView imageView,TextView textView,LinearLayout linearLayout){

        if(grid.getVisibility() == View.VISIBLE){
            collapse(grid);
            imageView.setRotationX(180);
            textView.setTextColor(Color.parseColor("#ffffff"));
            linearLayout.setBackgroundColor(Color.parseColor("#808080"));
            imageView.setColorFilter(Color.parseColor("#ffffff"));
        }else{
            expand(grid);
            imageView.setRotationX(360);
            textView.setTextColor(Color.parseColor("#2c2c2c"));
            linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            imageView.setColorFilter(Color.parseColor("#808080"));
        }
    }

    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }
            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public class ViewListNotSelectServer extends AsyncTask<String, String, String> {
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
                for (int i = 0; i < 1; i++) {
                    JSONObject jsonObject = jsonArrayView.getJSONObject(i);

                    txtViewName.setText(jsonObject.getString("VIEW_NAME"));
                    txtProcessCnt.setText(jsonObject.getString("PROCESS_CNT"));
                    txtSensorCnt.setText(jsonObject.getString("SENSOR_CNT"));
                    processListServer = new ProcessListServer();
                    processListServer.execute(jsonObject.getString("VIEW_NO"));
                }
            } catch (Exception e) {

            }
        }
    }

    public class ViewListServer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute(){
            if (oneCheck == false){
                asyncDialog = new ProgressDialog(MainActivity.this);
                asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                asyncDialog.setMessage("Loading...");
                asyncDialog.show();
                oneCheck = true;
            }
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectOneViewInfo());

            http.addOrReplace("siteNo",strings[0]);
            http.addOrReplace("userNo",strings[1]);

            HttpClient post = http.create();

            post.request();

            String body = post.getBody();

            Log.i(TAG,"ViewListServer check : " + body);

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            //두번째방법으로 가져오기

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                if (jsonArray.length() == 0){
                    txtProcessCnt.setText("0");
                    txtSensorCnt.setText("0");
                    txtViewName.setText("VIEW_NAME");
                    asyncDialog.dismiss();
                    new ViewListNotSelectServer().execute(String.valueOf(Utils.USER_SITE_NO));
                }

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObjectView = jsonArray.getJSONObject(i);
                    txtViewName.setText(jsonObjectView.getString("VIEW_NAME"));
                    txtProcessCnt.setText(jsonObjectView.getString("PROCESS_CNT"));
                    txtSensorCnt.setText(jsonObjectView.getString("SENSOR_CNT"));
                    processListServer = new ProcessListServer();
                    processListServer.execute(jsonObjectView.getString("VIEW_NO"));
                }

            }catch (Exception e){

            }
        }
    }

    public class ProcessListServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectProcessByViewNo());

            http.addOrReplace("viewNo",strings[0]);
            HttpClient post = http.create();

            post.request();
            String body = post.getBody();
            Log.i(TAG,"second : " + body);

            return body;
        }

        @Override
        protected void onPostExecute(String s) {

            testProcessNameList = new ArrayList<>();
            testProcessNoList = new ArrayList<>();

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObjectView = jsonArray.getJSONObject(i);

                    testProcessNameList.add(jsonObjectView.getString("NAME"));
                    testProcessNoList.add(jsonObjectView.getString("PROCESS_NO"));
                    Log.i(TAG,"process no : " + jsonObjectView.getString("PROCESS_NO"));
                    new SensorListServer().execute(jsonObjectView.getString("PROCESS_NO"),String.valueOf(Utils.USER_SITE_NO));
                }
                asyncDialog.dismiss();
            }catch (Exception e){

            }
        }
    }

    public class SensorListServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectSensorByProcessNo());
            http.addOrReplace("processNo",strings[0]);
            http.addOrReplace("siteNo",strings[1]);
            HttpClient post = http.create();
            post.request();
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {

            try{

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                JSONArray jsonPortOption = null;
                JSONObject objectPortOption = null;

                ArrayList<String> testSensorNameList = new ArrayList<>();
                ArrayList<String> testSensorIdList = new ArrayList<>();
                ArrayList<String> testSensorNoList = new ArrayList<>();

                ArrayList<Integer> sensorModelNoList = new ArrayList<>();
                ArrayList<String> sensorProcessList = new ArrayList<>();
                ArrayList<String> sensorLineList = new ArrayList<>();
                ArrayList<String> sensorLocationList = new ArrayList<>();
                ArrayList<String> sensorGroupList = new ArrayList<>();
                ArrayList<Integer> sensorMonitoringList = new ArrayList<>();

                port01ArrayList = new ArrayList<>();
                port01DateArrayList = new ArrayList<>();

                colorListArray = new ArrayList<>();
                txtColorListArray = new ArrayList<>();
                nameListArray = new ArrayList<>();
                priorityListArray = new ArrayList<>();
                portNoListArray = new ArrayList<>();

                if (jsonArray.length() == 0){
                    port01List = new ArrayList<>();
                    port01DateList = new ArrayList<>();

                    sensorModelNoList.add(0);
                    sensorProcessList.add("");
                    sensorLineList.add("");
                    sensorLocationList.add("");
                    sensorGroupList.add("");
                    testSensorNameList.add("");
                    testSensorIdList.add("");
                    testSensorNoList.add("");
                    sensorMonitoringList.add(0);
                    port01List.add(0);
                    port01List.add(0);
                    port01List.add(0);
                    port01List.add(0);
                    port01List.add(0);
                    port01List.add(0);
                    port01List.add(0);
                    port01List.add(0);
                    port01DateList.add("");
                    port01DateList.add("");
                    port01DateList.add("");
                    port01DateList.add("");
                    port01DateList.add("");
                    port01DateList.add("");
                    port01DateList.add("");
                    port01DateList.add("");

                    port01DateArrayList.add(port01DateList);
                    port01ArrayList.add(port01List);

                    sensorModelNoArray.add(sensorModelNoList);
                    sensorProcessArray.add(sensorProcessList);
                    sensorLineArray.add(sensorLineList);
                    sensorLocationArray.add(sensorLocationList);
                    sensorGroupArray.add(sensorGroupList);
                }

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObjectView = jsonArray.getJSONObject(i);

                    Log.i(TAG,"i : " + i + "view : " + jsonObjectView.toString());

                    port01List = new ArrayList<>();
                    port01DateList = new ArrayList<>();

                    colorList = new ArrayList<>();
                    txtColorList = new ArrayList<>();
                    priorityList = new ArrayList<>();
                    nameList = new ArrayList<>();
                    portNoList = new ArrayList<>();

                    colorList.addAll(colorList1);
                    txtColorList.addAll(txtColorList1);
                    priorityList.addAll(priorityList1);
                    nameList.addAll(nameList1);
                    portNoList.addAll(portNoList1);

                    if (jsonObjectView.isNull("MODEL_NO") == false){
                        sensorModelNoList.add(jsonObjectView.getInt("MODEL_NO"));
                    }else{
                        sensorModelNoList.add(0);
                    }
                    if (jsonObjectView.isNull("MONITORING") == false){
                        sensorMonitoringList.add(jsonObjectView.getInt("MONITORING"));
                    }else{
                        sensorMonitoringList.add(0);
                    }
                    if (jsonObjectView.isNull("PROCESS") == false){
                        sensorProcessList.add(jsonObjectView.getString("PROCESS"));
                    }else{
                        sensorProcessList.add("");
                    }
                    if (jsonObjectView.isNull("LINE") == false){
                        sensorLineList.add(jsonObjectView.getString("LINE"));
                    }else{
                        sensorLineList.add("");
                    }
                    if (jsonObjectView.isNull("LOCATION") == false){
                        sensorLocationList.add(jsonObjectView.getString("LOCATION"));
                    }else{
                        sensorLocationList.add("");
                    }
                    if (jsonObjectView.isNull("GROUP") == false){
                        sensorGroupList.add(jsonObjectView.getString("GROUP"));
                    }else{
                        sensorGroupList.add("");
                    }
                    if (jsonObjectView.isNull("NAME") == false){
                        testSensorNameList.add(jsonObjectView.getString("NAME"));
                    }else{
                        testSensorNameList.add("");
                    }
                    if (jsonObjectView.isNull("SENSOR_ID") == false){
                        testSensorIdList.add(jsonObjectView.getString("SENSOR_ID"));
                    }else{
                        testSensorIdList.add("");
                    }
                    if (jsonObjectView.isNull("SENSOR_NO") == false){
                        testSensorNoList.add(jsonObjectView.getString("SENSOR_NO"));
                        if (!jsonObject.isNull("processPortOption")) {
                            jsonPortOption = jsonObject.getJSONArray("processPortOption");
                            for (int x = 0; x < jsonPortOption.length(); x++) {
                                objectPortOption = jsonPortOption.getJSONObject(x);
                                if (Utils.JsonIsNullCheck(objectPortOption,"TEXT_COLOR").length() > 0){
                                    for (int y = 0; y < portNoList.size(); y++){
                                        if (portNoList.get(y) == objectPortOption.getInt("PORT_NO") && objectPortOption.getInt("SENSOR_NO") == jsonObjectView.getInt("SENSOR_NO")){
                                            colorList.set(y,objectPortOption.getString("COLOR"));
                                            txtColorList.set(y,objectPortOption.getString("TEXT_COLOR"));
                                            priorityList.set(y,objectPortOption.getInt("PRIORITY"));
                                            nameList.set(y,objectPortOption.getString("NAME"));
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        testSensorNoList.add("");
                    }

                    if (jsonObjectView.isNull("PORT1")==false){
                        port01List.add(jsonObjectView.getInt("PORT1"));
                    }else{
                        port01List.add(0);
                    }
                    if (jsonObjectView.isNull("PORT2")==false){
                        port01List.add(jsonObjectView.getInt("PORT2"));
                    }else{
                        port01List.add(0);
                    }
                    if (jsonObjectView.isNull("PORT3")==false){
                        port01List.add(jsonObjectView.getInt("PORT3"));
                    }else{
                        port01List.add(0);
                    }
                    if (jsonObjectView.isNull("PORT4")==false){
                        port01List.add(jsonObjectView.getInt("PORT4"));
                    }else{
                        port01List.add(0);
                    }
                    if (jsonObjectView.isNull("PORT5")==false){
                        port01List.add(jsonObjectView.getInt("PORT5"));
                    }else{
                        port01List.add(0);
                    }
                    if (jsonObjectView.isNull("PORT6")==false){
                        port01List.add(jsonObjectView.getInt("PORT6"));
                    }else{
                        port01List.add(0);
                    }
                    if (jsonObjectView.isNull("PORT7")==false){
                        port01List.add(jsonObjectView.getInt("PORT7"));
                    }else{
                        port01List.add(0);
                    }
                    if (jsonObjectView.isNull("PORT8")==false){
                        port01List.add(jsonObjectView.getInt("PORT8"));
                    }else{
                        port01List.add(0);
                    }

                    if (jsonObjectView.isNull("PORT1_ON")==false){
                        port01DateList.add(jsonObjectView.getString("PORT1_ON"));
                    }else{
                        port01DateList.add("");
                    }
                    if (jsonObjectView.isNull("PORT2_ON")==false){
                        port01DateList.add(jsonObjectView.getString("PORT2_ON"));
                    }else{
                        port01DateList.add("");
                    }
                    if (jsonObjectView.isNull("PORT3_ON")==false){
                        port01DateList.add(jsonObjectView.getString("PORT3_ON"));
                    }else{
                        port01DateList.add("");
                    }
                    if (jsonObjectView.isNull("PORT4_ON")==false){
                        port01DateList.add(jsonObjectView.getString("PORT4_ON"));
                    }else{
                        port01DateList.add("");
                    }
                    if (jsonObjectView.isNull("PORT5_ON")==false){
                        port01DateList.add(jsonObjectView.getString("PORT5_ON"));
                    }else{
                        port01DateList.add("");
                    }
                    if (jsonObjectView.isNull("PORT6_ON")==false){
                        port01DateList.add(jsonObjectView.getString("PORT6_ON"));
                    }else{
                        port01DateList.add("");
                    }
                    if (jsonObjectView.isNull("PORT7_ON")==false){
                        port01DateList.add(jsonObjectView.getString("PORT7_ON"));
                    }else{
                        port01DateList.add("");
                    }
                    if (jsonObjectView.isNull("PORT8_ON")==false){
                        port01DateList.add(jsonObjectView.getString("PORT8_ON"));
                    }else{
                        port01DateList.add("");
                    }

                    port01DateArrayList.add(port01DateList);
                    port01ArrayList.add(port01List);

                    sensorModelNoArray.add(sensorModelNoList);
                    sensorProcessArray.add(sensorProcessList);
                    sensorLineArray.add(sensorLineList);
                    sensorLocationArray.add(sensorLocationList);
                    sensorGroupArray.add(sensorGroupList);

                    colorListArray.add(colorList);
                    txtColorListArray.add(txtColorList);
                    nameListArray.add(nameList);
                    priorityListArray.add(priorityList);
                    portNoListArray.add(portNoList);
                }

                asyncDialog.dismiss();

//                Log.i(TAG,"testI : " + testI + " colorList : " + colorList);
//                Log.i(TAG,"portNoList : " + nameList);

                adapterMake(testI++,testProcessNameList, testSensorNoList, testSensorIdList, testSensorNameList, port01ArrayList
                        ,sensorModelNoList,sensorProcessList,sensorLineList,sensorLocationList,sensorGroupList,port01DateArrayList,sensorMonitoringList);

//                new SiteColorListServer().execute(String.valueOf(Utils.USER_SITE_NO));
            }catch (Exception e){

            }
        }
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
           // 실행이 끝난후 확인 가능
            if(msg.what == 0){
                btnFold1.setEnabled(true);
            }
            if(msg.what == 1){
                btnFold2.setEnabled(true);
            }
            if(msg.what == 2){
                btnFold3.setEnabled(true);
            }
            if(msg.what == 3){
                btnFold4.setEnabled(true);
            }
            if(msg.what == 4){
                btnFold5.setEnabled(true);
            }
            if(msg.what == 5){
                btnFold6.setEnabled(true);
            }

        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                grd1.invalidate();
                grd1.invalidateViews();
            }
            if (msg.what == 2){
                grd2.invalidate();
                grd2.invalidateViews();
            }
            if (msg.what == 3){
                grd3.invalidate();
                grd3.invalidateViews();
            }
            if (msg.what == 4){
                grd4.invalidate();
                grd4.invalidateViews();
            }
            if (msg.what == 5){
                grd5.invalidate();
                grd5.invalidateViews();
            }
            if (msg.what == 6){
                grd6.invalidate();
                grd6.invalidateViews();
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();

        if (pref.getInt("badgeCnt",0) > 0){
            frameBadge.setVisibility(View.VISIBLE);
            txtBadge.setText("" + pref.getInt("badgeCnt",0));
        }else{
            frameBadge.setVisibility(View.GONE);
        }

        oneCheck = false;
        testI = 0;

        linGrid1.setVisibility(View.GONE);
        linGrid2.setVisibility(View.GONE);
        linGrid3.setVisibility(View.GONE);
        linGrid4.setVisibility(View.GONE);
        linGrid5.setVisibility(View.GONE);
        linGrid6.setVisibility(View.GONE);
        grd1.setVisibility(View.GONE);
        grd2.setVisibility(View.GONE);
        grd3.setVisibility(View.GONE);
        grd4.setVisibility(View.GONE);
        grd5.setVisibility(View.GONE);
        grd6.setVisibility(View.GONE);

        port01ArrayList = new ArrayList<>();
        port01DateArrayList = new ArrayList<>();

        colorListArray = new ArrayList<>();
        txtColorListArray = new ArrayList<>();
        nameListArray = new ArrayList<>();
        priorityListArray = new ArrayList<>();
        portNoListArray = new ArrayList<>();

        testProcessNameList = new ArrayList<>();
        testProcessNoList = new ArrayList<>();
        testSensorNameArray = new ArrayList<>();
        testSensorIdArray = new ArrayList<>();
        testSensorNoArray = new ArrayList<>();
        colorArrayList = new ArrayList<>();
        priorityNumber = new ArrayList<>();
        priorityArrayList = new ArrayList<>();

        sensorModelNoArray = new ArrayList<>();
        sensorProcessArray = new ArrayList<>();
        sensorLineArray = new ArrayList<>();
        sensorLocationArray = new ArrayList<>();
        sensorGroupArray = new ArrayList<>();

        new SiteColorListServer().execute(String.valueOf(Utils.USER_SITE_NO));
        new ViewListServer().execute(String.valueOf(Utils.USER_SITE_NO),String.valueOf(Utils.USER_NO));

        if (asyncDialog != null){
            if (asyncDialog.isShowing()){
                asyncDialog.dismiss();
            }
        }

        timer2 = new CountDownTimer(60000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

                aa++;
                testI = 0;

                if (adt1 != null) {
                    handler.sendEmptyMessage(1);
                }
                if (adt2 != null){
                    handler.sendEmptyMessage(2);
                }
                if (adt3 != null){
                    handler.sendEmptyMessage(3);
                }
                if (adt4 != null){
                    handler.sendEmptyMessage(4);
                }
                if (adt5 != null){
                    handler.sendEmptyMessage(5);
                }
                if (adt6 != null){
                    handler.sendEmptyMessage(6);
                }
            }

            @Override
            public void onFinish() {
                aa = 0;
                btnFold1.setRotationX(360);
                btnFold2.setRotationX(360);
                btnFold3.setRotationX(360);
                btnFold4.setRotationX(360);
                btnFold5.setRotationX(360);
                btnFold6.setRotationX(360);
                new ViewListServer().execute(String.valueOf(Utils.USER_SITE_NO),String.valueOf(Utils.USER_NO));
                timer2.start();
            }
        };
        timer2.start();
    }

   private void adapterMake(int position,ArrayList<String> processName ,ArrayList<String> no,ArrayList<String> id, ArrayList<String> name, ArrayList<ArrayList<Integer>> status,
                            ArrayList<Integer> modelIdList,ArrayList<String> processList,ArrayList<String> lineList,
                            ArrayList<String> locationList, ArrayList<String> groupList,ArrayList<ArrayList<String>> dataList, ArrayList<Integer> monitoringList){

       adt1 = new SensorCellAdapter(MainActivity.this,5);
       adt2 = new SensorCellAdapter(MainActivity.this,5);
       adt3 = new SensorCellAdapter(MainActivity.this,5);
       adt4 = new SensorCellAdapter(MainActivity.this,5);
       adt5 = new SensorCellAdapter(MainActivity.this,5);
       adt6 = new SensorCellAdapter(MainActivity.this,5);

       int i = position;

            if (i == 0) {
                linGrid1.setVisibility(View.VISIBLE);
                grd1.setVisibility(View.VISIBLE);
                txtGrid1.setText(processName.get(i).toString());

                for (int j = 0; j < name.size(); j++){
                    if (name.get(j).length() != 0 && monitoringList.get(j) == 1){
                        adt1.addItem(id.get(j), name.get(j), status.get(j), colorListArray.get(j),priorityListArray.get(j),nameListArray.get(j),
                                modelIdList.get(j),processList.get(j),lineList.get(j)
                                ,locationList.get(j),groupList.get(j),dataList.get(j),txtColorListArray.get(j));
                    }
                }
                grd1.setAdapter(adt1);
            } else if (i == 1) {
                linGrid2.setVisibility(View.VISIBLE);
                grd2.setVisibility(View.VISIBLE);
                txtGrid2.setText(processName.get(i).toString());

                for (int j = 0; j < name.size(); j++){
                    try{
                        if (name.get(j).length() != 0 && monitoringList.get(j) == 1){
                            adt2.addItem(id.get(j), name.get(j), status.get(j), colorListArray.get(j),priorityListArray.get(j),nameListArray.get(j),
                                    modelIdList.get(j),processList.get(j),lineList.get(j)
                                    ,locationList.get(j),groupList.get(j),dataList.get(j),txtColorListArray.get(j));
                        }
                    }catch (Exception e){

                    }
                }
                grd2.setAdapter(adt2);

            } else if (i == 2) {
                linGrid3.setVisibility(View.VISIBLE);
                grd3.setVisibility(View.VISIBLE);
                txtGrid3.setText(processName.get(i).toString());
                for (int j = 0; j < name.size(); j++){
                    if (name.get(j).length() != 0 && monitoringList.get(j) == 1){
                        adt3.addItem(id.get(j), name.get(j), status.get(j), colorListArray.get(j),priorityListArray.get(j),nameListArray.get(j),
                                modelIdList.get(j),processList.get(j),lineList.get(j)
                                ,locationList.get(j),groupList.get(j),dataList.get(j),txtColorListArray.get(j));
                    }
                }
                grd3.setAdapter(adt3);
            } else if (i == 3) {
                linGrid4.setVisibility(View.VISIBLE);
                grd4.setVisibility(View.VISIBLE);
                txtGrid4.setText(processName.get(i).toString());
                for (int j = 0; j < name.size(); j++) {
                    if (name.get(j).length() != 0 && monitoringList.get(j) == 1){
                        adt4.addItem(id.get(j), name.get(j), status.get(j), colorListArray.get(j),priorityListArray.get(j),nameListArray.get(j),
                                modelIdList.get(j),processList.get(j),lineList.get(j)
                                ,locationList.get(j),groupList.get(j),dataList.get(j),txtColorListArray.get(j));
                    }
                }
                grd4.setAdapter(adt4);
            } else if (i == 4) {
                linGrid5.setVisibility(View.VISIBLE);
                grd5.setVisibility(View.VISIBLE);
                txtGrid5.setText(processName.get(i).toString());
                for (int j = 0; j < name.size(); j++){
                    if (name.get(j).length() != 0 && monitoringList.get(j) == 1){
                        adt5.addItem(id.get(j), name.get(j), status.get(j), colorListArray.get(j),priorityListArray.get(j),nameListArray.get(j),
                                modelIdList.get(j),processList.get(j),lineList.get(j)
                                ,locationList.get(j),groupList.get(j),dataList.get(j),txtColorListArray.get(j));
                    }
                }
                grd5.setAdapter(adt5);
            } else if (i == 5) {
                linGrid6.setVisibility(View.VISIBLE);
                grd6.setVisibility(View.VISIBLE);
                txtGrid6.setText(processName.get(i).toString());
                for (int j = 0; j < name.size(); j++){
                    if (name.get(j).length() != 0 && monitoringList.get(j) == 1){
                        adt6.addItem(id.get(j), name.get(j), status.get(j), colorListArray.get(j),priorityListArray.get(j),nameListArray.get(j),
                                modelIdList.get(j),processList.get(j),lineList.get(j)
                                ,locationList.get(j),groupList.get(j),dataList.get(j),txtColorListArray.get(j));
                    }
                }
                grd6.setAdapter(adt6);
            }
    }

    @Override
    public void onPause(){
        super.onPause();
        timer2.cancel();
    }

    public class SiteColorListServer extends AsyncTask<String, String, String> {
//        String processNo = "";
//
//        public SiteColorListServer(String processNo) {
//            this.processNo = processNo;
//        }

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectSiteColor());

            http.addOrReplace("siteNo",strings[0]);
            HttpClient post = http.create();
            post.request();

            String body = post.getBody();

            Log.i(TAG,"SiteColorListServer check : " + body);

            return body;
        }

        @Override
        protected void onPostExecute(String s) {

            try{
                colorList = new ArrayList<>();
                txtColorList = new ArrayList<>();
                priorityList = new ArrayList<>();
                nameList = new ArrayList<>();
                portNoList = new ArrayList<>();

                colorList1 = new ArrayList<>();
                txtColorList1 = new ArrayList<>();
                priorityList1 = new ArrayList<>();
                nameList1 = new ArrayList<>();
                portNoList1 = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObjectView = jsonArray.getJSONObject(i);
                    if (jsonObjectView.getString("PORT_NO").equals("100")) {
                        allOffColor = jsonObjectView.getString("COLOR");
                    } else if (jsonObjectView.getString("PORT_NO").equals("101")) {

                    } else {
                        colorList.add(jsonObjectView.getString("COLOR"));
                        txtColorList.add(jsonObjectView.getString("TEXT_COLOR"));
                        priorityList.add(jsonObjectView.getInt("PRIORITY"));
                        nameList.add(jsonObjectView.getString("NAME"));
                        portNoList.add(jsonObjectView.getInt("PORT_NO"));

                        colorList1.add(jsonObjectView.getString("COLOR"));
                        txtColorList1.add(jsonObjectView.getString("TEXT_COLOR"));
                        priorityList1.add(jsonObjectView.getInt("PRIORITY"));
                        nameList1.add(jsonObjectView.getString("NAME"));
                        portNoList1.add(jsonObjectView.getInt("PORT_NO"));
                    }
                }
            }catch (Exception e){

            }
        }
    }

    public class LogoutServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.GcmConnection());

            http.addOrReplace("user_no",strings[0]);
            http.addOrReplace("siteNo",strings[1]);
            http.addOrReplace("status","android");
            http.addOrReplace("token","-");
            HttpClient post = http.create();
            post.request();

            String body = post.getBody();

            Log.i(TAG,"LogoutServer check : " + body);

            return body;
        }

        @Override
        protected void onPostExecute(String s) {

            drawerLayout.closeDrawer(drawerView);
            if (s == null || s.length() == 0){
                final AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                alert_confirm.setMessage("Server Connecting failed").setCancelable(false).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new LogoutServer().execute(String.valueOf(Utils.USER_NO),String.valueOf(Utils.USER_SITE_NO));
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }else{
                updateIconBadge(getApplicationContext(),0);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(1);
                editor.putInt("login_flag",0);
                editor.putInt("badgeCnt",0);
                editor.remove("userEmail");
                editor.remove("userPw");

                Utils.TILE_NO = "";
                editor.commit();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        }
    }

    void gridCountPickUp(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        final LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final LinearLayout dialogView = (LinearLayout)inflater.inflate(R.layout.dialog_four_button,null);

        final Button btnTwo = (Button)dialogView.findViewById(R.id.btn_grid_select_dialog02);
        final Button btnThree = (Button)dialogView.findViewById(R.id.btn_grid_select_dialog03);
        final Button btnFour = (Button)dialogView.findViewById(R.id.btn_grid_select_dialog04);
        final Button btnFive = (Button)dialogView.findViewById(R.id.btn_grid_select_dialog05);

        builder.setView(dialogView);
        builder.setCancelable(true);
        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adt1.colums = 2;
                adt2.colums = 2;
                adt3.colums = 2;
                adt4.colums = 2;
                adt5.colums = 2;
                adt6.colums = 2;
                grd1.setNumColumns(2);
                grd2.setNumColumns(2);
                grd3.setNumColumns(2);
                grd4.setNumColumns(2);
                grd5.setNumColumns(2);
                grd6.setNumColumns(2);
                adt1.notifyDataSetChanged();
                adt2.notifyDataSetChanged();
                adt3.notifyDataSetChanged();
                adt4.notifyDataSetChanged();
                adt5.notifyDataSetChanged();
                adt6.notifyDataSetChanged();
                grd1.invalidate();
                grd2.invalidate();
                grd3.invalidate();
                grd4.invalidate();
                grd5.invalidate();
                grd6.invalidate();
                alertDialog.dismiss();
            }
        });

        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adt1.colums = 3;
                adt2.colums = 3;
                adt3.colums = 3;
                adt4.colums = 3;
                adt5.colums = 3;
                adt6.colums = 3;
                grd1.setNumColumns(3);
                grd2.setNumColumns(3);
                grd3.setNumColumns(3);
                grd4.setNumColumns(3);
                grd5.setNumColumns(3);
                grd6.setNumColumns(3);
                adt1.notifyDataSetChanged();
                adt2.notifyDataSetChanged();
                adt3.notifyDataSetChanged();
                adt4.notifyDataSetChanged();
                adt5.notifyDataSetChanged();
                adt6.notifyDataSetChanged();
                grd1.invalidate();
                grd2.invalidate();
                grd3.invalidate();
                grd4.invalidate();
                grd5.invalidate();
                grd6.invalidate();
                alertDialog.dismiss();
            }
        });

        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adt1.colums = 4;
                adt2.colums = 4;
                adt3.colums = 4;
                adt4.colums = 4;
                adt5.colums = 4;
                adt6.colums = 4;
                grd1.setNumColumns(4);
                grd2.setNumColumns(4);
                grd3.setNumColumns(4);
                grd4.setNumColumns(4);
                grd5.setNumColumns(4);
                grd6.setNumColumns(4);
                adt1.notifyDataSetChanged();
                adt2.notifyDataSetChanged();
                adt3.notifyDataSetChanged();
                adt4.notifyDataSetChanged();
                adt5.notifyDataSetChanged();
                adt6.notifyDataSetChanged();
                grd1.invalidate();
                grd2.invalidate();
                grd3.invalidate();
                grd4.invalidate();
                grd5.invalidate();
                grd6.invalidate();
                alertDialog.dismiss();
            }
        });

        btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adt1.colums = 5;
                adt2.colums = 5;
                adt3.colums = 5;
                adt4.colums = 5;
                adt5.colums = 5;
                adt6.colums = 5;
                grd1.setNumColumns(5);
                grd2.setNumColumns(5);
                grd3.setNumColumns(5);
                grd4.setNumColumns(5);
                grd5.setNumColumns(5);
                grd6.setNumColumns(5);
                adt1.notifyDataSetChanged();
                adt2.notifyDataSetChanged();
                adt3.notifyDataSetChanged();
                adt4.notifyDataSetChanged();
                adt5.notifyDataSetChanged();
                adt6.notifyDataSetChanged();
                grd1.invalidate();
                grd2.invalidate();
                grd3.invalidate();
                grd4.invalidate();
                grd5.invalidate();
                grd6.invalidate();
                alertDialog.dismiss();
            }
        });
    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear_main_hamberger : {
                drawerLayout.openDrawer(drawerView);
                drawerView.setClickable(true);
                break;
            }
            case R.id.img_main_more : {
                gridCountPickUp();
                break;
            }
            case R.id.txt_side_monitoring_child_process : {
                drawerLayout.closeDrawer(drawerView);
                break;
            }
            case R.id.txt_side_monitoring_child_tile : {
                drawerLayout.closeDrawer(drawerView);
                Intent intent = new Intent(this,TileViewActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                break;
            }
            case R.id.linear_main_monitor : {
                if (linearMonitorChildVisible.getVisibility() == View.VISIBLE){
                    collapse(linearMonitorChildVisible);
                    imgSideMonitoringArrow.setRotation(360);
                }else{
                    expand(linearMonitorChildVisible);
                    imgSideMonitoringArrow.setRotation(180);
                }
                break;
            }
            case R.id.linear_main_select : {
                if (linearSelectChildVislble.getVisibility() == View.VISIBLE){
                    collapse(linearSelectChildVislble);
                    imgSideSelectViewArrow.setRotation(360);
                }else{
                    expand(linearSelectChildVislble);
                    imgSideSelectViewArrow.setRotation(180);
                }
                break;
            }
            case R.id.txt_side_select_view_child_process : {
                drawerLayout.closeDrawer(drawerView);
                Intent i = new Intent(getApplicationContext(), SelectViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            }
            case R.id.txt_side_select_view_child_tile : {
                drawerLayout.closeDrawer(drawerView);
                Intent i = new Intent(getApplicationContext(), SelectTileViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            }
            case R.id.linear_main_message : {
//                if (linearMessageVisible.getVisibility() == View.VISIBLE){
//                    collapse(linearMessageVisible);
//                }else{
//                    expand(linearMessageVisible);
//                }
                drawerLayout.closeDrawer(drawerView);
                frameBadge.setVisibility(View.GONE);
                editor.putInt("badgeCnt",0);
                editor.commit();
                Intent i = new Intent(getApplicationContext(), MessageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                break;
            }
            case R.id.txt_side_message_child_receive : {
                drawerLayout.closeDrawer(drawerView);
                Intent i = new Intent(getApplicationContext(), MessageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            }
            case R.id.txt_side_message_child_send : {

                break;
            }
            case R.id.linear_main_setting : {
                drawerLayout.closeDrawer(drawerView);
                Intent i = new Intent(getApplicationContext(), SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            }
            case R.id.linear_main_logout : {
                final AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                alert_confirm.setMessage("Logout?").setCancelable(false).setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new LogoutServer().execute(String.valueOf(Utils.USER_NO),String.valueOf(Utils.USER_SITE_NO));
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
                break;
            }
        }
    }

    void dialogTest(){
        builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupLayout = (LinearLayout)inflater.inflate(R.layout.sensor_click_popup_layout,null);
        builder.setView(popupLayout);

        txtProcess = (TextView)popupLayout.findViewById(R.id.txt_sensor_process);
        txtName = (TextView)popupLayout.findViewById(R.id.txt_sensor_name);
        txtGroup = (TextView)popupLayout.findViewById(R.id.txt_sensor_group);
        txtLocation = (TextView)popupLayout.findViewById(R.id.txt_sensor_location);
        linearStatus = (LinearLayout)popupLayout.findViewById(R.id.linear_sensor_popup_status);
        btnCancel = (Button)popupLayout.findViewById(R.id.btn_sensor_popup_cancel);

        dialog = builder.create();

        dialog.setCancelable(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            linearStatusBar.setVisibility(View.VISIBLE);
            if (linearDialogTest.getVisibility() == View.VISIBLE){
                linearDialogTest.setVisibility(View.GONE);
                dialog.show();
            }

        }else if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            linearStatusBar.setVisibility(View.GONE);
            if (dialog.isShowing()){
                dialog.dismiss();
                linearDialogTest.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        backPressCloseHandler.onBackPressed();

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "Click Back button again to end the app.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            adt1.colums = 4;
            adt2.colums = 4;
            adt3.colums = 4;
            adt4.colums = 4;
            adt5.colums = 4;
            adt6.colums = 4;
            finish();
        }
    }
}
