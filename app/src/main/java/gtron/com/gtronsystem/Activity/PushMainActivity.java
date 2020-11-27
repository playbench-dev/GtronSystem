package gtron.com.gtronsystem.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.BackPressCloseHandler;
import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.Utils.Utils;

import static gtron.com.gtronsystem.Utils.Utils.NullCheck;

public class PushMainActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "PushMainActivity";
    private BackPressCloseHandler backPressCloseHandler;
    private DrawerLayout drawerLayout;
    private View drawerView;
    LinearLayout linearHamberger;
    ImageView imgDelete;
    LinearLayout linearListParent;
    LinearLayout linearMessage,linearSetting,linearLogout;
    TextView navName,navEmail;
    LinearLayout linearMessageVisible;
    TextView txtMessageReceive,txtMessageSend;

    Activity activity;
    ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();
    ArrayList<View> viewArrayList = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayList<String> deleteMessageList = new ArrayList<>();
    ArrayList<View> deleteViewList = new ArrayList<>();
    ArrayList<Integer> badgeDeleteList = new ArrayList<>();
    int badgeDeleteCnt = 0;
    ArrayList<Integer> aliveFlagList = new ArrayList<>();
    public static FrameLayout frameBadge;
    public static TextView txtBadge;
    ImageView imgLogo;
    CheckBox chbAll;
    ArrayList<CheckBox> allCheckBoxList = new ArrayList<>();
    ArrayList<String> allDeleteMessageList = new ArrayList<>();
    ArrayList<View> allDeleteViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_main);

        backPressCloseHandler = new BackPressCloseHandler(this);

        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();

        FindViewById();

        NullCheck(this);

        editor.putInt("badgeCnt",0);
        editor.commit();
        updateIconBadge(this,0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMessageList.size() == 0){

                }else{
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(PushMainActivity.this);
                    alert_confirm.setMessage("Do you want to delete?").setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = 0; i < deleteMessageList.size(); i++) {
                                        linearListParent.removeView(deleteViewList.get(i));
                                        DeleteMessageNextServer deleteMessageNextServer = new DeleteMessageNextServer();
                                        deleteMessageNextServer.execute(deleteMessageList.get(i));
                                    }
                                    chbAll.setChecked(false);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'No'
                                    dialog.dismiss();
                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                }
            }
        });

        chbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxArrayList.clear();
                deleteMessageList.clear();
                deleteViewList.clear();
                if (chbAll.isChecked()){
                    for (int i = 0; i < allCheckBoxList.size(); i++){
                        allCheckBoxList.get(i).setChecked(true);
                        deleteMessageList.add(allDeleteMessageList.get(i));
                        deleteViewList.add(allDeleteViewList.get(i));
                        checkBoxArrayList.add(allCheckBoxList.get(i));
                    }
                }else{
                    for (int i = 0; i < allCheckBoxList.size(); i++){
                        allCheckBoxList.get(i).setChecked(false);
                    }
                }
            }
        });

    }

    void FindViewById(){
        //main
        linearHamberger = (LinearLayout)findViewById(R.id.linear_push_main_hamberger);
        imgDelete = (ImageView)findViewById(R.id.img_push_main_delete);
        linearListParent = (LinearLayout)findViewById(R.id.linear_push_main_list_parent);
        chbAll = (CheckBox)findViewById(R.id.chb_push_message_check_all);

        //drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);
        navName = (TextView) drawerLayout.findViewById(R.id.nav_name);
        navEmail = (TextView) drawerLayout.findViewById(R.id.nav_email);
        linearMessage = (LinearLayout)drawerLayout.findViewById(R.id.linear_push_main_message);
        linearSetting = (LinearLayout)drawerLayout.findViewById(R.id.linear_push_main_setting);
        linearLogout = (LinearLayout)drawerLayout.findViewById(R.id.linear_push_main_logout);
        linearMessageVisible = (LinearLayout)findViewById(R.id.linear_side_message_child_visible);
        txtMessageReceive = (TextView)findViewById(R.id.txt_side_message_child_receive);
        txtMessageSend = (TextView)findViewById(R.id.txt_side_message_child_send);
        frameBadge = (FrameLayout)drawerLayout.findViewById(R.id.frame_push_main_left_badge);
        txtBadge = (TextView)drawerLayout.findViewById(R.id.txt_push_main_left_badge);
        imgLogo = (ImageView)drawerLayout.findViewById(R.id.imageView);

        linearHamberger.setOnClickListener(this);
        linearMessage.setOnClickListener(this);
        txtMessageReceive.setOnClickListener(this);
        txtMessageSend.setOnClickListener(this);
        linearSetting.setOnClickListener(this);
        linearLogout.setOnClickListener(this);

        navName.setText(Utils.USER_NAME);
        navEmail.setText(Utils.USER_EMAIL);

        if (Utils.LOGO_PATH.length() == 0){
            imgLogo.setImageResource(R.drawable.noimage);
        }else{
            Glide.with(this).load(Utils.LOGO_PATH).into(imgLogo);
        }
    }

    void viewMake(final int i, final String msgNo, final String sensorName, final String message, final String dayday, final String flag){
        View viewLayout = new View(getApplicationContext());
        viewLayout = getLayoutInflater().inflate(R.layout.message_list_item,null);

        LinearLayout linearItemParent = (LinearLayout)viewLayout.findViewById(R.id.linear_message_item_parent);
        TextView txtTitle = (TextView) viewLayout.findViewById(R.id.message_list_item_title) ;
        TextView txtMessage = (TextView) viewLayout.findViewById(R.id.message_list_item_message) ;
        TextView txtDate = (TextView) viewLayout.findViewById(R.id.message_list_item_date_string) ;
        final ImageView imgNew = (ImageView)viewLayout.findViewById(R.id.img_message_new);
        final CheckBox chbCheck = (CheckBox)viewLayout.findViewById(R.id.chb_message_check);


        if (!flag.equals("1")){
            imgNew.setVisibility(View.INVISIBLE);
        }
        linearListParent.addView(viewLayout);
        viewArrayList.add(viewLayout);

        allCheckBoxList.add(chbCheck);
        allDeleteMessageList.add(msgNo);
        allDeleteViewList.add(viewLayout);

        txtTitle.setText(sensorName);
        txtDate.setText(dayday);
        txtMessage.setText(message);

        final View finalViewLayout1 = viewLayout;
        chbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true){
                    checkBoxArrayList.add(chbCheck);
                    deleteMessageList.add(msgNo);
                    deleteViewList.add(finalViewLayout1);
                }else{
                    checkBoxArrayList.remove(chbCheck);
                    deleteMessageList.remove(msgNo);
                    deleteViewList.remove(finalViewLayout1);
                    chbAll.setChecked(false);
                }
            }
        });

        linearItemParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    imgNew.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(PushMainActivity.this,MessageDetailActivity.class);
                    intent.putExtra("title",sensorName);
                    intent.putExtra("message",message);
                    intent.putExtra("date",dayday);
                    startActivity(intent);
                }else if (event.getAction() == MotionEvent.ACTION_CANCEL){

                }
                return true;
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

    public class UpdateMessageNextServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.UpdateMessage());

            http.addOrReplace("userNo",strings[0]);
            http.addOrReplace("siteNo",strings[1]);

            HttpClient post = http.create();

            post.request();
            String body = post.getBody();

            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"update : " + s);
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

            linearListParent.removeAllViews();
            aliveFlagList = new ArrayList<>();

            try {
                JSONObject jsonObjectMS = new JSONObject(s);
                JSONArray jsonArrayMS = jsonObjectMS.getJSONArray("list");
                for (int i = 0; i < jsonArrayMS.length(); i++){
                    JSONObject jsonObject = jsonArrayMS.getJSONObject(i);
                    viewMake(i,jsonObject.getString("MSG_NO"),jsonObject.getString("SENSOR_NAME"),jsonObject.getString("MESSAGE"),jsonObject.getString("CREATE_DT"),jsonObject.getString("ALIVE_FLAG"));
                    if (jsonObject.getInt("ALIVE_FLAG")==1){
                        aliveFlagList.add(jsonObject.getInt("ALIVE_FLAG"));
                    }
                }
                if (aliveFlagList.size()!=0){
                    updateIconBadge(getApplicationContext(),aliveFlagList.size());
                }

            }catch (Exception e){

            }

        }
    }

    public class DeleteMessageNextServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            // Http 요청 준비 작업
            HttpClient.Builder http = new HttpClient.Builder("POST", ServerUtils.DeleteMessage());

            http.addOrReplace("msgNo",strings[0]);

            HttpClient post = http.create();

            post.request();
            String body = post.getBody();


            return body;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG,"delete : " + s);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear_push_main_hamberger : {
                drawerLayout.openDrawer(drawerView);
                break;
            }
            case R.id.linear_push_main_message : {
                drawerLayout.closeDrawer(drawerView);
                frameBadge.setVisibility(View.GONE);
                editor.putInt("badgeCnt",0);
                editor.commit();
                break;
            }
            case R.id.txt_side_message_child_receive : {
                drawerLayout.closeDrawer(drawerView);
                Intent i = new Intent(getApplicationContext(), MessageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            }
            case R.id.txt_side_message_child_send : {

                break;
            }
            case R.id.linear_push_main_setting : {
                drawerLayout.closeDrawer(drawerView);
                Intent i = new Intent(getApplicationContext(), SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            }
            case R.id.linear_push_main_logout : {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(PushMainActivity.this);
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
    protected void onResume() {
        super.onResume();

        if (linearListParent.getChildCount()!=0) {
            linearListParent.removeAllViews();
        }

        if (pref.getInt("badgeCnt",0) > 0){
            frameBadge.setVisibility(View.VISIBLE);
            txtBadge.setText("" + pref.getInt("badgeCnt",0));
        }else{
            frameBadge.setVisibility(View.GONE);
        }

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);

        aliveFlagList = new ArrayList<>();
        badgeDeleteList = new ArrayList<>();
        deleteMessageList = new ArrayList<>();
        deleteViewList = new ArrayList<>();
        checkBoxArrayList = new ArrayList<>();
        viewArrayList = new ArrayList<>();

        new SelectMessageNextServer().execute(String.valueOf(Utils.USER_NO), String.valueOf(Utils.USER_SITE_NO));
        new UpdateMessageNextServer().execute(String.valueOf(Utils.USER_NO) ,String.valueOf(Utils.USER_SITE_NO));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
