package gtron.com.gtronsystem.Activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.CustomScrollView;
import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.InterceptGridLayout;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.Utils.Utils;

import static gtron.com.gtronsystem.Utils.Utils.NullCheck;

public class TileViewActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "TileViewActivity";
    private long backKeyPressedTime = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView navName, navEmail;
    String test = "";
    TextView txtViewName, txtSensorCnt;
    int sensorCnt = 0;

    CountDownTimer timer2;

    static String token;
    static boolean oneCheck = false;
    private DrawerLayout drawerLayout;
    private View drawerView;
    LinearLayout linearStatusBar;
    ImageView imgHamber;
    LinearLayout linearHamber;
    LinearLayout linearMonitor, linearSelect, linearMessage, linearSetting, linearLogout;
    LinearLayout linearMonitorChildVisible,linearSelectChildVislble;
    TextView txtMonitorChildProcess, txtMonitorChildTile,txtSelectChildProcess,txtSelectChildTile;
    ArrayList<String> colorArrayList = new ArrayList<>();
    ArrayList<Integer> priorityArrayList = new ArrayList<>();
    ArrayList<Integer> port01List = new ArrayList<>();
    ArrayList<String> port01DateList = new ArrayList<>();
    ArrayList<Integer> portMonitorList = new ArrayList<>();
    ArrayList<ArrayList<Integer>> port01ListArray = new ArrayList<>();
    ArrayList<ArrayList<String>> port01DateListArray = new ArrayList<>();
    ArrayList<ArrayList<Integer>> portMonitoringListArray = new ArrayList<>();
    int testI = 0;
    ArrayList<Integer> priorityNumber = new ArrayList<>();
    ArrayList<ArrayList<Integer>> sensorIdArray = new ArrayList<>();
    ArrayList<ArrayList<String>> sensorNameArray = new ArrayList<>();
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
    ScaleGestureDetector mScaleDetector;
