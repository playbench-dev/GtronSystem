package gtron.com.gtronsystem.Activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gtron.com.gtronsystem.Utils.HttpClient;
import gtron.com.gtronsystem.Utils.ServerUtils;
import gtron.com.gtronsystem.MessageFunc.MessageListAdapter;
import gtron.com.gtronsystem.R;
import gtron.com.gtronsystem.Utils.Utils;

public class MessageActivity extends AppCompatActivity {
    private String TAG = "MessageActivity";
    ProgressDialog asyncDialog;
    LinearLayout linearBack;
    public static ImageView imgDelete;
    LinearLayout linearParent;
    ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();
    ArrayList<View> viewArrayList = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    static int badgeCnt;
    ArrayList<String> deleteMessageList = new ArrayList<>();
    ArrayList<View> deleteViewList = new ArrayList<>();
    ArrayList<Integer> badgeDeleteList = new ArrayList<>();
    int badgeDeleteCnt = 0;
    ScrollView scrMessage;
    ArrayList<Integer> aliveFlagList = new ArrayList<>();
    CheckBox chbAll;
    ArrayList<CheckBox> allCheckBoxList = new ArrayList<>();
    ArrayList<String> allDeleteMessageList = new ArrayList<>();
    ArrayList<View> allDeleteViewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        pref = getSharedPreferences("GetronSystem",MODE_PRIVATE);
        editor = pref.edit();

        linearBack = (LinearLayout)findViewById(R.id.linear_message_back);
        imgDelete = (ImageView)findViewById(R.id.img_message_delete);
        linearParent = (LinearLayout)findViewById(R.id.linear_message_parent);
        scrMessage = (ScrollView)findViewById(R.id.scr_message_parent);
        chbAll = (CheckBox)findViewById(R.id.chb_message_check_all);

        asyncDialog = new ProgressDialog(this);

        linearBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMessageList.size() == 0){

                }else{

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MessageActivity.this);
                    alert_confirm.setMessage("Do you want to delete?").setCancelable(false).setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (int i = 0; i < deleteMessageList.size(); i++) {
                                        linearParent.removeView(deleteViewList.get(i));
                                        DeleteMessageNextServer deleteMessageNextServer = new DeleteMessageNextServer();
                                        deleteMessageNextServer.execute(deleteMessageList.get(i));
                                    }
                                    chbAll.setChecked(false);
                                    imgDelete.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        Intent beforeIntent = getIntent();
        if (beforeIntent.hasExtra("notification")){
            Intent intent = new Intent(this,TileViewActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();
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
        }else{
            imgNew.setVisibility(View.VISIBLE);
        }

        linearParent.addView(viewLayout);
        viewArrayList.add(viewLayout);

        txtTitle.setText(sensorName);
        txtDate.setText(dayday);
        txtMessage.setText(message);
        allCheckBoxList.add(chbCheck);
        allDeleteMessageList.add(msgNo);
        allDeleteViewList.add(viewLayout);

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

                if (checkBoxArrayList.size()>0){
                    imgDelete.setVisibility(View.VISIBLE);
                }else{
                    imgDelete.setVisibility(View.GONE);
                }
            }
        });

        linearItemParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                }else if (event.getAction() == MotionEvent.ACTION_UP){
                    imgNew.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(MessageActivity.this,MessageDetailActivity.class);
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

            linearParent.removeAllViews();
            aliveFlagList = new ArrayList<>();

            try {
                JSONObject jsonObjectMS = new JSONObject(s);
                JSONArray jsonArrayMS = jsonObjectMS.getJSONArray("list");
                for (int i = 0; i < jsonArrayMS.length(); i++){
                    JSONObject jsonObject = jsonArrayMS.getJSONObject(i);
                    viewMake(i,Utils.JsonIsNullCheck(jsonObject,"MSG_NO"),Utils.JsonIsNullCheck(jsonObject,"SENSOR_NAME"),
                            Utils.JsonIsNullCheck(jsonObject,"MESSAGE"),Utils.JsonIsNullCheck(jsonObject,"CREATE_DT"),Utils.JsonIsNullCheck(jsonObject,"ALIVE_FLAG"));
                    if (Utils.JsonIntIsNullCheck(jsonObject,"ALIVE_FLAG")==1){
                        aliveFlagList.add(Utils.JsonIntIsNullCheck(jsonObject,"ALIVE_FLAG"));
                    }
                }
            }catch (Exception e){

            }
            new UpdateMessageNextServer().execute(String.valueOf(Utils.USER_NO) ,String.valueOf(Utils.USER_SITE_NO));
        }
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
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (notificationManager != null) {
                    notificationManager.cancelAll();
                }
            }
        }
    }

    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
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
    public void onResume(){
        super.onResume();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        updateIconBadge(this,0);

        if (linearParent.getChildCount()!=0) {
            linearParent.removeAllViews();
        }

        aliveFlagList = new ArrayList<>();
        badgeDeleteList = new ArrayList<>();
        deleteMessageList = new ArrayList<>();
        deleteViewList = new ArrayList<>();
        checkBoxArrayList = new ArrayList<>();
        viewArrayList = new ArrayList<>();

        new SelectMessageNextServer().execute(String.valueOf(Utils.USER_NO), String.valueOf(Utils.USER_SITE_NO));

        editor.putInt("badgeCnt",0);
        editor.commit();
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
}