//    ScrollView scrParent;
    LinearLayout linearTopParent;

    public static CustomScrollView scrParent;
    InterceptGridLayout gridLayout;
    WindowManager windowManager;
    Display display;

    public static ArrayList<TextView> txtSensorIdList = new ArrayList<>();
    public static ArrayList<TextView> txtSensorNameList = new ArrayList<>();
    public static ArrayList<TextView> txtSensorStatusList = new ArrayList<>();

    public int scaleCount = 4;

    int x[] = new int[2];
    int x1[] = new int[2];
    public static boolean touchBoolean = false;

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

    LinearLayout linearMessageVisible;
    TextView txtMessageReceive,txtMessageSend;
    public static FrameLayout frameBadge;
    public static TextView txtBadge;
    ImageView imgSideMonitoringArrow,imgSideSelectViewArrow;
    ImageView imgLogo;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_view);
        pref = getSharedPreferences("GetronSystem", MODE_PRIVATE);
        editor = pref.edit();

        editor.putString("viewType","tile");
        editor.apply();

        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

        NullCheck(this);

        dialogTest();

        Intent intent = getIntent();
        test = intent.getStringExtra("test");

        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_tile_view_parent);
        scrParent = (CustomScrollView)findViewById(R.id.scr_tile);
        gridLayout = (InterceptGridLayout)findViewById(R.id.grid_layout);

        linearTopParent = (LinearLayout)findViewById(R.id.linear_top_parent);

        txtViewName = (TextView) findViewById(R.id.select_view_list_item_title);
        txtSensorCnt = (TextView) findViewById(R.id.select_view_list_item_sensor_cnt);

        linearHamber = (LinearLayout) findViewById(R.id.linear_tile_hamberger);
        imgHamber = (ImageView) findViewById(R.id.img_tile_hamber);
        linearStatusBar = (LinearLayout)findViewById(R.id.linear_tile_view_status_bar);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);
        navName = (TextView) drawerLayout.findViewById(R.id.nav_name);
        navEmail = (TextView) drawerLayout.findViewById(R.id.nav_email);
        linearMonitor = (LinearLayout) drawerLayout.findViewById(R.id.linear_tile_monitor);
        linearSelect = (LinearLayout) drawerLayout.findViewById(R.id.linear_tile_select);
        linearMessage = (LinearLayout) drawerLayout.findViewById(R.id.linear_tile_message);
        linearSetting = (LinearLayout) drawerLayout.findViewById(R.id.linear_tile_setting);
        linearLogout = (LinearLayout) drawerLayout.findViewById(R.id.linear_tile_logout);

        linearMonitorChildVisible = (LinearLayout) drawerLayout.findViewById(R.id.linear_side_monitoring_child_visible);
        linearSelectChildVislble = (LinearLayout)drawerLayout.findViewById(R.id.linear_side_select_view_child_visible);
        txtMonitorChildProcess = (TextView) drawerLayout.findViewById(R.id.txt_side_monitoring_child_process);
        txtMonitorChildTile = (TextView) drawerLayout.findViewById(R.id.txt_side_monitoring_child_tile);
        txtSelectChildProcess = (TextView)drawerLayout.findViewById(R.id.txt_side_select_view_child_process);
        txtSelectChildTile = (TextView)drawerLayout.findViewById(R.id.txt_side_select_view_child_tile);
        imgLogo = (ImageView)drawerLayout.findViewById(R.id.imageView);
        imgSideMonitoringArrow = (ImageView)drawerLayout.findViewById(R.id.img_tile_side_monitoring_arrow);
        imgSideSelectViewArrow = (ImageView)drawerLayout.findViewById(R.id.img_tile_side_select_view_arrow);

        linearMessageVisible = (LinearLayout)findViewById(R.id.linear_side_message_child_visible);
        txtMessageReceive = (TextView)findViewById(R.id.txt_side_message_child_receive);
        txtMessageSend = (TextView)findViewById(R.id.txt_side_message_child_send);
        frameBadge = (FrameLayout)findViewById(R.id.frame_tile_view_left_badge);
        txtBadge = (TextView)findViewById(R.id.txt_tile_view_left_badge);

        linearMonitor.setOnClickListener(this);
        linearSelect.setOnClickListener(this);
        linearMessage.setOnClickListener(this);
        linearSetting.setOnClickListener(this);
        linearLogout.setOnClickListener(this);
        linearHamber.setOnClickListener(this);
        txtMonitorChildProcess.setOnClickListener(this);
        txtMonitorChildTile.setOnClickListener(this);
        txtSelectChildProcess.setOnClickListener(this);
        txtSelectChildTile.setOnClickListener(this);

        linearDialogTest = (FrameLayout)findViewById(R.id.linear_popup_test);
        txtLandProcess = (TextView)findViewById(R.id.txt_sensor_process);
        txtLandName = (TextView)findViewById(R.id.txt_sensor_name);
        txtLandGroup = (TextView)findViewById(R.id.txt_sensor_group);
        txtLandLocation = (TextView)findViewById(R.id.txt_sensor_location);
        linearLandStatus = (LinearLayout)findViewById(R.id.linear_sensor_popup_status);
        btnLandCancel = (Button)findViewById(R.id.btn_sensor_popup_cancel);

        btnLandCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearDialogTest.setVisibility(View.GONE);
            }
        });

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

    void GridListViewMake(final String sensorId, final String sensorName, final ArrayList<Integer> port1, final ArrayList<String> color, final ArrayList<Integer> priority, final ArrayList<String> name,
                          final String modelIdList, final String processList, final String lineList,
                          final String locationList, final String groupList, final ArrayList<String> dataList, ArrayList<Integer> monitorList, final ArrayList<String> textColor){

        View listView = new View(getApplicationContext());
        listView = getLayoutInflater().inflate(R.layout.grid_layout_item,null);
        final TextView sensor1 = (TextView) listView.findViewById(R.id.tv_sensor_1);
        final TextView sensor2 = (TextView) listView.findViewById(R.id.tv_sensor_2);
        final TextView sensor3 = (TextView) listView.findViewById(R.id.tv_sensor_3);
        final LinearLayout lin = (LinearLayout)listView.findViewById(R.id.lin_sensor_cell);

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();

        if (scaleCount == 2){
            sensor1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            sensor2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            sensor3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
        }else if (scaleCount == 3){
            sensor1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
            sensor2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
            sensor3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
        }else if (scaleCount == 4){
            sensor1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            sensor2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            sensor3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
        }else if (scaleCount == 5){
            sensor1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
            sensor2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
            sensor3.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
        }

        params.width = (int)(display.getWidth()/gridLayout.getColumnCount());
        params.height = (int)(display.getWidth()/gridLayout.getColumnCount());

        listView.setLayoutParams(params);

        gridLayout.addView(listView);

        txtSensorIdList.add(sensor1);
        txtSensorNameList.add(sensor2);
        txtSensorStatusList.add(sensor3);

        lin.setBackgroundColor(Color.parseColor(allOffColor));

        sensor1.setTextColor(Color.parseColor("#000000"));
        sensor2.setTextColor(Color.parseColor("#000000"));
        sensor3.setTextColor(Color.parseColor("#000000"));
        sensor2.setText(sensorName);

        final int[] highPriority = {1000};
        final ArrayList<String> sameNameList = new ArrayList<>();
        final ArrayList<String> sameColorList = new ArrayList<>();
        final ArrayList<String> portDateList = new ArrayList<>();
        final ArrayList<String> statusNameList = new ArrayList<>();
        final ArrayList<String> textColorList = new ArrayList<>();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                sameNameList.clear();
                sameColorList.clear();
                portDateList.clear();
                statusNameList.clear();
                textColorList.clear();
                for (int i = 0; i < port1.size(); i++){
                    if (port1.get(i) == 1){
                        portDateList.add(dataList.get(i));
                        statusNameList.add(name.get(i));
                        if (highPriority[0] > priority.get(i)){
                            lin.setBackgroundColor(Color.parseColor(color.get(i)));
                            sensor3.setText(name.get(i));
                            sensor1.setText(dataList.get(i));
                            sameNameList.clear();
                            sameColorList.clear();

                            highPriority[0] = priority.get(i);
                            sameNameList.add(name.get(i));
                            sameColorList.add(color.get(i));
                            textColorList.add(textColor.get(i));

                        }else if (highPriority[0] == priority.get(i)){
                            sameNameList.add(name.get(i));
                            sameColorList.add(color.get(i));
                            textColorList.add(textColor.get(i));
                        }
                    }
                }

                if (sameNameList.size() > 1){
                    lin.setBackgroundColor(Color.parseColor(sameColorList.get(aa % sameColorList.size())));
                    sensor3.setText(sameNameList.get(aa % sameColorList.size()));
                    sensor1.setText(portDateList.get(aa % sameColorList.size()));
                    if (textColorList.get(aa % textColorList.size()).equals("#FFF")){
                        sensor1.setTextColor(Color.parseColor("#FFFFFF"));
                        sensor2.setTextColor(Color.parseColor("#FFFFFF"));
                        sensor3.setTextColor(Color.parseColor("#FFFFFF"));
                    }else{
                        sensor1.setTextColor(Color.parseColor("#000000"));
                        sensor2.setTextColor(Color.parseColor("#000000"));
                        sensor3.setTextColor(Color.parseColor("#000000"));
                    }
                    lin.invalidate();
                    lin.setBackgroundColor(Color.parseColor(sameColorList.get(aa % sameColorList.size())));
                }
                else if (sameNameList.size() == 0){
                    lin.setBackgroundColor(Color.parseColor(allOffColor));
                    sensor3.setText("ALL OFF");
                }
            }
        };

        Timer timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, 0, 3000);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG,"get : " + getResources().getConfiguration().orientation);
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    dialog.show();
                }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    linearDialogTest.setVisibility(View.VISIBLE);
                }

                linearStatus.removeAllViews();
                linearLandStatus.removeAllViews();

                txtName.setText(sensorName);
                txtProcess.setText(processList);
                txtGroup.setText(groupList);
                txtLocation.setText(locationList);

                txtLandName.setText(sensorName);
                txtLandProcess.setText(processList);
                txtLandGroup.setText(groupList);
                txtLandLocation.setText(locationList);

                if (portDateList.size() > 0){
                    for (int i = 0; i < portDateList.size(); i++){
                        TextView txtStatus = new TextView(TileViewActivity.this);
                        TextView txtStatus1 = new TextView(TileViewActivity.this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        txtStatus.setLayoutParams(params);
                        txtStatus.setGravity(Gravity.CENTER);
                        txtStatus.setTextSize(12);
                        txtStatus.setText(statusNameList.get(i)+"("+portDateList.get(i)+")");

                        txtStatus1.setLayoutParams(params);
                        txtStatus1.setGravity(Gravity.CENTER);
                        txtStatus1.setTextSize(12);
                        txtStatus1.setText(statusNameList.get(i)+"("+portDateList.get(i)+")");
                        linearStatus.addView(txtStatus);
                        linearLandStatus.addView(txtStatus1);
                    }
                }else if (portDateList.size() == 0){
                    TextView txtStatus = new TextView(TileViewActivity.this);
                    TextView txtStatus1 = new TextView(TileViewActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    txtStatus.setLayoutParams(params);
                    txtStatus.setGravity(Gravity.CENTER);
                    txtStatus.setText("ALL OFF");

                    txtStatus1.setLayoutParams(params);
                    txtStatus1.setGravity(Gravity.CENTER);
                    txtStatus1.setText("ALL OFF");

                    linearStatus.addView(txtStatus);
                    linearLandStatus.addView(txtStatus1);
                }


            }
        });
    }

    void dialogTest(){
        builder = new AlertDialog.Builder(TileViewActivity.this);
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

    private void changeColumnCount(int columnCount) {

        Log.i(TAG,"display : " + display.getWidth());

        if (gridLayout.getColumnCount() != columnCount) {
            final int viewsCount = gridLayout.getChildCount();
            for (int i = 0; i < viewsCount; i++) {
                View view = gridLayout.getChildAt(i);

                    if (columnCount == 2){
                        txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                        txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                        txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                    }else if (columnCount == 3){
                        txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                        txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                        txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                    }else if (columnCount == 4){
                        txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                        txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                        txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                    }else if (columnCount == 5){
                        txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                        txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                        txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                    }

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                Log.i(TAG,"display : " + display.getWidth());
                params.width = (int)(display.getWidth()/columnCount);
                params.height = (int)(display.getWidth()/columnCount);
                view.setLayoutParams(params);
            }
            scaleCount = columnCount;
            gridLayout.setColumnCount(columnCount);
        }
    }

    private void rotationGrid(int columnCount){
        final int viewsCount = gridLayout.getChildCount();
        for (int i = 0; i < viewsCount; i++) {
            View view = gridLayout.getChildAt(i);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                if (columnCount == 2){
                    txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                    txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                    txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                }else if (columnCount == 3){
                    txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                    txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                    txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
                }else if (columnCount == 4){
                    txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                    txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                    txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                }else if (columnCount == 5){
                    txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                    txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                    txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                }
            }else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                if (columnCount == 2){
                    txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,32);
                    txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,32);
                    txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,32);
                }else if (columnCount == 3){
                    txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,29);
                    txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,29);
                    txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,29);
                }else if (columnCount == 4){
                    txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,26);
                    txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,26);
                    txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,26);
                }else if (columnCount == 5){
                    txtSensorIdList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,23);
                    txtSensorNameList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,23);
                    txtSensorStatusList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP,23);
                }
            }
            params.width = (int)(display.getWidth()/columnCount);
            params.height = (int)(display.getWidth()/columnCount);
            view.setLayoutParams(params);
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

    public class SiteColorListServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectSiteColor());

            http.addOrReplace("siteNo",strings[0]);
            HttpClient post = http.create();
            post.request();

            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.i(TAG,"colorList : " + s);

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

            try{
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

    public class SelectTileListServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.SelectTileList());
            http.addOrReplace("siteNo",strings[0]);
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
                for (int i = 0; i < 1; i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    Utils.TILE_NO = object.getString("TILE_NO");
                    editor.putString("tileNo",object.getString("TILE_NO"));
                    editor.commit();
                    new TileListServer().execute(String.valueOf(Utils.USER_SITE_NO),object.getString("TILE_NO"));
                }
            }catch (JSONException e){

            }
        }
    }

    public class TileListServer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.TileMonitoring());

            http.addOrReplace("siteNo",strings[0]);
            http.addOrReplace("tileNo",strings[1]);
            HttpClient post = http.create();
            post.request();

            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"TileMonitoring : " + s);
            try{
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("tileSensorList");
                JSONArray jsonArray1 = jsonObject.getJSONArray("processStatus");
                JSONObject tileListObject = jsonObject.getJSONObject("tileList");

                JSONArray jsonArray2 = null;
                JSONObject jsonObjectPortOption = null;

                ArrayList<String> testSensorNameList = new ArrayList<>();
                ArrayList<String> testSensorIdList = new ArrayList<>();
                ArrayList<String> testSensorNoList = new ArrayList<>();

                ArrayList<String> sensorModelNoList = new ArrayList<>();
                ArrayList<String> sensorProcessList = new ArrayList<>();
                ArrayList<String> sensorLineList = new ArrayList<>();
                ArrayList<String> sensorLocationList = new ArrayList<>();
                ArrayList<String> sensorGroupList = new ArrayList<>();
                ArrayList<Integer> sensorMonitoring = new ArrayList<>();
                ArrayList<Integer> sensorIndexList = new ArrayList<>();

                port01ListArray = new ArrayList<>();
                port01DateListArray = new ArrayList<>();
                portMonitoringListArray = new ArrayList<>();

                colorListArray = new ArrayList<>();
                txtColorListArray = new ArrayList<>();
                nameListArray = new ArrayList<>();
                priorityListArray = new ArrayList<>();
                portNoListArray = new ArrayList<>();

                txtViewName.setText(tileListObject.getString("TITLE"));

                int sensorCnt = 0;

                ArrayList<String> sendsorIdList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++){

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

                    JSONObject jsonObjectView = jsonArray.getJSONObject(i);

                    if (jsonObjectView.isNull("INDEX_LOCATION") == false){
                        sensorIndexList.add(jsonObjectView.getInt("INDEX_LOCATION"));
                    }else{
                        sensorIndexList.add(100);
                    }

                    if (jsonObjectView.isNull("MODEL_NAME") == false){
                        sensorModelNoList.add(jsonObjectView.getString("MODEL_NAME"));
                    }else{
                        sensorModelNoList.add("");
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

                    if (jsonObjectView.isNull("SENSOR_ID") == false){
                        testSensorIdList.add(jsonObjectView.getString("SENSOR_ID"));
                    }else{
                        testSensorIdList.add("");
                    }

                    if (jsonObjectView.isNull("SENSOR_NO") == false){
                        testSensorNoList.add(jsonObjectView.getString("SENSOR_NO"));

                        if (!jsonObject.isNull("processPortOption")){
                            jsonArray2 = jsonObject.getJSONArray("processPortOption");

                            for (int x = 0; x < jsonArray2.length(); x++) {
                                jsonObjectPortOption = jsonArray2.getJSONObject(x);
                                if (Utils.JsonIsNullCheck(jsonObjectPortOption,"TEXT_COLOR").length() > 0){
                                    for (int y = 0; y < portNoList.size(); y++){
                                        if (portNoList.get(y) == jsonObjectPortOption.getInt("PORT_NO") && jsonObjectPortOption.getInt("SENSOR_NO") == jsonObjectView.getInt("SENSOR_NO")){
                                            colorList.set(y,jsonObjectPortOption.getString("COLOR"));
                                            txtColorList.set(y,jsonObjectPortOption.getString("TEXT_COLOR"));
                                            priorityList.set(y,jsonObjectPortOption.getInt("PRIORITY"));
                                            nameList.set(y,jsonObjectPortOption.getString("NAME"));
                                        }
                                    }
                                }
                            }
                        }
                    }else{
                        testSensorNoList.add("");
                    }

                    if (jsonObjectView.isNull("NAME") == false){
                        testSensorNameList.add(jsonObjectView.getString("NAME"));
                    }else{
                        testSensorNameList.add("");
                    }

                    if (!sendsorIdList.contains(jsonObjectView.getString("SENSOR_ID"))){
                        sensorCnt++;
                        sendsorIdList.add(jsonObjectView.getString("SENSOR_ID"));
                    }

                    port01List = new ArrayList<>();
                    port01DateList = new ArrayList<>();
//                    portMonitorList = new ArrayList<>();

                    for (int x = 0; x < jsonArray1.length(); x++){
                        JSONObject jsonObjectView1 = jsonArray1.getJSONObject(x);
                        if (jsonObjectView.getString("SENSOR_NO").equals(jsonObjectView1.getString("SENSOR_NO"))){
                            if (jsonObjectView1.isNull("MONITORING")==false){
                                portMonitorList.add(jsonObjectView1.getInt("MONITORING"));
                            }else{
                                portMonitorList.add(0);
                            }

                            if (jsonObjectView1.isNull("PORT1")==false){
                                port01List.add(jsonObjectView1.getInt("PORT1"));
                            }else{
                                port01List.add(0);
                            }
                            if (jsonObjectView1.isNull("PORT2")==false){
                                port01List.add(jsonObjectView1.getInt("PORT2"));
                            }else{
                                port01List.add(0);
                            }
                            if (jsonObjectView1.isNull("PORT3")==false){
                                port01List.add(jsonObjectView1.getInt("PORT3"));
                            }else{
                                port01List.add(0);
                            }
                            if (jsonObjectView1.isNull("PORT4")==false){
                                port01List.add(jsonObjectView1.getInt("PORT4"));
                            }else{
                                port01List.add(0);
                            }
                            if (jsonObjectView1.isNull("PORT5")==false){
                                port01List.add(jsonObjectView1.getInt("PORT5"));
                            }else{
                                port01List.add(0);
                            }
                            if (jsonObjectView1.isNull("PORT6")==false){
                                port01List.add(jsonObjectView1.getInt("PORT6"));
                            }else{
                                port01List.add(0);
                            }
                            if (jsonObjectView1.isNull("PORT7")==false){
                                port01List.add(jsonObjectView1.getInt("PORT7"));
                            }else{
                                port01List.add(0);
                            }
                            if (jsonObjectView1.isNull("PORT8")==false){
                                port01List.add(jsonObjectView1.getInt("PORT8"));
                            }else{
                                port01List.add(0);
                            }

                            if (jsonObjectView1.isNull("PORT1_ON")==false){
                                port01DateList.add(jsonObjectView1.getString("PORT1_ON"));
                            }else{
                                port01DateList.add("");
                            }
                            if (jsonObjectView1.isNull("PORT2_ON")==false){
                                port01DateList.add(jsonObjectView1.getString("PORT2_ON"));
                            }else{
                                port01DateList.add("");
                            }
                            if (jsonObjectView1.isNull("PORT3_ON")==false){
                                port01DateList.add(jsonObjectView1.getString("PORT3_ON"));
                            }else{
                                port01DateList.add("");
                            }
                            if (jsonObjectView1.isNull("PORT4_ON")==false){
                                port01DateList.add(jsonObjectView1.getString("PORT4_ON"));
                            }else{
                                port01DateList.add("");
                            }
                            if (jsonObjectView1.isNull("PORT5_ON")==false){
                                port01DateList.add(jsonObjectView1.getString("PORT5_ON"));
                            }else{
                                port01DateList.add("");
                            }
                            if (jsonObjectView1.isNull("PORT6_ON")==false){
                                port01DateList.add(jsonObjectView1.getString("PORT6_ON"));
                            }else{
                                port01DateList.add("");
                            }
                            if (jsonObjectView1.isNull("PORT7_ON")==false){
                                port01DateList.add(jsonObjectView1.getString("PORT7_ON"));
                            }else{
                                port01DateList.add("");
                            }
                            if (jsonObjectView1.isNull("PORT8_ON")==false){
                                port01DateList.add(jsonObjectView1.getString("PORT8_ON"));
                            }else{
                                port01DateList.add("");
                            }
                        }
                    }

                    txtSensorCnt.setText(""+sensorCnt);
                    port01ListArray.add(port01List);
                    port01DateListArray.add(port01DateList);
                    portMonitoringListArray.add(portMonitorList);
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

                for (int x = 0; x < testSensorNoList.size(); x++){
                    for (int y = 0; y < testSensorNoList.size(); y++){
                        if (sensorIndexList.get(x) < sensorIndexList.get(y)){
                            int index = sensorIndexList.get(x);
                            sensorIndexList.set(x,sensorIndexList.get(y));
                            sensorIndexList.set(y,index);

                            String id = testSensorIdList.get(x);
                            testSensorIdList.set(x,testSensorIdList.get(y));
                            testSensorIdList.set(y,id);

                            String name = testSensorNameList.get(x);
                            testSensorNameList.set(x,testSensorNameList.get(y));
                            testSensorNameList.set(y,name);

                            String mode = sensorModelNoList.get(x);
                            sensorModelNoList.set(x,sensorModelNoList.get(y));
                            sensorModelNoList.set(y,mode);

                            String process = sensorProcessList.get(x);
                            sensorProcessList.set(x,sensorProcessList.get(y));
                            sensorProcessList.set(y,process);

                            String line = sensorLineList.get(x);
                            sensorLineList.set(x,sensorLineList.get(y));
                            sensorLineList.set(y,line);

                            String location = sensorLocationList.get(x);
                            sensorLocationList.set(x,sensorLocationList.get(y));
                            sensorLocationList.set(y,location);

                            String group = sensorGroupList.get(x);
                            sensorGroupList.set(x,sensorGroupList.get(y));
                            sensorGroupList.set(y,group);

                            ArrayList<Integer> list = port01ListArray.get(x);
                            port01ListArray.set(x,port01ListArray.get(y));
                            port01ListArray.set(y,list);

                            ArrayList<String> dateList = port01DateListArray.get(x);
                            port01DateListArray.set(x,port01DateListArray.get(y));
                            port01DateListArray.set(y,dateList);

                            ArrayList<String> colorList = colorListArray.get(x);
                            colorListArray.set(x,colorListArray.get(y));
                            colorListArray.set(y,colorList);

                            ArrayList<String> txtColorList = txtColorListArray.get(x);
                            txtColorListArray.set(x,txtColorListArray.get(y));
                            txtColorListArray.set(y,txtColorList);

                            ArrayList<String> nameList = nameListArray.get(x);
                            nameListArray.set(x,nameListArray.get(y));
                            nameListArray.set(y,nameList);

                            ArrayList<Integer> priorityList = priorityListArray.get(x);
                            priorityListArray.set(x,priorityListArray.get(y));
                            priorityListArray.set(y,priorityList);

                            ArrayList<Integer> portNoList = portNoListArray.get(x);
                            portNoListArray.set(x,portNoListArray.get(y));
                            portNoListArray.set(y,portNoList);
                        }
                    }
                }

                for (int j = 0; j < testSensorNameList.size(); j++){
                        GridListViewMake(testSensorIdList.get(j), testSensorNameList.get(j), port01ListArray.get(j), colorListArray.get(j),priorityListArray.get(j),nameListArray.get(j),
                                sensorModelNoList.get(j),sensorProcessList.get(j),sensorLineList.get(j),sensorLocationList.get(j),sensorGroupList.get(j),port01DateListArray.get(j),portMonitoringListArray.get(j),txtColorListArray.get(j));
                }

                testI++;

            }catch (JSONException e){

            }
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                gridLayout.invalidate();
            }
        }
    };

    @Override
    public void onPause(){
        super.onPause();
        timer2.cancel();
        Log.i(TAG,"onPause");
    }

    @Override
    public void onResume(){
        super.onResume();

        scaleCount = gridLayout.getColumnCount();

        txtSensorIdList.clear();
        txtSensorNameList.clear();
        txtSensorStatusList.clear();

        if (pref.getInt("badgeCnt",0) > 0){
            frameBadge.setVisibility(View.VISIBLE);
            txtBadge.setText("" + pref.getInt("badgeCnt",0));
        }else{
            frameBadge.setVisibility(View.GONE);
        }

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);

        oneCheck = false;

        gridLayout.removeAllViews();

        new SiteColorListServer().execute(String.valueOf(Utils.USER_SITE_NO));

        colorArrayList = new ArrayList<>();
        priorityNumber = new ArrayList<>();
        priorityArrayList = new ArrayList<>();

        sensorNameArray = new ArrayList<>();
        sensorModelNoArray = new ArrayList<>();
        sensorProcessArray = new ArrayList<>();
        sensorLineArray = new ArrayList<>();
        sensorLocationArray = new ArrayList<>();
        sensorGroupArray = new ArrayList<>();

        Log.i(TAG,"tileNo : " + Utils.TILE_NO);

        if (Utils.TILE_NO.length() != 0){
            new TileListServer().execute(String.valueOf(Utils.USER_SITE_NO),Utils.TILE_NO);
        }else{
            new SelectTileListServer().execute(String.valueOf(Utils.USER_SITE_NO));
        }

        sensorCnt = 0;

        timer2 = new CountDownTimer(60000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {

                testI = 0;
                aa++;
                if (gridLayout != null) {
                    handler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onFinish() {
                sensorCnt = 0;
                aa = 0;

                gridLayout.removeAllViews();

                if (Utils.TILE_NO.length() != 0){
                    new TileListServer().execute(String.valueOf(Utils.USER_SITE_NO),Utils.TILE_NO);
                }else{
                    new SelectTileListServer().execute(String.valueOf(Utils.USER_SITE_NO));
                }
                timer2.start();
            }
        };
        timer2.start();
    }

    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            if (detector.getCurrentSpan() > 200 && detector.getTimeDelta() > 400) {
                if (detector.getCurrentSpan() - detector.getPreviousSpan() < -1) {
                    if (gridLayout.getColumnCount() == 2) {
                        changeColumnCount(3);
                        return true;
                    } else if (gridLayout.getColumnCount() == 3){
                        changeColumnCount(4);
                        return true;
                    } else if (gridLayout.getColumnCount() == 4){
                        changeColumnCount(5);
                        return true;
                    }
                } else if(detector.getCurrentSpan() - detector.getPreviousSpan() > 1) {
                    if (gridLayout.getColumnCount() == 5) {
                        changeColumnCount(4);
                        return true;
                    } else if (gridLayout.getColumnCount() == 4) {
                        changeColumnCount(3);
                        return true;
                    } else if (gridLayout.getColumnCount() == 3){
                        changeColumnCount(2);
                        return true;
                    }
                }
            }

            gridLayout.invalidate();
            return false;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
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
            case R.id.linear_tile_hamberger : {
                drawerLayout.openDrawer(drawerView);
                drawerView.setClickable(true);
                break;
            }
            case R.id.txt_side_monitoring_child_process : {
                drawerLayout.closeDrawer(drawerView);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                break;
            }
            case R.id.txt_side_monitoring_child_tile : {
                drawerLayout.closeDrawer(drawerView);
                break;
            }
            case R.id.linear_tile_monitor : {
                if (linearMonitorChildVisible.getVisibility() == View.VISIBLE){
                    collapse(linearMonitorChildVisible);
                    imgSideMonitoringArrow.setRotation(360);
                }else{
                    expand(linearMonitorChildVisible);
                    imgSideMonitoringArrow.setRotation(180);
                }
                break;
            }
            case R.id.linear_tile_select : {
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
            case R.id.linear_tile_message : {
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

            case R.id.linear_tile_setting : {
                drawerLayout.closeDrawer(drawerView);
                Intent i = new Intent(getApplicationContext(), SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            }
            case R.id.linear_tile_logout : {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(TileViewActivity.this);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        display = windowManager.getDefaultDisplay();
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            linearStatusBar.setVisibility(View.VISIBLE);
            if (linearDialogTest.getVisibility() == View.VISIBLE){
                linearDialogTest.setVisibility(View.GONE);
                dialog.show();
            }
            rotationGrid(gridLayout.getColumnCount());
        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            linearStatusBar.setVisibility(View.GONE);
            if (dialog.isShowing()){
                dialog.dismiss();
                linearDialogTest.setVisibility(View.VISIBLE);
            }
            rotationGrid(gridLayout.getColumnCount());
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
            updateIconBadge(getApplicationContext(),0);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
            editor.putInt("login_flag",0);
            editor.putInt("badgeCnt",0);
            editor.commit();
            Utils.TILE_NO = "";
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "Click Back button again to end the app.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            ActivityCompat.finishAffinity(this);
            System.runFinalizersOnExit(true);
            System.exit(0);
        }
    }
}
